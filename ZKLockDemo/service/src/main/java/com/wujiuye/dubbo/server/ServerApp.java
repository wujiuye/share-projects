package com.wujiuye.dubbo.server;

import com.wujiuye.dubbo.interfaces.impl.GoodsServiceImpl;
import com.wujiuye.dubbo.open.BaseOpenService;
import com.wujiuye.dubbo.open.OpenGoodsService;
import com.wujiuye.dubbo.open.OpenOrderService;
import com.wujiuye.dubbo.open.OpenUserService;
import com.wujiuye.dubbo.pojo.Goods;

import java.io.IOException;

public class ServerApp {


    public static void main(String[] args) throws IOException {
        BaseOpenService userService = new OpenUserService();
        userService.openService();
        BaseOpenService goodsService = new OpenGoodsService();
        goodsService.openService();
        BaseOpenService orderService = new OpenOrderService();
        orderService.openService();

        //初始化商品
        Goods goods = new Goods("abc121312","phone","nokia",1000,100);
        new GoodsServiceImpl().savaGoods(goods);

        System.in.read();
    }

}
