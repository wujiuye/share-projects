package com.wujiuye.sck.consumer.service.impl;

import com.wujiuye.sck.common.api.ListGenericResponse;
import com.wujiuye.sck.provider.client.DemoService;
import com.wujiuye.sck.consumer.service.DemoInvokeService;
import com.wujiuye.sck.provider.dto.DemoDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wujiuye 2020/06/05
 */
@Service
public class DemoInvokeServiceImpl implements DemoInvokeService {

    @Resource
    private DemoService demoService;

    @Override
    public ListGenericResponse<DemoDto> invokeDemo() {
        return demoService.getServices();
    }

}
