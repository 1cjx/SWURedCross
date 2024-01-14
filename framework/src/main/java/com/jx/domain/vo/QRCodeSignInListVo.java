package com.jx.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeSignInListVo {
    private Long signInId;
    private String userName;
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;
}
