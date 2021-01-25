package com.example.websocket.config;

import com.alibaba.fastjson.JSONObject;
import com.example.websocket.websocket.WebSocketServer;
import com.example.websocket.websocket.WebsocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;
/** 
* @Description:消息监听器
* @Param:  
* @return:  
* @Author: zxk 
* @Date: 2021/1/21 
*/
@Component
@Slf4j
public class JedisListener extends JedisPubSub {
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("channel:{},subscribedChannels:{}",channel,subscribedChannels);
    }

    @Override
    public void onMessage(String channel, String message) {
        // 此处可以接收到生产者推送的信息,并通过websocket进行相关的操作
        log.info("channel:{},message:{}",channel,message);
        if(channel.equals("test")){
            WebsocketMessage websocketMessage = JSONObject.parseObject(message, WebsocketMessage.class);
            WebSocketServer.redisSendMsg(websocketMessage);
        }
    }

}
