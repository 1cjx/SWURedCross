package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.bo.ClassBo;
import com.jx.domain.bo.EmailInfoBo;
import com.jx.domain.entity.ActivityAssignment;
import com.jx.domain.vo.ActivityAssignmentVo;
import com.jx.domain.bo.ActivityAssignmentsBo;
import com.jx.domain.bo.TimeSlotBo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * (ActivityAssignment)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-02 17:23:35
 */
public interface ActivityAssignmentMapper extends BaseMapper<ActivityAssignment> {

    /**
     * 根据活动id获取活动排班信息中的时间和地点
     * @param activityId
     * @return
     */
    List<ActivityAssignmentsBo> getActivityAssignmentsVoList(Long activityId);

    /**
     * 根据活动地点与时间查询班次
     * @param locationId
     * @param time
     * @return
     */
    List<ClassBo> getTimeSlotVoList(@Param("activityId")Long activityId, @Param("typeId")Long typeId, @Param("locationId") Long locationId, @Param("time") Date time);

    ActivityAssignmentVo getActivityAssignmentVo(@Param("activityAssignmentId") Long activityAssignmentId);


    Long getUserIsInThisTimeSlot(@Param("userId") Long userId, @Param("postAssignmentId") Long postAssignmentId);

    EmailInfoBo getEmailInfoByPostAssignmentId(@Param("postAssignmentId") Long postAssignmentId);
}

