package com.wujiuye.sck.common.config;

import com.wujiuye.sck.common.aop.ResponseBodyAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 控制台应用服务配置
 *
 * @author wujiuye 2020/06/05
 */
@Configuration
@Import(value = {ResponseBodyAdvice.class})
public class SckDemoWebConfig {
}
