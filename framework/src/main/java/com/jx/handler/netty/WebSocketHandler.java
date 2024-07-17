package com.jx.handler.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jx.domain.bean.NettyGroup;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.bean.WebSocketGroup;
import com.jx.listener.XFWebSocketListener;
import com.jx.service.AiChatService;
import com.jx.utils.SecurityUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;


@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private AiChatService aiChatService;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded被调用,{}", JSON.toJSONString(ctx));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务器收到消息：{}", msg.text());
        // 获取用户ID,关联channel
        JSONObject jsonObject = JSON.parseObject(msg.text());
        String sessionId = jsonObject.getString("sessionId");
        String channelId = UUID.randomUUID().toString().replaceAll("-", "");
        // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取channelID
        AttributeKey<String> key = AttributeKey.valueOf("channelId");
        ctx.channel().attr(key).setIfAbsent(channelId);
        NettyGroup.getUserChannelMap().put(channelId, ctx.channel());
        //接收消息格式{"uid":"123456","text":"中华人民共和国成立时间"}
        String text = jsonObject.getString("text");
        String systemRole = jsonObject.getString("systemRole");
        //请求大模型服务器，获取结果
        aiChatService.pushMessageToXFServer(channelId,sessionId, text,systemRole);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved被调用,{}", JSON.toJSONString(ctx));
        // 获取channelId
        String channelId = (String) ctx.channel().attr(AttributeKey.valueOf("channelId")).get();
        // 获取userId
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        // 删除通道
        NettyGroup.getUserChannelMap().remove(channelId);
        // 获取websocketListener
        XFWebSocketListener webSocketListener = WebSocketGroup.getWebSocketListenerMap().get(channelId);
        // 有回答，可能是回答完了也可能是回答了一半，进行保存
        if(StringUtils.hasText(webSocketListener.getAnswer())){
            webSocketListener.saveQuestionAndAnswer(userId);
        }
        // 关闭后端与大模型的websocket
        WebSocket webSocket = WebSocketGroup.getWebsocketMap().get(channelId);
        webSocket.cancel();
        //删除已绑定的websocket与websocketListener
        WebSocketGroup.getWebSocketListenerMap().remove(channelId);
        WebSocketGroup.getWebsocketMap().remove(channelId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("通道异常：{}", cause.getMessage());
        String channelId = (String) ctx.channel().attr(AttributeKey.valueOf("channelId")).get();
        NettyGroup.getUserChannelMap().remove(channelId);
    }
}


