package com.wujiuye.sck.consumer.config;

import com.netflix.client.ClientException;
import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import org.springframework.cloud.netflix.ribbon.*;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 支付后台配置
 *
 * @author wujiuye 2020/07/06
 */
@Configuration
public class FeignRibbonConfiguration {

    @Resource
    private SpringClientFactory springClientFactory;

    /**
     * 自定义FeignLoadBalancer，替换默认的RequestSpecificRetryHandler
     */
    public static class MyFeignLoadBalancer extends FeignLoadBalancer {

        public MyFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector) {
            super(lb, clientConfig, serverIntrospector);
        }

        @Override
        public RequestSpecificRetryHandler getRequestSpecificRetryHandler(RibbonRequest request, IClientConfig requestConfig) {
            // 返回自定义的RequestSpecificRetryHandler
            // 参数一：是否连接异常重试时重试
            // 参数二：是否所有异常都重试
            return new RequestSpecificRetryHandler(false, false,
                    getRetryHandler(), requestConfig) {
                /**
                 * @param e 抛出的异常
                 * @param sameServer 是否同节点服务的重试
                 * @return
                 */
                @Override
                public boolean isRetriableException(Throwable e, boolean sameServer) {
                    if (e instanceof ClientException) {
                        // 连接异常重试
                        if (((ClientException) e).getErrorType() == ClientException.ErrorType.CONNECT_EXCEPTION) {
                            return true;
                        }
                        // 连接超时重试
                        if (((ClientException) e).getErrorType() == ClientException.ErrorType.SOCKET_TIMEOUT_EXCEPTION) {
                            return true;
                        }
                        // 读超时重试，读超时重试只允许不同服务节点的重试
                        // 所以同节点的重试不支持，读超时了就不要重新请求同一个节点了。
                        if (((ClientException) e).getErrorType() == ClientException.ErrorType.READ_TIMEOUT_EXCEPTION) {
                            return !sameServer;
                        }
                        // 服务端异常
                        // 服务端异常切换新节点重试
                        if (((ClientException) e).getErrorType() == ClientException.ErrorType.SERVER_THROTTLED) {
                            return !sameServer;
                        }
                    }
                    // 连接异常时重试
                    return isConnectionException(e);
                }
            };
        }
    }

    /**
     * 使用自定义FeignLoadBalancer缓存工厂
     *
     * @return
     */
    @Bean
    public CachingSpringLoadBalancerFactory cachingSpringLoadBalancerFactory() {
        return new CachingSpringLoadBalancerFactory(springClientFactory) {

            private volatile Map<String, FeignLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

            @Override
            public FeignLoadBalancer create(String clientName) {
                FeignLoadBalancer client = this.cache.get(clientName);
                if (client != null) {
                    return client;
                }
                IClientConfig config = this.factory.getClientConfig(clientName);
                ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
                ServerIntrospector serverIntrospector = this.factory.getInstance(clientName,
                        ServerIntrospector.class);
                // 使用自定义的FeignLoadBalancer
                client = new MyFeignLoadBalancer(lb, config, serverIntrospector);
                this.cache.put(clientName, client);
                return client;
            }
        };
    }

}
