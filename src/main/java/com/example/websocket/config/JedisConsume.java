package com.example.websocket.config;

import com.example.websocket.util.RedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @Description:  消费者
* @Param:
* @return:
* @Author: zxk
* @Date: 2021/1/21
*/
@Component
public class JedisConsume implements InitializingBean {
    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private JedisListener jedisListener;

    @Override
    public void afterPropertiesSet() throws Exception {
        /*new Thread(()->{
            Jedis jedis = RedisUtil.getJedis();
            jedis.subscribe(jedisListener, "test");
            RedisUtil.close(jedis);
        }).start();*/
        // 利用线程池创建一个消费者
        ExecutorService thread = Executors
                .newSingleThreadExecutor((r) -> new Thread(r, "jedis consumer"));
        thread.submit(new Thread(() -> {
            jedisCluster.subscribe(jedisListener, "test");
        }));
        // 消费者声明之后会一直保持阻塞状态,声明后立刻调用shutdown()方法
        thread.shutdown();
    }
}
