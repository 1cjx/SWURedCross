package com.jx.domain.vo;

import com.jx.domain.bo.ActivityAssignmentInfoBo;
import com.jx.domain.bo.ActivityAssignmentsBo;
import com.jx.domain.entity.ActivityAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetailForAdminVo {
    //活动id
    private Long id;
    //活动名
    private String name;
    //活动主题
    private String theme;
    private Long allowedDepartmentId;
    //分类名
    private Long categoryId;
    //地点列表
    //活动开始日期
    private String status;
    private Date beginDate;
    //活动结束日期
    private Date endDate;
    //活动安排信息
    private List<ActivityAssignmentInfoBo> activityAssignmentList;
}
