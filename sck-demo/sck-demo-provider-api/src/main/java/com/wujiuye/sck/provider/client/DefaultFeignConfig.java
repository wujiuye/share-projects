package com.wujiuye.sck.provider.client;

import feign.Request;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.clientconfig.FeignClientConfigurer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * 默认Feign配置
 *
 * @author wujiuye 2020/07/06
 */
@AutoConfigureBefore(FeignClientsConfiguration.class)
public class DefaultFeignConfig {

    @Bean
    public FeignClientConfigurer feignClientConfigurer() {
        return new FeignClientConfigurer() {
            @Override
            public boolean inheritParentConfiguration() {
                return true;
            }
        };
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(
                // 连接超时配置
                5, TimeUnit.SECONDS,
                // 读超时配置
                6, TimeUnit.SECONDS,
                // 如果请求响应3xx，是否重定向请求
                false);
    }

}
