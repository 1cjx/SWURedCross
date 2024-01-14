package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostAssignmentVo {
    private Long id;
    private Long postId;
    private String postName;
    private Long reqPeople;
    private Long allowedDepartmentId;
    private Long allowedRoleId;
}
