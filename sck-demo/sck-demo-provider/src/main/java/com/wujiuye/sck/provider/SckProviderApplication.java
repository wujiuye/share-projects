package com.wujiuye.sck.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * sck-demo服务提供者
 *
 * @author wujiuye 2020/06/05
 */
@EnableConfigurationProperties
//@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SckProviderApplication {

    static {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/tmp");
    }

    public static void main(String[] args) {
        SpringApplication.run(SckProviderApplication.class, args);
    }

}
