package com.jx.domain.vo;

import com.jx.domain.bo.ActivityAssignmentsBo;
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
public class ActivityDetailVo {
    //活动id
    private Long id;
    //活动名
    private String activityName;
    //活动主题
    private String theme;
    //分类名
    private String categoryName;
    //地点列表
    private List<ListLocationVo> locations;
    //活动开始日期
   
    private Date beginDate;
    //活动结束日期
   
    private Date endDate;
    //活动安排信息
    private List<ActivityAssignmentsBo> scheduled;
}
