package com.jx.utils;

import com.jx.domain.bo.EmailInfoBo;
import com.jx.domain.entity.ActivityAssignment;
import com.jx.domain.entity.PostAssignment;
import com.jx.domain.entity.Scheduled;
import com.jx.domain.entity.User;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.ActivityAssignmentMapper;
import com.jx.mapper.PostAssignmentMapper;
import com.jx.service.PostAssignmentService;
import com.jx.service.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class FreemarkerUtils{

    @Autowired
    ActivityAssignmentMapper activityAssignmentMapper;
    @Autowired
    UserService userService;
    /**
     * 获取HTML模板
     * @param maps
     * @return
     */
    public String getTemplate(Map<String,Object> maps){
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
        cfg.setClassForTemplateLoading(FreemarkerUtils.class,"/template/freemarker");
        try {
            Template template = cfg.getTemplate("index.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, maps);
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
    }
    public Map<String, Object> getDataModels(Scheduled scheduled){
        Map<String, Object> dataModels = new HashMap<>();
        //志愿者名
        User user = userService.getById(scheduled.getUserId());
        //活动名字
        //班次日期+时间+地点+岗位
        EmailInfoBo emailInfoBo = activityAssignmentMapper.getEmailInfoByPostAssignmentId(scheduled.getPostAssignmentId());
        emailInfoBo.setUserName(user.getName());
        //封装
        dataModels.put("emailInfo",emailInfoBo);
        return dataModels;
    }
}
