package com.jx.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (College)表实体类
 *
 * @author makejava
 * @since 2023-10-26 21:17:17
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_college")
public class College  {
    //学院id@TableId
    private Long id;

    //学院名
    private String name;



}

