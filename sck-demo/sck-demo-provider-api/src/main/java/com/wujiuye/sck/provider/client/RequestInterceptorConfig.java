package com.wujiuye.sck.provider.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;

/**
 * 请求拦截器配置
 *
 * @author wujiuye 2020/07/06
 */
@AutoConfigureBefore(FeignClientsConfiguration.class)
public class RequestInterceptorConfig {

    /**
     * 如果有需要的话
     *
     * @return
     */
    // @Bean("headerRequestInterceptor")
    public RequestInterceptor headerRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("list", "test");
            }
        };
    }

    /**
     * 如果有需要的话
     *
     * @return
     */
    // @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("test", "test");
    }

}
