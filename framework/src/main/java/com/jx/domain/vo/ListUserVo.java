package com.jx.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jx.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserVo {
    private Long id;
    //姓名
    private String name;
    //手机号
    private String phoneNumber;
    //用户性别(0未知,1男,2女)
    private String sex;
    //qq号
    private String qq;
    //1为可访问后台
    private String type;
    //学院名
    private Long collegeId;
    //所属部门id
    private Long departmentId;
    //职称(0干事,1部长,2会长)
    private Long titleId;

    private String email;
}
