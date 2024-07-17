package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/chatSession")
public class ChatSessionController {
    @Autowired
    ChatSessionService chatSessionService;
    /**
     * 获取该用户的会话列表
     * @return
     */
    @GetMapping
    @SystemLog(businessName = "获取该用户的会话列表",type="2")
    ResponseResult getUserSessionList(){
        return chatSessionService.getUserSessionList();
    }
}
