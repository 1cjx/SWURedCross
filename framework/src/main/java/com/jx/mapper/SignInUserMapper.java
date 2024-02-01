package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.dto.ListSignInDto;
import com.jx.domain.entity.SignInUser;
import com.jx.domain.vo.ListSignInUserVo;
import com.jx.domain.vo.SignInUserExportVo;
import com.jx.domain.vo.VolunteerRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (SigninUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-19 11:01:37
 */
public interface SignInUserMapper extends BaseMapper<SignInUser> {

    List<ListSignInUserVo> listSignIns(ListSignInDto listSignInDto);

    SignInUser selectByMppId(@Param("assignmentId") Long assignmentId, @Param("userId") Long userId);

    void updateByMppId(SignInUser newSignInUser);

    List<SignInUserExportVo> exportSignInUser(ListSignInDto listSignInDto);
}

