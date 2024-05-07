package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.Post;
import com.jx.domain.bo.PostNeedBo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;


/**
 * (Post)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-02 17:24:34
 */
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 根据班次id获取所需岗位列表
     * @param activityAssignmentId
     * @param ignorePostName
     * @return
     */
    List<PostNeedBo> getPostNeedVoList(@Param("activityAssignmentId") Long activityAssignmentId, @Param("ignorePostName")String ignorePostName,
                                       @Param("userDepartmentId") Long userDepartmentId, @Param("userTitleId") Long userTitleId);

    /**
     * 根据排班id 获取岗位信息
     * @param postAssignmentId
     * @return
     */
    String getPostByActivityAssignmentId(@Param("postAssignmentId")Long postAssignmentId);

    /**
     * 根据班次id 获取岗位信息
     * @param activityAssignmentId
     * @return
     */
    List<PostNeedBo> getPostNeedVoListByActivityAssignmentId(@Param("activityAssignmentId") Long activityAssignmentId);
}

