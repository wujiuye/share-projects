package com.wujiuye.sck.common.api;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 通用数组响应
 *
 * @author wujiuye 2020/06/05
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ListGenericResponse<T> extends BaseResponse {

    private PageInfo<T> data;

}
