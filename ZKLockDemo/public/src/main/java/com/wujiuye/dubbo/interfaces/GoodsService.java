package com.wujiuye.dubbo.interfaces;

import com.wujiuye.dubbo.pojo.Goods;

public interface GoodsService {

    Goods queryGoods(String goodsName);

    void savaGoods(Goods goods);

}
