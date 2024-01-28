package com.jx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
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
    private String userName;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date signInTime;
}
