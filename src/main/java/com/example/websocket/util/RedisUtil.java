package com.example.websocket.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    private static JedisPool jedisPool = null;
   /* static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxWaitMillis(100);
        jedisPoolConfig.setTestOnBorrow(false);//jedis 第一次启动时，会报错
        jedisPoolConfig.setTestOnReturn(true);
        // 初始化JedisPool
        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379,10000,"123456");
    }*/

    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            Jedis resource = getJedisPoolInstance().getResource();
            return resource;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void close(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    private RedisUtil() {
    }

    public static JedisPool getJedisPoolInstance() {
        if (null == jedisPool) {
            synchronized (RedisUtil.class) {
                if (null == jedisPool) {
                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxTotal(1024);
                    jedisPoolConfig.setMaxIdle(100);
                    jedisPoolConfig.setMaxWaitMillis(100);
                    jedisPoolConfig.setTestOnBorrow(false);//jedis 第一次启动时，会报错
                    jedisPoolConfig.setTestOnReturn(true);
                    // 初始化JedisPool
                    jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379,10000,"123456");
                }
            }
        }
        return jedisPool;
    }
}
