package com.jx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.ChatRandomQuestion;
import com.jx.mapper.ChatRandomQuestionMapper;
import com.jx.service.ChatRandomQuestionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (ChatRandomQuestion)表服务实现类
 *
 * @author makejava
 * @since 2024-07-14 23:53:15
 */
@Service("chatRandomQuestionService")
public class ChatRandomQuestionServiceImpl extends ServiceImpl<ChatRandomQuestionMapper, ChatRandomQuestion> implements ChatRandomQuestionService {

    @Autowired
    ChatRandomQuestionMapper chatRandomQuestionMapper;
    @Override
    public ResponseResult getRandomQuestion() {
        List<ChatRandomQuestion> chatRandomQuestionList = chatRandomQuestionMapper.getRandomQuestion();
        return ResponseResult.okResult(chatRandomQuestionList);
    }
}
