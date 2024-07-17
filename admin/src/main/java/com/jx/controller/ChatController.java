package com.jx.controller;
import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.service.ActivityService;
import com.jx.service.ChatRandomQuestionService;
import com.jx.service.ChatRoleContentService;
import com.jx.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (ChatRandomQuestion)表控制层
 *
 * @author makejava
 * @since 2024-07-14 23:53:13
 */
@RestController
@RequestMapping("/sys/chat")
public class ChatController{
    @Autowired
    ChatRandomQuestionService chatRandomQuestionService;

    @Autowired
    ChatRoleContentService chatRoleContentService;

    @Autowired
    ActivityService activityService;

    /**
     * 新聊天获取四个随机问题
     * @return
     */
    @GetMapping("/getRandomQuestion")
    @SystemLog(businessName = "新聊天获取四个随机问题",type="2")
    ResponseResult getRandomQuestion(){
       return chatRandomQuestionService.getRandomQuestion();
    }

    /**
     * 获取当前会话的历史问题
     * @param sessionId 会话id
     * @return
     */
    @GetMapping("/getSessionHistoryQA/{sessionId}")
    @SystemLog(businessName = "获取当前会话的历史问题",type="2")
    ResponseResult getSessionHistoryQA(@PathVariable("sessionId")Long sessionId){
        return chatRoleContentService.getSessionHistoryQA(sessionId);
    }
}

