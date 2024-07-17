package com.jx.listener;

import com.alibaba.fastjson.JSON;
import com.jx.domain.bean.JsonParse;
import com.jx.domain.bean.NettyGroup;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.bean.RoleContent;
import com.jx.domain.entity.ChatRoleContent;
import com.jx.domain.entity.ChatSession;
import com.jx.mapper.ChatRoleContentMapper;
import com.jx.mapper.ChatSessionMapper;
import com.jx.service.ChatRoleContentService;
import com.jx.utils.SecurityUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.LongValue;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@Scope("prototype")
public class XFWebSocketListener extends WebSocketListener {
    @Autowired
    ChatRoleContentService chatRoleContentService;
    @Autowired
    ChatSessionMapper chatSessionMapper;
    //断开websocket标志位
    private boolean wsCloseFlag = false;
    //通道id
    private String channelId;
    private String sessionId;
    private String question;
    private StringBuilder answer = new StringBuilder();
    public void setChannelId(String channelId){
        this.channelId = channelId;
    }
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
    public void setQuestion(String question){
        this.question = question;
    }
    //语句组装buffer，将大模型返回结果全部接收，在组装成一句话返回
    public String getQuestion(){
        return this.question;
    }
    public StringBuilder getAnswer(){
        return answer;
    }

    public boolean isWsCloseFlag() {
        return wsCloseFlag;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("大模型服务器连接成功！");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        JsonParse myJsonParse = JSON.parseObject(text, JsonParse.class);
        if (myJsonParse.getHeader().getCode() != 0) {
            log.error("发生错误，错误信息为:{}", JSON.toJSONString(myJsonParse.getHeader()));
            // 关闭连接标识
            wsCloseFlag = true;
            return;
        }
        List<RoleContent> textList = myJsonParse.getPayload().getChoices().getText();
        for (RoleContent temp : textList) {
            pushToOne(channelId,temp.getContent());
            this.answer.append(temp.getContent());
        }
        if (myJsonParse.getHeader().getStatus() == 2) {
            wsCloseFlag = true;
            pushToOne(channelId,"[END]");
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                log.error("onFailure body:{}", response.body().string());
                if (101 != code) {
                    log.error("讯飞星火大模型连接异常");
                }
            }
        } catch (IOException e) {
            log.error("IO异常：{}", e);
        }
    }
    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("大模型连接关闭");
    }
    public static void pushToOne(String uid, String text) {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(text)) {
            log.error("uid或text均不能为空");
            throw new RuntimeException("uid或text均不能为空");
        }
        ConcurrentHashMap<String, Channel> userChannelMap = NettyGroup.getUserChannelMap();
        for (String channelId : userChannelMap.keySet()) {
            if (channelId.equals(uid)) {
                Channel channel = userChannelMap.get(channelId);
                if (channel != null) {
                    channel.eventLoop().execute(() -> {
                        ResponseResult success = ResponseResult.okResult(text);
                        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(success)));
                    });
                } else {
                    log.error("该id对于channelId不存在！");
                }
                return;
            }
        }
        log.error("该用户不存在！");
    }


    public void saveQuestionAndAnswer(String userId) {
        if(!StringUtils.hasText(sessionId)) {
            String name = question.length() > 20 ? question.substring(0, 20) : question;
            ChatSession chatSession = new ChatSession(null,Long.valueOf(userId),name,new Date());
            chatSessionMapper.insert(chatSession);
            sessionId = String.valueOf(chatSession.getId());
        }
        List<ChatRoleContent> chatRoleContents = new ArrayList<>();
        ChatRoleContent userRoleContent = new ChatRoleContent(null,"user",question, Long.valueOf(sessionId));
        ChatRoleContent assistantRoleContent = new ChatRoleContent(null,"assistant",answer.toString(),Long.valueOf(sessionId));
        chatRoleContents.add(userRoleContent);
        chatRoleContents.add(assistantRoleContent);
        chatRoleContentService.saveBatch(chatRoleContents);
    }
}
