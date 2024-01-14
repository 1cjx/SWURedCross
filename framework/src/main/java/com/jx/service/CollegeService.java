package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.entity.College;


/**
 * (College)表服务接口
 *
 * @author makejava
 * @since 2023-10-26 21:17:18
 */
public interface CollegeService extends IService<College> {

    ResponseResult selectColleges(String name);

    ResponseResult listAllCollege();
}

