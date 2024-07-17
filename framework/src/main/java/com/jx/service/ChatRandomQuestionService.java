package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.ChatRandomQuestion;


/**
 * (ChatRandomQuestion)表服务接口
 *
 * @author makejava
 * @since 2024-07-14 23:53:15
 */
public interface ChatRandomQuestionService extends IService<ChatRandomQuestion> {

    ResponseResult getRandomQuestion();

}

