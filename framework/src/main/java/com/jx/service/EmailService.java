package com.jx.service;


import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.Scheduled;

public interface EmailService {
    ResponseResult sendEmail(Scheduled scheduled);
}
