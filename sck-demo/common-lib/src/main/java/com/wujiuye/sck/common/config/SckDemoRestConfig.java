package com.wujiuye.sck.common.config;

import com.wujiuye.sck.common.aop.ResponseBodyAdvice;
import com.wujiuye.sck.common.error.GlobalExceptionTranslator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 通用Rest API服务配置
 *
 * @author wujiuye 2020/06/05
 */
@Configuration
@Import(value = {GlobalExceptionTranslator.class, ResponseBodyAdvice.class})
public class SckDemoRestConfig {
}
