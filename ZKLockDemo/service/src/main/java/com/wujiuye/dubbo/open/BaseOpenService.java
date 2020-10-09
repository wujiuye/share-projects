package com.wujiuye.dubbo.open;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;

public abstract class BaseOpenService<T> {

    public abstract void openService();

    /**
     * 开放一个接口
     * @param impl  接口的实现类
     */
    protected void openService(T impl){
        ServiceConfig<T> serviceConfig = new ServiceConfig<>();
        //配置开放的接口
        serviceConfig.setInterface(impl.getClass().getInterfaces()[0]);
        //配置协议
        serviceConfig.setProtocol(new ProtocolConfig("dubbo",-1));
        //配置实现类
        serviceConfig.setRef(impl);
        //配置应用实例（告知这是哪个应用提供的）
        serviceConfig.setApplication(new ApplicationConfig("server-app"));
        //配置注册中心
        serviceConfig.setRegistry(new RegistryConfig("multicast://224.5.6.7:12345"));
        serviceConfig.setVersion("1.0.0");
        serviceConfig.setTimeout(1000);//设置超时时间
        serviceConfig.setDelay(-1);//超时不重试
        //开始提供服务
        serviceConfig.export();
    }

}
