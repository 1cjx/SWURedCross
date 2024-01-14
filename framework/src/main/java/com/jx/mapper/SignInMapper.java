package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.SignIn;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * (SignIn)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-19 11:01:48
 */
public interface SignInMapper extends BaseMapper<SignIn> {

    List<Date> getSignInTimeList(@Param("assignmentId") Long assignmentId, @Param("userId") Long userId);
}

