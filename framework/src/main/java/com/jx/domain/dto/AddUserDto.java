package com.jx.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelIgnoreUnannotated //忽略掉不加@ExcelProperty的属性
public class AddUserDto {
    @ExcelProperty(index=0)
    private String departmentName;
    @ExcelProperty(index=1)
    private String titleName;
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
    private Long titleId;
    private Long departmentId;
    private Long collegeId;
}
