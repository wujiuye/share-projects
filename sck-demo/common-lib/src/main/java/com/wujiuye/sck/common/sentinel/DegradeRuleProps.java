package com.wujiuye.sck.common.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 熔断降级规则配置
 *
 * @author wujiuye 2020/07/08
 */
@Data
@ToString
@Component
@RefreshScope
@ConditionalOnClass(DegradeRule.class)
@ConfigurationProperties(prefix = "feign.sentinel.degrade")
public class DegradeRuleProps {

    /**
     * 是否启用熔断降级功能
     */
    private boolean enable = true;
    /**
     * 熔断规则
     */
    private List<DegradeRule> rules;

    @Data
    @ToString
    public static class DegradeRule {
        private String resource;
        private Integer count;
        private Integer timeWindowSecond;
        // 1、DEGRADE_GRADE_EXCEPTION_COUNT = 2
        // 2、DEGRADE_GRADE_EXCEPTION_RATIO = 1
        // 3、DEGRADE_GRADE_RT = 0
        private Integer degradeGrad = RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT;
        /**
         * 可触发RT率断路的最小连续请求数（使用DEGRADE_GRADE_RT时必须，不然一个请求就百分百）
         */
        private int rtSlowRequestAmount = 5;
        /**
         * 可触发断路的最小请求数（在活动统计时间timeWindowSecond范围内）。
         */
        private Integer minRequestAmount = 5;

        public static boolean checkNotNull(DegradeRule rule) {
            return rule != null && rule.count > 0
                    && rule.timeWindowSecond > 0
                    && !StringUtils.isEmpty(rule.resource);
        }

    }

}
