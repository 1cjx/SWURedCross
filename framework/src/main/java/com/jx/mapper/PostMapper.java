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
     * 根据活动id,时间段id、时间、地点获取所需岗位列表
     * @param timeSlotId
     * @param locationId
     * @param time
     * @param ignorePostName
     * @return
     */
    List<PostNeedBo> getPostNeedVoList(@Param("activityId")Long activityId, @Param("typeId")Long typeId,
                                       @Param("timeSlotId") Long timeSlotId, @Param("locationId")
    Long locationId, @Param("time") Date time, @Param("ignorePostName")String ignorePostName,
                                       @Param("userDepartmentId") Long userDepartmentId, @Param("userRoleId") Long userRoleId);

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

