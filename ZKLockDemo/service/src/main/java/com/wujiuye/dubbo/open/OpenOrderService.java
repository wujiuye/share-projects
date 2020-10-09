package com.wujiuye.dubbo.open;

import com.wujiuye.dubbo.interfaces.OrderService;
import com.wujiuye.dubbo.interfaces.impl.OrderServiceImpl;

public class OpenOrderService extends BaseOpenService<OrderService>{
    @Override
    public void openService() {
        super.openService(new OrderServiceImpl());
    }
}
