package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jx.config.XFConfig;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.bean.RoleContent;
import com.jx.domain.bean.WebSocketGroup;
import com.jx.domain.entity.ChatRoleContent;
import com.jx.domain.entity.ChatSession;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.listener.XFWebClient;
import com.jx.listener.XFWebSocketListener;
import com.jx.mapper.ChatSessionMapper;
import com.jx.service.AiChatService;
import com.jx.service.ChatRoleContentService;
import com.jx.service.ChatSessionService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.SecurityUtils;
import kotlin.jvm.internal.Lambda;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {

    @Autowired
    private XFWebClient xfWebClient;
    @Autowired
    ChatRoleContentService chatRoleContentService;

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ChatSessionMapper chatSessionMapper;

    //测试账号只有2个并发，此处只使用一个，若是生产环境允许多个并发，可以采用分布式锁
    @Override
    public synchronized void pushMessageToXFServer(String channelId,String sessionId,String text,String systemRole) {
        ArrayList<RoleContent> questions = new ArrayList<>();
        // 判断本次会话是否为新建会话
        if(StringUtils.hasText(sessionId)){
            // 查询本次会话最近的两次QA 两次Q和两次A对于最近的四条记录
            Long sId =  Long.valueOf(sessionId);
            LambdaQueryWrapper<ChatRoleContent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ChatRoleContent::getSessionId,sId);
            lambdaQueryWrapper.orderByDesc(ChatRoleContent::getId);
            lambdaQueryWrapper.last("limit 4");
            List<ChatRoleContent> chatRoleContentList =  chatRoleContentService.list(lambdaQueryWrapper);
            // 按照历史顺序重新进行排序
            chatRoleContentList.sort(Comparator.comparing(ChatRoleContent::getId));
            questions.addAll(BeanCopyUtils.copyBeanList(chatRoleContentList,RoleContent.class));
        }
        // 如果有设定大模型角色背景,插入问答中
        if(StringUtils.hasText(systemRole)){
            RoleContent systemRoleContent = RoleContent.createSystemRoleContent(systemRole);
            questions.add(systemRoleContent);
        }
        // 插入本次的Q
        RoleContent userRoleContent = RoleContent.createUserRoleContent(text);
        questions.add(userRoleContent);
        // 创建监听websocket的listener
        XFWebSocketListener xfWebSocketListener = applicationContext.getBean(XFWebSocketListener.class);
        xfWebSocketListener.setChannelId(channelId);
        xfWebSocketListener.setSessionId(sessionId);
        xfWebSocketListener.setQuestion(text);
        WebSocket webSocket = xfWebClient.sendMsg(channelId, questions, xfWebSocketListener);
        // 将websocket与websocketListener存起来使用
        WebSocketGroup.getWebsocketMap().put(channelId,webSocket);
        WebSocketGroup.getWebSocketListenerMap().put(channelId,xfWebSocketListener);
        if (webSocket == null) {
            log.error("webSocket连接异常");
            throw new SystemException(AppHttpCodeEnum.LLM_REQUEST_ERROR);
        }
    }
}
