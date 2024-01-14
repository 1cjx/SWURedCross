package com.jx.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto  {
    //学号作为用户唯一标识
    private Long id;

    //密码
    private String password;
}

