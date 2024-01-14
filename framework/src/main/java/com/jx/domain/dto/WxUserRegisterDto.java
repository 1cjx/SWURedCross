package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUserRegisterDto {
    private  Long id;
    private String name;
    private  String phoneNumber;
    private String qq;
    private String email;
    private String sex;
    private String code;
}
