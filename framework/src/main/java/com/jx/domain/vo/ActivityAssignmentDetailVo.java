package com.jx.domain.vo;

import com.jx.domain.bo.PostNeedBo;
import com.jx.domain.dto.AddPostAssignmentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityAssignmentDetailVo {
    private Long id;
    private Date time;
    private Long locationId;
    private Long timeSlotId;
    private Long typeId;
    private List<PostAssignmentVo> postList;
}
