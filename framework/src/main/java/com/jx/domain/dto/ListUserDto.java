package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String qq;
    private String isBind;
    private String status;
    private Long collegeId;
    private Long departmentId;
    private Long titleId;
}
