package com.jx.mq;

import com.alibaba.fastjson.JSONObject;
import com.jx.constants.SystemConstants;
import com.jx.domain.entity.Email;
import com.jx.domain.entity.Scheduled;
import com.jx.domain.entity.User;
import com.jx.service.ScheduledService;
import com.jx.service.UserService;
import com.jx.utils.FreemarkerUtils;
import com.jx.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmailConsumer {
    @Autowired
    ScheduledService scheduledService;
    @Autowired
    UserService userService;
    @Autowired
    FreemarkerUtils freemarkerUtils;
    @Autowired
    MailUtils mailUtils;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SystemConstants.MAIL_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name= SystemConstants.MAIL_EXCHANGE_NAME),
            key = SystemConstants.MAIL_ROUTING_KEY_NAME
    ))
    public void consume(Message message){
        log.info("收到消息: {}", message);
        Scheduled scheduled = JSONObject.parseObject(message.getBody(), Scheduled.class);
        //消费幂等性
        scheduledService.lambdaUpdate()
                .set(Scheduled::getIsSendEmail,SystemConstants.CONSUME_SUCCESS)
                .eq(Scheduled::getUserId,scheduled.getUserId())
                .eq(Scheduled::getPostAssignmentId,scheduled.getPostAssignmentId())
                .eq(Scheduled::getIsSendEmail,SystemConstants.NOT_CONSUME)
                .update();
        //拼接邮件内容
        Email email = new Email();
        //1.设置邮件收件人
        User user = userService.getById(scheduled.getUserId());
        List<String> tos  = new ArrayList<>();
        tos.add(user.getEmail());
        email.setTo(tos);
        //2.获取邮件内容
        Map<String, Object> dataModels = freemarkerUtils.getDataModels(scheduled);
        System.out.println(dataModels);
        String template = freemarkerUtils.getTemplate(dataModels);
        //3.发送模板邮件
        mailUtils.sendHtmlMail(tos,"西南大学红十字会志愿服务活动报名成功通知",template);
    }
}
