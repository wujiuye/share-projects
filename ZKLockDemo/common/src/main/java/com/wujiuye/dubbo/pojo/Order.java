package com.wujiuye.dubbo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    private String orderId;//订单id
    private Goods goods;//商品
    private User user;//用户
    private Date date;//下单时间
}
