package com.wujiuye.sck.common.api;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 统一响应
 *
 * @author wujiuye 2020/06/05
 */
@Data
@ToString
@Accessors(chain = true)
public class BaseResponse {

    private int code;
    private String message;

    public BaseResponse() {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.msg;
    }

    public BaseResponse(ResultCode code) {
        this.code = code.getCode();
    }

    public BaseResponse(ResultCode code, String message) {
        this.code = code.getCode();
        this.message = message;
    }

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS.getCode();
    }

}
