package com.wujiuye.sck.common.api;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 分页支持
 *
 * @author wujiuye 2020/06/05
 */
@Data
@ToString
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int totalCount;
    private long limit;
    private int totalPage;
    private long page;
    private List<T> list;

    public PageInfo(List<T> list, int totalCount, int limit, int page) {
        this.list = list;
        this.totalCount = totalCount;
        this.limit = limit;
        this.page = page;
        this.totalPage = totalCount % limit == 0 ? totalCount / limit : totalCount / limit + 1;
    }

}
