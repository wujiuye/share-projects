package com.wujiuye.dubbo.open;

import com.wujiuye.dubbo.interfaces.GoodsService;
import com.wujiuye.dubbo.interfaces.impl.GoodsServiceImpl;

public class OpenGoodsService extends BaseOpenService<GoodsService>{


    @Override
    public void openService() {
        super.openService(new GoodsServiceImpl());
    }
}
