package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ActivityVo {
    //活动id
    private Long id;
    //活动名
    private String name;
    private String theme;
    private Date beginDate;
    private Date endDate;

}
