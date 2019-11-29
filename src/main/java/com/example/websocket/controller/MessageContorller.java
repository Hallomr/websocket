package com.example.websocket.controller;

import com.example.websocket.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageContorller {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebSocketServer webSocketServer;

    @RequestMapping(value = "/pushToWeb")
    public void pushToWeb() throws InterruptedException {
        //模拟定时任务触发数据推送实时数据
        for(int i = 0;i<=100;i++) {
            Thread.sleep(1000);
            webSocketServer.sendInfo("web", "mywebsocket");
        }
    }
}
