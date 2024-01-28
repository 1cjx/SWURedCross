package com.jx.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (SigninUser)表实体类
 *
 * @author makejava
 * @since 2023-10-19 11:01:38
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_signin_user")
public class SignInUser  {
    private Long assignmentId;
    private Long userId;
    private Date signInTime;
    private Date midTime;
    private Date signOutTime;
    private Long signInCount;
}

