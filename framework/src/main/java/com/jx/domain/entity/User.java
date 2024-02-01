package com.jx.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2023-09-02 17:25:35
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User  {
    //学号作为用户唯一标识
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    //密码
    private String password;
    //姓名
    private String name;
    //手机号
    private String phoneNumber;
    //用户性别(0未知,1男,2女)
    private String sex;
    //qq号
    private String qq;
    //头像
    private String avatar;
    //1为可访问后台
    private String type;
    private String isBind;
    //微信openid,绑定微信用户
    private String openid;
    //学院名
    private Long collegeId;
    //所属部门id
    private Long departmentId;
    //职称(0干事,1部长,2会长)
    private Long roleId;
    //注册日期
    private Date registerdate;
    private String need;
    //最后登录日期
    private Date lastlogindate;
    private String email;

    private Long createBy;

    private Date createTime;
    private String status;
    private Long updateBy;
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
}

