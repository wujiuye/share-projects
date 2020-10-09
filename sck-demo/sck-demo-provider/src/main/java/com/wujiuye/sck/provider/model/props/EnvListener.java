package com.wujiuye.sck.provider.model.props;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wujiuye 2020/06/05
 */
//@Component
public class EnvListener implements InitializingBean, ApplicationListener<EnvironmentChangeEvent> {

    @Resource
    private DemoProps demoProps;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().stream().forEach(System.out::println);
        System.out.println(demoProps);
        System.out.println(demoProps.hashCode());
        // 借助异常打印调用栈
        try {
            throw new Exception("onApplicationEvent EnvironmentChangeEvent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(demoProps.hashCode());
    }
}
