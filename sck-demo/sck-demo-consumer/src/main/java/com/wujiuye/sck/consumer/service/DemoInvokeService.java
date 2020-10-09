package com.wujiuye.sck.consumer.service;

import com.wujiuye.sck.common.api.ListGenericResponse;
import com.wujiuye.sck.provider.dto.DemoDto;

/**
 * @author wujiuye 2020/06/05
 */
public interface DemoInvokeService {

    ListGenericResponse<DemoDto> invokeDemo();

}
