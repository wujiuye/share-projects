package com.wujiuye.sck.consumer.config;

import com.wujiuye.sck.common.config.SckDemoWebConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 支付后台配置
 *
 * @author wujiuye 2020/06/05
 */
@Configuration
@Slf4j
@Import(SckDemoWebConfig.class)
public class AppConfig {
}
