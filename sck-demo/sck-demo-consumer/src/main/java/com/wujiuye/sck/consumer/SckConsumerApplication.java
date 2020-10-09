package com.wujiuye.sck.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * sck-demo消费者
 *
 * @author wujiuye 2020/06/05
 */
@EnableFeignClients(basePackages = {"com.wujiuye.sck.provider.client"})
//@EnableDiscoveryClient
@SpringBootApplication
public class SckConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SckConsumerApplication.class, args);
    }

}
