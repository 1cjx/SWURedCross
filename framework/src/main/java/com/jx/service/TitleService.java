package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.entity.Title;


/**
 * (Title)表服务接口
 *
 * @author makejava
 * @since 2024-02-01 17:13:19
 */
public interface TitleService extends IService<Title> {

    ResponseResult listAllTitle();
}

