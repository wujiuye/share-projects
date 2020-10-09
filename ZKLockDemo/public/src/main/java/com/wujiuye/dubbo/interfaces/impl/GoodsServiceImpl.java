package com.wujiuye.dubbo.interfaces.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wujiuye.dubbo.db.RedisUtils;
import com.wujiuye.dubbo.interfaces.GoodsService;
import com.wujiuye.dubbo.pojo.Goods;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class GoodsServiceImpl implements GoodsService {

    RedisUtils redisUtils = new RedisUtils();

    @Override
    public Goods queryGoods(String goodsName) {
        Jedis jedis = redisUtils.getConn();
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(jedis.get(goodsName));
            Goods goods = new Goods();
            goods.setCount(jsonNode.get("count").asInt());
            goods.setGoodsId(jsonNode.get("goodsId").asText());
            goods.setGoodsName(jsonNode.get("goodsName").asText());
            goods.setGoodsType(jsonNode.get("goodsType").asText());
            goods.setPirce(jsonNode.get("pirce").asInt());
            return goods;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public void savaGoods(Goods goods) {
        Jedis jedis = redisUtils.getConn();
        ObjectMapper om = new ObjectMapper();
        try {
            jedis.set(goods.getGoodsName(),om.writeValueAsString(goods));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }
}
