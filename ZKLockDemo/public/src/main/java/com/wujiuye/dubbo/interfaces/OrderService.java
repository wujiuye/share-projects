package com.wujiuye.dubbo.interfaces;

import com.wujiuye.dubbo.pojo.Goods;
import com.wujiuye.dubbo.pojo.Order;

public interface OrderService {

    //下单
    Order placeOrder(String goodsName,int number);

}
