package com.wujiuye.sck.common.aop;

import com.wujiuye.sck.common.api.BaseResponse;
import com.wujiuye.sck.common.api.GenericResponse;
import com.wujiuye.sck.common.api.ListGenericResponse;
import com.wujiuye.sck.common.api.PageInfo;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 如果接口的返回值类型不是BaseResponse或其子类，
 * 则由ResponseBodyAdvice封装为BaseResponse
 * (异常不会走这里)
 *
 * @author wujiuye
 * @version 1.0 on 2020/06/03
 */
@ControllerAdvice
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return !BaseResponse.class.isAssignableFrom(methodParameter.getMethod().getReturnType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        BaseResponse response;
        if (body instanceof PageInfo) {
            response = new ListGenericResponse<>();
            ((ListGenericResponse) response).setData((PageInfo) body);
        } else {
            response = new GenericResponse<>();
            ((GenericResponse) response).setData(body);
        }
        return response;
    }

}
