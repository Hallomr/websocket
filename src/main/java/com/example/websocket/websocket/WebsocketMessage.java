package com.example.websocket.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebsocketMessage {
    //websocket 消息通道名称
    private String channel;
    //推送消息-测试字段
    private String msgField1;
}
