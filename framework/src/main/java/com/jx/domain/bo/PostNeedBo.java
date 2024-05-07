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
public class PostNeedBo {
    private Long id;
    //岗位名
    private String postName;
    private Long postId;
    private String reqPeople;
    private Long nowPeople;
    private Long allowedDepartmentId;
    private Long allowedTitleId;
    private boolean isChoose;
    private List<VolunteerInfoBo> volunteerInfoBoList;
}
