package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.bean.RoleContent;
import com.jx.domain.entity.ChatRoleContent;
import com.jx.domain.entity.ChatSession;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.ChatRoleContentMapper;
import com.jx.service.ChatRoleContentService;
import com.jx.service.ChatSessionService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.SecurityUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * (ChatRoleContent)表服务实现类
 *
 * @author makejava
 * @since 2024-07-14 19:30:36
 */
@Service("chatRoleContentService")
public class ChatRoleContentServiceImpl extends ServiceImpl<ChatRoleContentMapper, ChatRoleContent> implements ChatRoleContentService {

    @Autowired
    ChatSessionService chatSessionService;
    @Override
    public ResponseResult getSessionHistoryQA(Long sessionId) {
        if(Objects.isNull(sessionId)){
            throw  new SystemException(AppHttpCodeEnum.PARAM_NOT_NULL);
        }
        // 判断一下该session是否存在
        ChatSession chatSession = chatSessionService.getById(sessionId);
        if(Objects.isNull(chatSession)){
            throw new SystemException(AppHttpCodeEnum.SESSION_NOT_FOUND);
        }
        // 查询一下这个session是不是这个user的
        if(!chatSession.getUserId().equals(SecurityUtils.getUserId())){
            throw new SystemException(AppHttpCodeEnum.SESSION_USER_ERROR);
        }
        //查询当前会话的历史id
        LambdaQueryWrapper<ChatRoleContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatRoleContent::getSessionId,sessionId);
        List<ChatRoleContent> chatRoleContents = list(wrapper);
        // 只返回role和content
        List<RoleContent> roleContentList = BeanCopyUtils.copyBeanList(chatRoleContents, RoleContent.class);
        return ResponseResult.okResult(roleContentList);
    }
}
