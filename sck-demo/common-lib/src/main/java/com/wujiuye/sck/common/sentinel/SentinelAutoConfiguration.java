package com.wujiuye.sck.common.sentinel;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Sentinel自动配置
 *
 * @author wujiuye 2020/07/08
 */
@Configuration
@ConditionalOnClass(DegradeRuleManager.class)
@ConditionalOnProperty(value = "feign.sentinel.enabled", havingValue = "true")
@Import({DegradeRuleDataSource.class})
@EnableConfigurationProperties({DegradeRuleProps.class})
public class SentinelAutoConfiguration {

}
