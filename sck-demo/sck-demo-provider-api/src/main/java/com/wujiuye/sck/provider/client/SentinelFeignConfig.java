package com.wujiuye.sck.provider.client;

import org.springframework.context.annotation.Bean;

/**
 * 熔断处理
 *
 * @author wuijuye 2020/07/07
 */
public class SentinelFeignConfig {

    @Bean
    public ServiceDegradeFallback degradeMockYcpayService() {
        return new ServiceDegradeFallback();
    }

}
