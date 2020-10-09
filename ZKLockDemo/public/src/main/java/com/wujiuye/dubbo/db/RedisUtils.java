package com.wujiuye.dubbo.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

    private JedisPool jedisPool;

    public RedisUtils() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);
        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
    }

    public Jedis getConn() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource(); // 获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jedis;
    }


    public void closeConn(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }
}
