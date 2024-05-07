package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.entity.Role;
import com.jx.domain.entity.Title;
import com.jx.domain.vo.RoleVo;
import com.jx.domain.vo.TitleVo;
import com.jx.mapper.TitleMapper;
import com.jx.service.TitleService;
import com.jx.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Title)表服务实现类
 *
 * @author makejava
 * @since 2024-02-01 17:13:19
 */
@Service("titleService")
public class TitleServiceImpl extends ServiceImpl<TitleMapper, Title> implements TitleService {

    @Override
    public ResponseResult listAllTitle() {
        LambdaQueryWrapper<Title> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Title::getStatus, SystemConstants.STATUS_NORMAL);
        List<TitleVo> titles = BeanCopyUtils.copyBeanList(list(wrapper),TitleVo.class);
        return ResponseResult.okResult(titles);
    }
}
