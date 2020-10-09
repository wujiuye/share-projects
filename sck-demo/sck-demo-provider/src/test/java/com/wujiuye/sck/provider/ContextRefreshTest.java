package com.wujiuye.sck.provider;

import com.wujiuye.sck.provider.model.props.DemoProps;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;

public class ContextRefreshTest extends BaseSpringBootTest implements ApplicationContextAware {

    @Autowired
    private DemoProps demoProps;
    @Resource
    private ContextRefresher contextRefresh;
    private ApplicationContext applicationContext;

    @Test
    public void testContextRefresh() {
        System.out.println(demoProps.getMessage());
        contextRefresh.refresh();
        System.out.println(demoProps.getMessage());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
