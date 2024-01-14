package com.jx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserVo {
    private  Long id;
    private String name;
    private  String phoneNumber;
    private String qq;
    private String email;
    private String type;
    private String sex;
    private String status;
    private String department;
    private String college;
    private String role;
}
