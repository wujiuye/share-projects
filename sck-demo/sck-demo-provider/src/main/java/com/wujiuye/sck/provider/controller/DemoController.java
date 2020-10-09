package com.wujiuye.sck.provider.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.wujiuye.sck.common.api.*;
import com.wujiuye.sck.provider.dto.DemoDto;
import com.wujiuye.sck.provider.model.props.DemoProps;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujiuye 2020/06/05
 */
@RestController
@RequestMapping("/v1")
public class DemoController {

    @Resource
    private DemoProps demoProps;

    @GetMapping("/test")
    public BaseResponse test() {
//        String resourceName = "/v1/test";
//        Entry entry = null;
//        try {
//            ContextUtil.enter(resourceName);
//            entry = SphU.entry(resourceName, EntryType.IN, 1);
//            throw new RuntimeException();
//        } catch (Exception ex) {
//            // fallback handle
//            if (!BlockException.isBlockException(ex)) {
//                Tracer.trace(ex);
//                return new BaseResponse(ResultCode.FAILURE);
//            } else {
//                return new BaseResponse(ResultCode.SUCCESS);
//            }
//        } finally {
//            if (entry != null) {
//                entry.exit(1);
//            }
//            ContextUtil.exit();
//        }
        return new BaseResponse();
    }

//    @Resource
//    private DiscoveryClient discoveryClient;

    @PostMapping("/services")
    public ListGenericResponse<DemoDto> getServices() {
        ListGenericResponse<DemoDto> response = new ListGenericResponse<>();
//        PageInfo<DemoDto> pageInfo = new PageInfo<>(discoveryClient.getServices()
//                .stream().map(DemoDto::new)
//                .collect(Collectors.toList()), discoveryClient.getServices().size(), 100, 1);
        PageInfo<DemoDto> pageInfo = new PageInfo<>(Collections.singletonList(new DemoDto("sck-demo-provider")),
                1, 1, 1);
        response.setData(pageInfo);
        return response;
    }


    /**
     * 不能直接响应DemoProps对象，因为这是一个代理类
     *
     * @return
     */
    @GetMapping("/config")
    public GenericResponse<Map> testConfig() {
        GenericResponse<Map> response = new GenericResponse<>();
        Map<String, String> map = new HashMap<>();
        map.put("message", demoProps.getMessage());
        response.setData(map);
        return response;
    }

}
