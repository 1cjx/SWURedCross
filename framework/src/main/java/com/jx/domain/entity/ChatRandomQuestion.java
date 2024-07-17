package com.jx.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ChatRandomQuestion)表实体类
 *
 * @author makejava
 * @since 2024-07-14 23:53:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_chat_random_question")
public class ChatRandomQuestion  {
    @TableId
    private Long id;

    
    private String content;
    
    private String imgPath;



}

