package com.wujiuye.sck.common.error;

import com.wujiuye.sck.common.api.ResultCode;
import lombok.Getter;

/**
 * 业务统一抛出异常类型
 *
 * @author wujiuye 2020/06/05
 */
public class ServiceException extends RuntimeException {

    @Getter
    private final ResultCode resultCode;

    public ServiceException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILURE;
    }

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public ServiceException(ResultCode resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }

    public ServiceException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
        this.resultCode = ResultCode.FAILURE;
    }

}
