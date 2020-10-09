package com.wujiuye.sck.consumer.controller;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.wujiuye.sck.consumer.service.DemoInvokeService;
import com.wujiuye.sck.provider.ProviderConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * demo测试控制器
 *
 * @author wujiuye 2020/06/05
 */
@RestController
@Slf4j
@RequestMapping
public class DemoTestController {

    @Resource
    private DemoInvokeService demoInvokeService;
//    @Resource
//    private DiscoveryClient serverList;

    @Resource
    private SpringClientFactory factory;

    @GetMapping("/demo")
    public Object test2() {
        return demoInvokeService.invokeDemo();
    }

//    @GetMapping("/ServerList")
//    public Object test3() {
//        return serverList.getInstances("sck-demo-provider");
//    }

    @GetMapping("/ServerList")
    public Object test3() {
        return factory.getInstance(ProviderConstant.SERVICE_NAME, ServerList.class)
                .getUpdatedListOfServers();
    }

}
