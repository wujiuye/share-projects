package com.wujiuye.sck.provider.model.props;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wujiuye 2020/06/05
 */
//@Component
public class PropsListener implements InitializingBean, ApplicationListener<RefreshScopeRefreshedEvent> {

    @Resource
    private DemoProps demoProps;

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        System.out.println(demoProps);
        System.out.println(demoProps.hashCode());
        System.out.println(event.getName());
        System.out.println(event.getSource());
        // 借助异常打印调用栈
        try {
            throw new Exception("onApplicationEvent RefreshScopeRefreshedEvent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(demoProps.hashCode());
    }

}
