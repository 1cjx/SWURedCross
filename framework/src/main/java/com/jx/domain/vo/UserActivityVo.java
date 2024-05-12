package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityVo {

    //用户当前活动班次信息
    List<UserActivityAssignmentVo> userActivityVos;
    //当前活动信息
    ActivityVo activityVo;
}
