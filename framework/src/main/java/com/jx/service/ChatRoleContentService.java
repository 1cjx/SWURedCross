package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.ChatRoleContent;


/**
 * (ChatRoleContent)表服务接口
 *
 * @author makejava
 * @since 2024-07-14 19:30:36
 */
public interface ChatRoleContentService extends IService<ChatRoleContent> {

    ResponseResult getSessionHistoryQA(Long sessionId);
}

