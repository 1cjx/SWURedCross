package com.jx.service;



public interface AiChatService {


    void pushMessageToXFServer(String channelId,String sessionId,String text,String systemText);
}
