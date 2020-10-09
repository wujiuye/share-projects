package com.wujiuye.sck.provider.client;

import com.wujiuye.sck.common.api.ListGenericResponse;
import com.wujiuye.sck.common.api.ResultCode;
import com.wujiuye.sck.provider.dto.DemoDto;

/**
 * 接口降级
 *
 * @author wujiuye 2020/07/07
 */
public class ServiceDegradeFallback implements DemoService {

    @Override
    public ListGenericResponse<DemoDto> getServices() {
        ListGenericResponse response = new ListGenericResponse<DemoDto>();
        response.setCode(ResultCode.SERVICE_DEGRAD.getCode())
                .setMessage("服务降级");
        return response;
    }

}
