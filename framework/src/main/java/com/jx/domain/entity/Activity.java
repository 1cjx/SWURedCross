package com.jx.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Activity)表实体类
 *
 * @author makejava
 * @since 2023-09-02 23:48:22
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_activity")
public class Activity  {
    //活动id
    @TableId
    private Long id;

    //活动名
    private String name;
    //活动主题
    private String theme;
    //活动分类id
    private Long categoryId;
    //0为不可报名1为可报名
    private String status;
    //活动开始日期
   
    private Date beginDate;
    //允许参与活动的部门 0为允许所有参与
    private Long allowedDepartmentId;
    //活动结束日期
   
    private Date endDate;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}

