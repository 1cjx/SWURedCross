package com.jx.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDto {
    @ExcelProperty(index=0)
    private String departmentName;
    @ExcelProperty(index=1)
    private String roleName;
    @ExcelProperty(index=2)
    private  Long id;
    @ExcelProperty(index=3)
    private String name;
    @ExcelProperty(index=4)
    private String sex;
    @ExcelProperty(index=5)
    private String collegeName;
    @ExcelProperty(index=6)
    private  String phoneNumber;

    @ExcelProperty(index=7)
    private String qq;
    @ExcelProperty(index=8)
    private String email;

    private String password;
    private String type;
    private String avatar;
    private String status;
    private Long roleId;
    private Long departmentId;
    private Long collegeId;
}
