package com.jx.domain.bean;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;


public class NettyGroup {


    /**
     * 存放用户与Chanel的对应信息，用于给指定用户发送消息
     */
    private static ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    private NettyGroup() {
    }


    /**
     * 获取连接channel map
     */
    public static ConcurrentHashMap<String, Channel> getUserChannelMap() {
        return userChannelMap;
    }
}
