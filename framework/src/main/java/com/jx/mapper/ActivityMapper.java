package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.bo.ActivityAssignmentInfoBo;
import com.jx.domain.entity.Activity;
import com.jx.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Activity)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-02 17:20:59
 */
public interface ActivityMapper extends BaseMapper<Activity> {
    /**
     * 查询该志愿活动举办的场地名集合
     * @param activityId
     * @return
     */
    List<ListLocationVo> getActivityLocations(Long activityId);

    /**
     * 查询当前用户参与的志愿活动
     * type为 1 就只查询用户作为班次负责人的活动
     * @param userId
     * @return
     */
    List<ActivityVo> getUserActivity(@Param("userId") Long userId, @Param("postName") String postName);

    /**
     * 根据状态、地点id、分类id、用户所在部门id获取活动列表
     * @param status
     * @param locationId
     * @param departmentId
     * @param categoryId
     * @return
     */
    List<ListActivityVo> getActivityList(@Param("status") String status, @Param("locationId") Long locationId,
                                         @Param("departmentId")Long departmentId,
                                         @Param("categoryId") Long categoryId,
                                         @Param("activityName")String activityName);
    /**
     * 根据活动id获取活动详情信息
     * @param activityId
     * @return
     */
    ActivityDetailVo getActivityDetails(Long activityId);

    /**
     * 获取各类活动参与次数
     * @return
     */
    List<ActivityCategoryHoldVo> getVariousActivitiesNum();

    List<ActivityAssignmentInfoBo> getActivityAssignmentInfoBo(@Param("activityId") Long id);
}

