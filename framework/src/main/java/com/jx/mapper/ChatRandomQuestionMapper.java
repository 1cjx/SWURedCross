package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.ChatRandomQuestion;

import java.util.List;


/**
 * (ChatRandomQuestion)表数据库访问层
 *
 * @author makejava
 * @since 2024-07-14 23:53:14
 */
public interface ChatRandomQuestionMapper extends BaseMapper<ChatRandomQuestion> {

    List<ChatRandomQuestion> getRandomQuestion();
}

