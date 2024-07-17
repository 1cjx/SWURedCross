package com.jx.domain.bean;

import com.jx.listener.XFWebSocketListener;
import okhttp3.WebSocket;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于记录websocket相关组
 */
public class WebSocketGroup {
    public WebSocketGroup(){

    }
    /**
     * 用来记录不同线程生成的websocketListener
     */
    public static ConcurrentHashMap<String, XFWebSocketListener> webSocketListenerMap = new ConcurrentHashMap<>();
    /**
     * 用来记录不同线程生成的websocket
     */
    public static ConcurrentHashMap<String, WebSocket> websocketMap = new ConcurrentHashMap<>();
    /**
     * 获取websocketMap,可以根据channelId查询websocket
     */
    public static ConcurrentHashMap<String, WebSocket> getWebsocketMap(){
        return websocketMap;
    }

    /**
     * 获取webSocketListenerMap,可以根据channelId查询websocketListener
     */
    public static ConcurrentHashMap<String, XFWebSocketListener> getWebSocketListenerMap(){
        return webSocketListenerMap;
    }
}
