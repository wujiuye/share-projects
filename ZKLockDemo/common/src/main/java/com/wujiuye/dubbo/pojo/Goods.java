package com.wujiuye.dubbo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Goods implements Serializable {

    private String goodsId;//商品id
    private String goodsType;//商品类型
    private String goodsName;//商品名称
    private Integer pirce;//价格
    private Integer count;//库存
}
