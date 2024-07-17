package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.ChatSession;
import com.jx.domain.vo.ChatSessionVo;
import com.jx.mapper.ChatSessionMapper;
import com.jx.service.ChatSessionService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (ChatSession)表服务实现类
 *
 * @author makejava
 * @since 2024-07-14 19:32:25
 */
@Service("chatSessionService")
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements ChatSessionService {

    @Override
    public ResponseResult getUserSessionList() {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatSession::getUserId,userId);
        wrapper.orderByDesc(ChatSession::getCreateTime);
        List<ChatSessionVo> chatSessionList = BeanCopyUtils.copyBeanList(list(wrapper), ChatSessionVo.class);
        return  ResponseResult.okResult(chatSessionList);
    }
}
