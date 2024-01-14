package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.User;
import com.jx.domain.vo.UserInfoVo;
import com.jx.domain.bo.VolunteerInfoBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-02 17:25:35
 */
public interface UserMapper extends BaseMapper<User> {

    User getUserByInfo(@Param("id") Long id, @Param("name") String name);
    UserInfoVo getUserInfo(@Param("id") Long id);
    UserInfoVo getLeaderInfo(@Param("postAssignmentId")Long postAssignmentId);
    List<VolunteerInfoBo> getVolunteerInfo(@Param("postAssignmentId")Long postAssignmentId);
}

