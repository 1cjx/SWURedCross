package com.jx.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClassBo {
    //时间段
    private TimeSlotBo timeSlotBo;
    //所需岗位列表
    private List<PostNeedBo> postNeedBoList;
}
