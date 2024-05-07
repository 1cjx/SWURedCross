package com.jx.service;


import com.jx.domain.ResponseResult;
import com.jx.domain.entity.Email;
import com.jx.domain.entity.Scheduled;

public interface EmailService {
    ResponseResult sendEmail(Scheduled scheduled);
}
