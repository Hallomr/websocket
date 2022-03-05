package com.example.websocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.websocket.util.RedisUtil;
import com.example.websocket.websocket.WebsocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@RestController
@Slf4j
public class MessageContorller {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Autowired
    //private WebSocketServer webSocketServer;

    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping(value = "/pushToWeb")
    public void pushToWeb() throws InterruptedException {
        //模拟定时任务触发数据推送实时数据
        for(int i = 0;i<=10;i++) {
            Thread.sleep(1000);
            //webSocketServer.sendInfo("web", "mywebsocket");
            WebsocketMessage test = new WebsocketMessage().setChannel("test").setMsgField1("message" + i);
            String s = JSONObject.toJSONString(test);
            jedisCluster.publish("test",s);
            /*Jedis jedis = RedisUtil.getJedis();
            jedis.publish("test",s);
            RedisUtil.close(jedis);
            */
            log.info(s);
        }
    }
}
