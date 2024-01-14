package com.jx.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSignInDto {
    public Long assignmentId;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public Date signInBegin;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public Date signInEnd;
}
