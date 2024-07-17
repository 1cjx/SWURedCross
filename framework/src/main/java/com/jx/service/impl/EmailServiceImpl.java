package com.jx.service.impl;

import com.jx.constants.SystemConstants;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.entity.Scheduled;
import com.jx.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.util.UUID;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //发送消息 生产者
    @Override
    public ResponseResult sendEmail(Scheduled scheduled) {
        String  msgId = UUID.randomUUID().toString();
        CorrelationData cd = new CorrelationData(msgId);
        //给future添加confirmCallBack
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("消息回调失败",throwable);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                log.info("收到ConfirmCallback回执");
                if(confirm.isAck()){
                    log.info("消息发送成功,收到ack");
                    cd.getId();
                }
                else{
                    log.error("消息发送失败,收到nack,原因:{}",confirm.getReason());
                }
            }
        });
        rabbitTemplate.convertAndSend(SystemConstants.MAIL_EXCHANGE_NAME, SystemConstants.MAIL_ROUTING_KEY_NAME, scheduled, cd);// 发送消息
        return ResponseResult.okResult();
    }
}
