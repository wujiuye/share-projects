package com.wujiuye.dubbo.sub;

import com.wujiuye.dubbo.interfaces.OrderService;

public class SubOrderService extends BaseSubService<OrderService>{
    public SubOrderService() {
        super(OrderService.class);
    }
}
