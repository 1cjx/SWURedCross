package com.jx.domain.bo;

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
public class ActivityAssignmentsBo {
    private Long locationId;
    //排班日期
    private Date time;
    //排班地点名
    private String location;
    //班次列表
    private List<ClassBo> assignmentVoList;
    private Long typeId;
    private String typeName;
}
