package com.wujiuye.sck.common.api;

import lombok.*;

/**
 * 通用响应
 *
 * @author wujiuye 2020/06/05
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GenericResponse<T> extends BaseResponse {

    private T data;

}
