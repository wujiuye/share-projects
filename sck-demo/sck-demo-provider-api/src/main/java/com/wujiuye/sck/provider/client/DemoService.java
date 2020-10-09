package com.wujiuye.sck.provider.client;

import com.wujiuye.sck.common.api.ListGenericResponse;
import com.wujiuye.sck.provider.ProviderConstant;
import com.wujiuye.sck.provider.dto.DemoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 约定：Controller层不需要实现接口，使用类似于go语言的接口实现方式
 * 本地无注册中心时，需要指定url参数，才能启动服务
 *
 * @author wujiuye 2020/06/05
 */
@FeignClient(name = ProviderConstant.SERVICE_NAME,
        path = "/v1",
        url = "${fegin-client.sck-demo-provider-url}",
        primary = false,
        configuration = {DefaultFeignConfig.class,
                DefaultFeignRetryConfig.class,
                RequestInterceptorConfig.class,
                SentinelFeignConfig.class},
        fallback = ServiceDegradeFallback.class)
public interface DemoService {

    @PostMapping("/services")
    ListGenericResponse<DemoDto> getServices();

}
