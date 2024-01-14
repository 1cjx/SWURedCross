package com.jx.domain.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private  Long id;
    private String name;
    private String sex;
    private  String phoneNumber;

    private String qq;
    private String email;
    private String password;
    private String type;
    private String status;
    private Long roleId;
    private Long departmentId;
    private Long collegeId;
}
