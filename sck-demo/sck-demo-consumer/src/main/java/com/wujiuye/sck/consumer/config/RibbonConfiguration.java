package com.wujiuye.sck.consumer.config;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.wujiuye.sck.provider.ProviderConstant;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 针对Ribbon的配置
 *
 * @author wujiuye 2020/07/06
 */
@RibbonClient(name = ProviderConstant.SERVICE_NAME,
        configuration = {
                RibbonConfiguration.MyRibbonClientConfiguration.class
        })
public class RibbonConfiguration {

    @AutoConfigureBefore(RibbonClientConfiguration.class)
    public static class MyRibbonClientConfiguration {

        @Bean
        public IClientConfig ribbonClientConfig() {
            DefaultClientConfigImpl config = new DefaultClientConfigImpl();
            config.setClientName(ProviderConstant.SERVICE_NAME);
            config.set(CommonClientConfigKey.MaxAutoRetries, 1);
            config.setProperty(CommonClientConfigKey.MaxAutoRetriesNextServer, 1);
            config.set(CommonClientConfigKey.ConnectTimeout, 5000);
            config.set(CommonClientConfigKey.ReadTimeout, 5000);
            return config;
        }

    }

}
