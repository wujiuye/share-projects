package com.wujiuye.dubbo.sub;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

public abstract class BaseSubService<T> {

    private Class<?> interfaceClass;

    protected BaseSubService(Class<?> interfaceClass){
        this.interfaceClass = interfaceClass;
    }

    public <T> T getService(){
        ReferenceConfig<T> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(interfaceClass);
        referenceConfig.setRegistry(new RegistryConfig("multicast://224.5.6.7:12345"));
        referenceConfig.setApplication(new ApplicationConfig("client-app"));
        referenceConfig.setVersion("1.0.0");
        return referenceConfig.get();
    }
}
