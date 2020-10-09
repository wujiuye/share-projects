package com.wujiuye.sck.provider.config;

import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.wujiuye.sck.common.config.SckDemoRestConfig;
import com.wujiuye.sck.common.sentinel.DegradeRuleProps;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;

/**
 * 支付接口服务配置
 *
 * @author wujiuye 2020/06/05
 */
@Configuration
@Import(value = SckDemoRestConfig.class)
public class AppConfig {

//    static {
//        FlowRule flowRule = new FlowRule();
//        flowRule.setResource("/v1/test");
//        flowRule.setCount(1);
//        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
//        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        FlowRuleManager.loadRules(Collections.singletonList(flowRule));
//    }

//    static {
//        DegradeRule flowRule = new DegradeRule();
//        flowRule.setResource("/v1/test");
//        flowRule.setCount(1);
//        flowRule.setTimeWindow(1);
//        flowRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
//        flowRule.setMinRequestAmount(1);
//        DegradeRuleManager.loadRules(Collections.singletonList(flowRule));
//    }

}
