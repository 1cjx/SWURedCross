package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUserDto {
    //用户名
    private Long id;
    //用户姓名
    private String name;
    //code
    private String code;
}
