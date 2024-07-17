package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.service.DashboardService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "主页仪表盘相关接口")
@RequestMapping("/system/dashboard")
public class                           DashboardController {
    @Autowired
    DashboardService dashboardService;

    /**
     * 获取已注册志愿者数量
     * @return
     */
    @SystemLog(businessName = "查询已注册志愿者数量",type="2")
    @GetMapping("/getVolunteerNums")
    ResponseResult getVolunteerNums(){
        return dashboardService.getVolunteerNums();
    }

    /**
     * 获取总志愿时长
     * @return
     */
    @SystemLog(businessName = "查询总志愿服务时长",type="2")
    @GetMapping("/getTotalVolunteerTimes")
    ResponseResult getTotalVolunteerTimes(){
        return dashboardService.getTotalVolunteerTimes();
    }

    /**
     * 获取活动数
     * @return
     */
    @SystemLog(businessName = "查询已开展活动数",type="2")
    @GetMapping("/getActivityNums")
    ResponseResult getActivityNums(){
        return dashboardService.getActivityNums();
    }
    /**
     * 根据传参对活动积极性进行排序 默认全体部门按志愿时长降序排列
     * # sortType=1按照参与活动数进行排序，否则按照活动时间排序
     */
    @SystemLog(businessName = "查询活动参与排行榜",type="2")
    @GetMapping("/getRankingByActivity")
    ResponseResult getRankingByActivity(Long departmentId,Long sortType){
        return dashboardService.getRankingByActivity(departmentId,sortType);
    }

    /**
     * 获取各类活动参与次数
     * @return
     */
    @SystemLog(businessName = "查询各类活动开展次数",type="2")
    @GetMapping("/getVariousActivitiesNum")
    ResponseResult getVariousActivitiesNum(){
        return dashboardService.getVariousActivitiesNum();
    }
}
