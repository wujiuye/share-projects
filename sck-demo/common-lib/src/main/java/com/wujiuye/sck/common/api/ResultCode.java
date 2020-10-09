package com.wujiuye.sck.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP统一响应状态码
 *
 * @author william
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(10000, "请求成功"),
    FAILURE(10001, "请求失败"),
    PARAM_MISS(10002, "参数错误"),
    PARAM_TYPE_ERROR(10003, "参数类型错误"),
    PARAM_VALID_ERROR(10004, "参数校验异常"),
    PARAM_BIND_ERROR(10005, "参数绑定异常"),
    NOT_FOUND(10006, "未找到控制器"),
    METHOD_NOT_SUPPORTED(10007, "请求方法不支持"),

    SERVICE_DEGRAD(11000,"服务降级");

    final int code;

    final String msg;
}
