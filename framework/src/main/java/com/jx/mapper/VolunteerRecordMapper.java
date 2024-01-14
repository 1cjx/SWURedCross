package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.bo.VolunteerRecordBo;
import com.jx.domain.entity.VolunteerRecord;
import com.jx.domain.vo.UserVolunteerRecordVo;
import com.jx.domain.vo.VolunteerRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (VolunteerRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-05 21:17:11
 */
public interface VolunteerRecordMapper extends BaseMapper<VolunteerRecord> {

    Double getUserTotalVolunteerTimes(@Param("userId") Long userId);

    List<VolunteerRecordVo> getUserVolunteerActivityInfo(@Param("userId") Long userId);

    List<VolunteerRecordBo> getUserVolunteerAssignmentInfo(@Param("userId") Long  userId, @Param("activityId") Long activityId);

    VolunteerRecord getUserVolunteerInfo(@Param("userId") Long userId, @Param("activityAssignmentId") Long activityAssignmentId);

    Double getUserVolunteerActivityTotalTime(@Param("userId") Long userId, @Param("activityId") Long activityId);

    Long getTotalVolunteerTimes();

    List<UserVolunteerRecordVo> getUserVolunteerRecordList(@Param("departmentId") Long departmentId, @Param("sortType") Long sortType);
}

