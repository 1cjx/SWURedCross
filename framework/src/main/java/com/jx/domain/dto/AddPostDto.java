package com.jx.domain.dto;


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
 * (Post)表实体类
 *
 * @author makejava
 * @since 2023-09-02 17:24:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostDto  {
    //岗位id
    private Long id;
    //岗位名
    private String name;
    //岗位描述
    private String description;
    //0为通用
    private Long categoryId;
    private String status;
}

