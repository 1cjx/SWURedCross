package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.ChatSession;


/**
 * (ChatSession)表服务接口
 *
 * @author makejava
 * @since 2024-07-14 19:32:24
 */
public interface ChatSessionService extends IService<ChatSession> {

    ResponseResult getUserSessionList();
}

