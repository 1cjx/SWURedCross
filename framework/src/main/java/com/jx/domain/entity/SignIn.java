package com.jx.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (SignIn)表实体类
 *
 * @author makejava
 * @since 2023-10-19 11:01:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_sign_in")
public class SignIn  {
    //签到id
    @TableId
    private Long id;

    //对应排班id
    private Long assignmentId;
    //签到结束时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
    private String status;
    private String type;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    private Integer delFlag;



}

