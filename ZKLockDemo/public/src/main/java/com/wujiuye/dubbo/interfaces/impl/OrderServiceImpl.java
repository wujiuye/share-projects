package com.wujiuye.dubbo.interfaces.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wujiuye.dubbo.db.RedisUtils;
import com.wujiuye.dubbo.interfaces.OrderService;
import com.wujiuye.dubbo.pojo.Goods;
import com.wujiuye.dubbo.pojo.Order;
import com.wujiuye.dubbo.zklock.ZKClient;
import com.wujiuye.dubbo.zklock.ZKLock;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Date;

public class OrderServiceImpl implements OrderService {

    //因为连接很耗时，所以需要一个全局的连接
    private ZKClient zkClient = new ZKClient("zk01.wujiuye.com:2181,zk02.wujiuye.com:2182,zk03.wujiuye.com:2183");

    private RedisUtils redisUtils = new RedisUtils();

    /**
     * 严格来说，这里需要加入分布式事务，否则消费端调用超时了，结果提供方的代码还是执行了的，
     * 这就会产生问题了
     *
     * @param goodsName
     * @param number
     * @return
     */
    @Override
    public Order placeOrder(String goodsName, int number) {
        ZKLock zkLock = new ZKLock(zkClient);
        zkLock.lock();
        Jedis jedis = redisUtils.getConn();
        try {
            String goodsString = jedis.get(goodsName);
            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode jsonNode = om.readTree(goodsString);
                Goods goods = new Goods();
                goods.setCount(jsonNode.get("count").asInt());
                goods.setGoodsId(jsonNode.get("goodsId").asText());
                goods.setGoodsName(jsonNode.get("goodsName").asText());
                goods.setGoodsType(jsonNode.get("goodsType").asText());
                goods.setPirce(jsonNode.get("pirce").asInt());

                //修改总数
                if (goods.getCount() == 0) return null;
                if (goods.getCount() < number) return null;
                goods.setCount(goods.getCount() - number);
                System.err.println("[[[[[[[[[商品剩余总数：" + goods.getCount() + "]]]]]]]]]]");
                jedis.set(goodsName,om.writeValueAsString(goods));

                Order order = new Order();
                order.setDate(new Date());
                order.setGoods(goods);
                order.setUser(null);
                order.setOrderId("order-" + goods.getCount() + "-" + System.currentTimeMillis());

                return order;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        } finally {
            zkLock.unlock();
            jedis.close();
        }
    }

}
