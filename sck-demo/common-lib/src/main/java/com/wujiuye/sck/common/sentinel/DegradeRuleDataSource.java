package com.wujiuye.sck.common.sentinel;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 熔断规则配置数据源
 *
 * @author wujiuye 2020/07/08
 */
@Component
@ConditionalOnClass({AbstractDataSource.class, DegradeRuleManager.class})
public class DegradeRuleDataSource extends AbstractDataSource<DegradeRuleProps, List<DegradeRule>>
        implements ApplicationListener<RefreshScopeRefreshedEvent>,
        InitializingBean {

    @Autowired(required = false)
    private DegradeRuleProps degradeRuleProps;

    public DegradeRuleDataSource() {
        super(degradeRuleProps -> {
            if (degradeRuleProps == null || !degradeRuleProps.isEnable()
                    || CollectionUtils.isEmpty(degradeRuleProps.getRules())) {
                return new ArrayList<>();
            }
            return degradeRuleProps.getRules().parallelStream()
                    .filter(DegradeRuleProps.DegradeRule::checkNotNull)
                    .map(degradeRule -> {
                        DegradeRule rule = new DegradeRule();
                        rule.setResource(degradeRule.getResource());
                        rule.setCount(degradeRule.getCount());
                        rule.setTimeWindow(degradeRule.getTimeWindowSecond());
                        rule.setGrade(degradeRule.getDegradeGrad());
                        rule.setMinRequestAmount(degradeRule.getMinRequestAmount());
                        rule.setRtSlowRequestAmount(degradeRule.getRtSlowRequestAmount());
                        return rule;
                    }).collect(Collectors.toList());
        });
    }

    @Override
    public DegradeRuleProps readSource() throws Exception {
        if (degradeRuleProps == null) {
            return null;
        }
        return degradeRuleProps;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        try {
            List<DegradeRule> rules = loadConfig();
            if (!CollectionUtils.isEmpty(rules)) {
                DegradeRuleManager.loadRules(rules);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        onApplicationEvent(new RefreshScopeRefreshedEvent());
    }

}
