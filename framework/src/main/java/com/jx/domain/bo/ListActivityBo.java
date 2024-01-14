package com.jx.domain.bo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.jx.domain.vo.ListLocationVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ListActivityBo {
    //活动id
    @TableId
    private Long id;

    //活动名
    private String name;
    //活动主题
    private String theme;
    //活动分类id
    private Long categoryId;
    private String category;
    private String status;
    //活动开始时间
    private Date beginDate;
    private Long allowedDepartmentId;
    //活动结束时间
    private Date endDate;
    //活动举办的地点集合
}
