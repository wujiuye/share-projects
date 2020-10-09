package com.wujiuye.dubbo.sub;

import com.wujiuye.dubbo.interfaces.GoodsService;
import com.wujiuye.dubbo.pojo.Goods;

public class SubGoodsService extends BaseSubService<GoodsService>{

    public SubGoodsService() {
        super(GoodsService.class);
    }
}
