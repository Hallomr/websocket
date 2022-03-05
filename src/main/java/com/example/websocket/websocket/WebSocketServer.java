package com.example.websocket.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

//@ServerEndpoint("/websocket/{user}")
@ServerEndpoint("/websocket")
@Component
@Data
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    //订阅业务
    private static ConcurrentHashMap<String,WebSocketServer> webServiceMap = new ConcurrentHashMap<>();
    //当前业务通道名
    private static String channel;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        //在线数加1
        addOnlineCount();
        log.info("有新连接加入！当前在线数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (Exception e) {
            log.error("websocket IO异常");
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线数为" + getOnlineCount());
        if(!Strings.isEmpty(channel)){
            webServiceMap.remove(channel);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        //获取消息
        //WebsocketMessage websocketMessage = JSONObject.parseObject(message, WebsocketMessage.class);
        //插入业务 可根据不同channel 做各类业务消息推送
        //webServiceSet.add(websocketMessage.getChannel());
        webServiceMap.put("test",this);
        try {
            //可在此处推送初始化数据
            this.sendMessage("初始化数据");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public  void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 向指定业务推送自定义消息
     */
    public  void sendInfo(String channel, String message) {
        log.info(message);
        WebSocketServer webSocketServer = webServiceMap.get(channel);
        webSocketServer.sendMessage(message);
    }

    public static void redisSendMsg(WebsocketMessage msg){
        WebSocketServer webSocketServer = webServiceMap.get(msg.getChannel());
        webSocketServer.sendMessage(JSONObject.toJSONString(msg));
        log.info("message:{}",JSONObject.toJSONString(msg));
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
