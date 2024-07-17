package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.bo.*;
import com.jx.domain.dto.*;
import com.jx.domain.entity.*;
import com.jx.domain.vo.*;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.*;
import com.jx.service.*;
import com.jx.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (Activity)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:21:00
 */
@Service("activityService")
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    ActivityService activityService;
    @Autowired
    ScheduledService scheduledService;
    @Autowired
    ActivityAssignmentMapper activityAssignmentMapper;
    @Autowired
    ScheduledMapper scheduledMapper;
    @Autowired
    ActivityAssignmentService activityAssignmentService;
    @Autowired
    PostMapper postMapper;
    @Autowired
    SignInUserMapper signInUserMapper;
    @Autowired
    PostService postService;
    @Autowired
    LocationMapper locationMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    VolunteerRecordMapper volunteerRecordMapper;
    @Autowired
    RedisCache redisCache;
    @Autowired
    PostAssignmentService postAssignmentService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PostAssignmentMapper postAssignmentMapper;
    @Autowired
    SignInService signInService;
    @Autowired
    SignInMapper signInMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取活动列表
     * 返回活动名、活动主题、活动时间
     * @return
     */
    @Override
    public ResponseResult getActivityList(Long pageNum,Long pageSize,ListActivityDto listActivityDto) {
        //根据status、location、department、category查询活动信息
        //其中department为用户的department 当仅当该部门成员或会长团可以访问该活动
        User user = SecurityUtils.getLoginUser().getUser();
        List<ListActivityVo>activityVos = activityMapper.getActivityList(listActivityDto.getStatus(),listActivityDto.getLocationId(),user.getDepartmentId(),listActivityDto.getCategoryId(),listActivityDto.getName());
        //查询活动开展的地点
        activityVos.stream().forEach(
                o->
                       o.setLocations(BeanCopyUtils.copyBeanList(locationMapper.getActivityLocations(o.getId())
                                , ListLocationVo.class)));
        //分页
        Page<ListActivityVo> listActivityVoPage = PageUtils.listToPage(activityVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(listActivityVoPage.getRecords(),listActivityVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    public void judgeSchedule(AddScheduledDto addScheduledDto){
        //判断活动是否还存在
        Activity activity = activityService.getById(addScheduledDto.getActivityId());
        if(Objects.isNull(activity)){
            throw new SystemException(AppHttpCodeEnum.ACTIVITY_NOT_EXITS);
        }
        //判断该活动是否还招募
        if(activity.getStatus().equals(SystemConstants.STATUS_ERROR)){
            throw new SystemException(AppHttpCodeEnum.ACTIVITY_NOT_RECRUIT);
        }
        ActivityAssignment activityAssignment = activityAssignmentService.getById(addScheduledDto.getActivityAssignmentId());
        //判断班次是否存在
        if(Objects.isNull(activityAssignment)){
            throw new SystemException(AppHttpCodeEnum.ACTIVITY_ASSIGNMENT_NOT_EXITS);
        }
        //判断该班次是否还招募
        if(activityAssignment.getStatus().equals(SystemConstants.STATUS_ERROR)){
            throw new SystemException(AppHttpCodeEnum.ACTIVITY_ASSIGNMENT_NOT_RECRUIT);
        }
    }
    @Override
    @Transactional
    public ResponseResult addSchedule(AddScheduledDto addScheduledDto) {
        User user = SecurityUtils.getLoginUser().getUser();
        Long userId = user.getId();
        addScheduledDto.setUserId(userId);
        //判断活动和班次
        judgeSchedule(addScheduledDto);
        //该用户角色和部门是否符合条件
        PostAssignment postAssignment = postAssignmentService.getById(addScheduledDto.getPostAssignmentId());
        Long postAllowedTitleId = postAssignment.getAllowedTitleId();
        Long postDepartmentId = postAssignment.getAllowedDepartmentId();
        // 判断角色
        if(!postAllowedTitleId.equals(0L)&&!postAllowedTitleId.equals(user.getTitleId())){
            throw new SystemException(AppHttpCodeEnum.USER_PERMISSION_NOT_ENOUGH);
        }
        //判断部门
        if(!postDepartmentId.equals(0L)&&!postDepartmentId.equals(user.getDepartmentId())){
            throw new SystemException(AppHttpCodeEnum.USER_PERMISSION_NOT_ENOUGH);
        }
        //判断在同一时间段是否报班
        Long cnt = activityAssignmentMapper.getUserIsInThisTimeSlot(userId,addScheduledDto.getPostAssignmentId());
        if(cnt>0L){
            throw new SystemException(AppHttpCodeEnum.JUST_ONE_POST);
        }
        Scheduled scheduled = BeanCopyUtils.copyBean(addScheduledDto,Scheduled.class);
        if(Objects.isNull(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //报名
        if(doSecKill(addScheduledDto.getUserId(),addScheduledDto.getPostAssignmentId())){
            scheduledService.save(scheduled);
            //报名成功 发送邮件
            emailService.sendEmail(scheduled);
        }
        return ResponseResult.okResult();
    }
    @Override
    @Transactional
    public ResponseResult cancelSchedule(AddScheduledDto addScheduledDto) {
        judgeSchedule(addScheduledDto);
        Long userId = SecurityUtils.getUserId();
        Long postAssignmentId = addScheduledDto.getPostAssignmentId();
        //限制每个月只能退选两次
        Integer num = redisCache.getCacheObject("cancelSchedule:"+userId);
        if(Objects.isNull(num)){
            redisCache.setCacheObject("cancelSchedule:"+userId,2, TimeUtils.getSecondToNextMonth(), TimeUnit.SECONDS);
        }
        else if(num<=0){
            throw new SystemException(AppHttpCodeEnum.THIS_MONTH_CANCEL_NUMBER_RUN_OUT);
        }
        // 库存key
        String kcKey = "sk:" + postAssignmentId + ":qt";
        // 秒杀成功用户key
        String userKey = "sk:" + postAssignmentId + ":user";
        //取消次数减一
        redisCache.decrementCount("cancelSchedule:" + userId, 1L);
        // 库存+1
        redisCache.addCount(kcKey, 1L);
        // 用户从清单里面删除
        redisCache.deleteMember(userKey, String.valueOf(userId));
        //从数据库中删除信息
        Scheduled scheduled = BeanCopyUtils.copyBean(addScheduledDto, Scheduled.class);
        scheduled.setUserId(userId);
        scheduledMapper.remove(scheduled);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getChatActivityList(Long pageNum, Long pageSize, ListActivityDto listActivityDto) {
        List<ListActivityVo>activityVos = activityMapper.getActivityList(listActivityDto.getStatus(),listActivityDto.getLocationId(),null,listActivityDto.getCategoryId(),listActivityDto.getName());
        //查询活动开展的地点
        activityVos.stream().forEach(
                o->
                        o.setLocations(BeanCopyUtils.copyBeanList(locationMapper.getActivityLocations(o.getId())
                                , ListLocationVo.class)));
        //分页
        Page<ListActivityVo> listActivityVoPage = PageUtils.listToPage(activityVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(listActivityVoPage.getRecords(),listActivityVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }


    @Override
    public ResponseResult getActivityDetail(Long activityId) {
        //根据活动id获取活动的基本信息
        ActivityDetailVo activityDetailVo = activityMapper.getActivityDetails(activityId);
        //获取当前用户信息
        User user = SecurityUtils.getLoginUser().getUser();
        //这个活动当前是否允许查看
        // 1.判断状态是否是发布状态
        if(activityDetailVo.getStatus().equals(SystemConstants.STATUS_ERROR)) {
            throw new SystemException(AppHttpCodeEnum.ACTIVITY_NOT_EXITS);
        }
        // 2.判断允许部门是否包括 用户所在部门
        Long activityAllowedDepartmentId = activityDetailVo.getAllowedDepartmentId();
        if(!activityAllowedDepartmentId.equals(user.getDepartmentId())&&!activityAllowedDepartmentId.equals(0L)&&!activityAllowedDepartmentId.equals(-1L)) {
            throw new SystemException(AppHttpCodeEnum.ACTIVITY_NOT_EXITS);
        }
        //获取活动的举办地点
        activityDetailVo.setLocations(activityMapper.getActivityLocations(activityId));
        //活动排班信息为列表 日期+地点+班次列表为元素
        //获取活动的排班信息中的日期和地点
        List<ActivityAssignmentsBo> schedule = activityAssignmentMapper.getActivityAssignmentsVoList(activityId);
        //根据日期、地点、班次类型获取班次列表
        if(!schedule.isEmpty()) {
            schedule.stream().forEach(
                    o -> {
                        List<ClassBo> classBos = activityAssignmentMapper.getTimeSlotVoList(activityId,o.getTypeId(),o.getLocationId(), o.getTime());
                        classBos.stream().forEach(
                                e -> {
                                    //根据时间段id、日期、地点获取岗位列表
                                    List<PostNeedBo> postNeedBoList = postMapper.getPostNeedVoList(e.getActivityAssignmentId(),null,user.getDepartmentId(),user.getTitleId());
                                    postNeedBoList.stream().forEach(
                                            k -> {
                                                String needPeople = k.getReqPeople();
                                                LambdaQueryWrapper<Scheduled> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                                                lambdaQueryWrapper.eq(Scheduled::getPostAssignmentId, k.getId());
                                                String nowPeople = String.valueOf(scheduledService.count(lambdaQueryWrapper));
                                                String reqPeople = nowPeople + "/" + needPeople;
                                                k.setReqPeople(reqPeople);
                                                Long userId = user.getId();
                                                lambdaQueryWrapper.eq(Scheduled::getUserId,userId);
                                                if(scheduledService.count(lambdaQueryWrapper)>0){
                                                    k.setChoose(true);
                                                }
                                                else{
                                                    k.setChoose(false);
                                                }
                                            }
                                    );
                                    e.setPostNeedBoList(postNeedBoList);
                                }
                        );
                        o.setAssignmentVoList(classBos);
                    }
            );
        }
        //进行一个过滤处理,筛选掉为空的信息
        schedule = schedule.stream().map(o->{
            o.setAssignmentVoList(o.getAssignmentVoList().stream().filter(e->!e.getPostNeedBoList().isEmpty()).collect(Collectors.toList()));
            return o;
        }).filter(o->!o.getAssignmentVoList().isEmpty()).collect(Collectors.toList());
        activityDetailVo.setScheduled(schedule);

        //班次岗位列表以排班岗位+所需人数为元素
        return ResponseResult.okResult(activityDetailVo);
    }


    @Override
    public ResponseResult userActivityList(Long pageNum, Long pageSize,Boolean type) {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        String postName = type?SystemConstants.IS_LEADER:null;
        List<ActivityVo> activityVos =  activityMapper.getUserActivity(userId,postName);
        Page<ActivityVo> pageUserActivityVos = PageUtils.listToPage(activityVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(pageUserActivityVos.getRecords(),pageUserActivityVos.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult userActivityDetail(Long activityId,Boolean type) {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //获取用户在当前活动参与的排班
        List<Scheduled> scheduled = scheduledMapper.getUserInThisActivitySchedules(activityId,userId);
        List<UserActivityAssignmentVo> userActivityAssignmentVos = new ArrayList<>();
        for (Scheduled s:scheduled) {
            String postName = postMapper.getPostByActivityAssignmentId(s.getPostAssignmentId());
            PostAssignment postAssignment = postAssignmentService.getById(s.getPostAssignmentId());
            ActivityAssignmentVo activityAssignmentVoList = activityAssignmentMapper.getActivityAssignmentVo(postAssignment.getActivityAssignmentId());
            if(type) {
                if(postName.equals(SystemConstants.IS_LEADER)) {
                    UserActivityAssignmentVo userActivityAssignmentVo = new UserActivityAssignmentVo(activityAssignmentVoList, null, null, null, null);
                    //封装volunteer信息
                    //获取当前排班的信息
                    ActivityAssignment activityAssignment = activityAssignmentService.getById(postAssignment.getActivityAssignmentId());
                    //查询本班次的岗位信息
                    List<PostNeedBo> postNeedBoList = postMapper.getPostNeedVoList(postAssignment.getActivityAssignmentId(), SystemConstants.IS_LEADER, 0L, 0L);
                    //查询本班次各个岗位志愿者信息
                    postNeedBoList.stream().forEach(o -> {
                                o.setVolunteerInfoBoList(userMapper.getVolunteerInfo(o.getId()));
                                //封装人数信息
                                LambdaQueryWrapper<Scheduled> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                                lambdaQueryWrapper.eq(Scheduled::getPostAssignmentId, o.getId());
                                String nowPeopleNum = String.valueOf(scheduledService.count(lambdaQueryWrapper));
                                String requestPeopleNum = nowPeopleNum + "/" + o.getReqPeople();
                                o.setReqPeople(requestPeopleNum);
                            }
                    );
                    //将志愿者信息封装
                    userActivityAssignmentVo.setVolunteersInfo(postNeedBoList);
                    userActivityAssignmentVos.add(userActivityAssignmentVo);
                }
            }
            else{
                    //封装活动信息
                    //根据用户所报岗位信息 查询班次id
                UserActivityAssignmentVo userActivityAssignmentVo = new UserActivityAssignmentVo(activityAssignmentVoList, s.getCreateTime(), postName, null, null);
                    //封装leader信息
                    if(!postName.equals(SystemConstants.IS_LEADER)){
                        UserInfoVo leaderInfo = userMapper.getLeaderInfo(s.getPostAssignmentId());
                        userActivityAssignmentVo.setLeaderInfo(leaderInfo);
                    }
                userActivityAssignmentVos.add(userActivityAssignmentVo);
            }
        }
        Activity activity = getById(activityId);
        ActivityVo activityVo = BeanCopyUtils.copyBean(activity,ActivityVo.class);
        UserActivityVo userActivityVo = new UserActivityVo(userActivityAssignmentVos,activityVo);
        return ResponseResult.okResult(userActivityVo);
    }


    @Override
    public ResponseResult userVolunteerInfo(Long pageNum, Long pageSize) {
        Long userId = SecurityUtils.getUserId();
        //获取用户已完成签到的活动列表
        List<VolunteerRecordVo> volunteerRecordVos =volunteerRecordMapper.getUserVolunteerActivityInfo(userId);
        //封装信息
        volunteerRecordVos.stream().forEach(o->{
            //获取活动相关信息
            Activity activity = activityService.getById(o.getActivityId());
            //封装活动简略信息
            ActivityVo activityVo = BeanCopyUtils.copyBean(activity,ActivityVo.class);
            o.setActivityVo(activityVo);
            //封装该活动用户参与的总时长
            Double activityTotalTime = volunteerRecordMapper.getUserVolunteerActivityTotalTime(userId,o.getActivityId());
            if(Objects.isNull(activityTotalTime)){
                o.setTotalVolunteerTime(0.0);
            }
            else{
                o.setTotalVolunteerTime(activityTotalTime);
            }
            //获取用户参与的班次id列表
            List<VolunteerRecordBo> volunteerRecordBoList = volunteerRecordMapper.getUserVolunteerAssignmentInfo(userId,o.getActivityId());
            volunteerRecordBoList.stream().forEach(e->{
                //封装班次志愿者的工作总结
                VolunteerRecord volunteerRecord = volunteerRecordMapper.getUserVolunteerInfo(userId,e.getActivityAssignmentId());
                e.setReason(volunteerRecord.getReason());
                e.setStatus(volunteerRecord.getStatus());
                e.setVolunteerTime(volunteerRecord.getVolunteerTime());
                //封装班次信息
               e.setActivityAssignmentVo(activityAssignmentMapper.getActivityAssignmentVo(e.getActivityAssignmentId()));
            });
            o.setVolunteerRecordBoList(volunteerRecordBoList);
        });
        Page<VolunteerRecordVo> volunteerRecordVoPage = PageUtils.listToPage(volunteerRecordVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(volunteerRecordVoPage.getRecords(),volunteerRecordVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult listActivity(Long pageSize, Long pageNum, ListActivityDto listActivityDto) {
        LambdaQueryWrapper<Activity> activityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        activityLambdaQueryWrapper.like(StringUtils.hasText(listActivityDto.getName()),Activity::getName,listActivityDto.getName());
        activityLambdaQueryWrapper.eq(StringUtils.hasText(listActivityDto.getStatus()),Activity::getStatus,listActivityDto.getStatus());
        activityLambdaQueryWrapper.eq(!Objects.isNull(listActivityDto.getCategoryId()),Activity::getCategoryId,listActivityDto.getCategoryId());
        activityLambdaQueryWrapper.orderByDesc(Activity::getBeginDate);
        List<Activity> activityList = list(activityLambdaQueryWrapper);
        List<ListActivityBo> activityVos = BeanCopyUtils.copyBeanList(activityList,ListActivityBo.class);
        activityVos.stream().forEach(o->{
            Category category = categoryService.getById(o.getCategoryId());
            if(!Objects.isNull(category)) {
                o.setCategory(category.getName());
            }
            else{
                o.setCategory("暂无");
            }
        });
        Page<ListActivityBo> activityVoPage = PageUtils.listToPage(activityVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(activityVoPage.getRecords(),activityVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getActivityDetailForAdmin(Long id) {
        //根据活动id获取活动的基本信息
        Activity activity = activityService.getById(id);
        ActivityDetailForAdminVo activityDetailForAdminVo = BeanCopyUtils.copyBean(activity,ActivityDetailForAdminVo.class);
        List<ActivityAssignmentInfoBo> activityAssignmentInfoBos = activityMapper.getActivityAssignmentInfoBo(id);
        activityAssignmentInfoBos.stream().forEach(o->{
            List<PostNeedBo> postNeedBoList = postMapper.getPostNeedVoListByActivityAssignmentId(o.getId());
            postNeedBoList.stream().forEach(e->{
                e.setVolunteerInfoBoList(userMapper.getVolunteerInfo(e.getId()));
                //封装人数信息
                LambdaQueryWrapper<Scheduled> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Scheduled::getPostAssignmentId, e.getId());
                Long nowPeopleNum = (long)(scheduledService.count(lambdaQueryWrapper));
                e.setNowPeople(nowPeopleNum);
            });
            o.setPostList(postNeedBoList);
        });
        activityDetailForAdminVo.setActivityAssignmentList(activityAssignmentInfoBos);
        return ResponseResult.okResult(activityDetailForAdminVo);
    }


    @Override
    @Transactional
    public ResponseResult releaseActivity(AddActivityDto addActivityDto) {
        Activity activity = BeanCopyUtils.copyBean(addActivityDto,Activity.class);
        //插入新活动 返回活动id
        activityMapper.insert(activity);
        Long activityId = activity.getId();
        List<AddActivityAssignmentDto> activityAssignmentDetailVos = addActivityDto.getActivityAssignmentList();
        activityAssignmentDetailVos.stream().forEach(o->{
            ActivityAssignment activityAssignment = BeanCopyUtils.copyBean(o,ActivityAssignment.class);
            activityAssignment.setActivityId(activityId);
            //插入新班次 返回班次id
            activityAssignmentMapper.insert(activityAssignment);
            Long activityAssignmentId = activityAssignment.getId();
            List<AddPostAssignmentDto> postAssignmentDtoList = o.getPostList();
            postAssignmentDtoList.stream().forEach(e->{
                PostAssignment postAssignment = BeanCopyUtils.copyBean(e,PostAssignment.class);
                postAssignment.setPeopleNumber(e.getReqPeople());
                postAssignment.setActivityAssignmentId(activityAssignmentId);
                // 插入新岗位需求 返回岗位需求id
                postAssignmentMapper.insert(postAssignment);
                Long postAssignmentId = postAssignment.getId();
                Integer reqVolunteerNum = postAssignment.getPeopleNumber().intValue();
                // redis创建报班申请
                String key = "sk:" + postAssignmentId +":qt";
                redisCache.setCacheObject(key,reqVolunteerNum);
            });
        });
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateActivity(UpdateActivityDto updateActivityDto) {
        Activity activity = BeanCopyUtils.copyBean(updateActivityDto,Activity.class);
        //根据活动id修改活动信息
        Long activityId = activity.getId();
        updateById(activity);
        List<UpdateActivityAssignmentDto> updateActivityAssignmentDtoList =  updateActivityDto.getActivityAssignmentList();
        updateActivityAssignmentDtoList.stream().forEach(o->{
            //根据班次id修改班次信息 如果id不存在 则说明是新增的班次
            ActivityAssignment activityAssignment = BeanCopyUtils.copyBean(o,ActivityAssignment.class);
            Long activityAssignmentId = activityAssignment.getId();
            if(!Objects.isNull(activityAssignmentId)){
                if(activityAssignmentId<0L){//为负数说明删除
                    //删除与之相关的信息
                    activityAssignmentId = -activityAssignmentId;
                    deletePostAssignment(activityAssignmentId);
                    activityAssignmentService.removeById(activityAssignmentId);
                }
                else{
                    activityAssignmentService.updateById(activityAssignment);
                }
            }
            else{
                activityAssignment.setActivityId(activityId);
                activityAssignmentMapper.insert(activityAssignment);
            }
            List<UpdatePostAssignmentDto> updatePostAssignmentDtoList = o.getPostList();
            updatePostAssignmentDtoList.stream().forEach(e->{
                //根据岗位信息id修改岗位需求 如果id不存在 则说明是新增的岗位信息
                PostAssignment newPostAssignment = BeanCopyUtils.copyBean(e,PostAssignment.class);
                newPostAssignment.setPeopleNumber(e.getReqPeople());
                Long postAssignmentId = newPostAssignment.getId();
                if(!Objects.isNull(postAssignmentId)){
                    if(postAssignmentId<0L){
                        // 删除该岗位志愿者
                        deleteVolunteer(-postAssignmentId);
                        //删除该岗位信息
                        postAssignmentService.removeById(-postAssignmentId);
                    }
                    else {
                        PostAssignment oldPostAssignment = postAssignmentService.getById(postAssignmentId);
                        String key = "sk:" + postAssignmentId + ":qt";
                        // 修改前人数小于修改后人数
                        if (oldPostAssignment.getPeopleNumber().longValue() > newPostAssignment.getPeopleNumber().longValue()) {
                            LambdaQueryWrapper<Scheduled> scheduledLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            scheduledLambdaQueryWrapper.eq(Scheduled::getPostAssignmentId, postAssignmentId);
                            long cnt = scheduledService.count(scheduledLambdaQueryWrapper);
                            // 如果小于就抛出异常
                            if (cnt > newPostAssignment.getPeopleNumber().longValue()) {
                                throw new SystemException(AppHttpCodeEnum.PEOPLE_NUM_ERROR);
                            }
                            // 否则就减小redis中的库存
                            redisCache.decrementCount(key, oldPostAssignment.getPeopleNumber().longValue() - newPostAssignment.getPeopleNumber().longValue());
                        } //修改后人数增加
                        else {
                            //增加redis中的库存
                            redisCache.addCount(key, newPostAssignment.getPeopleNumber().longValue() - oldPostAssignment.getPeopleNumber().longValue());
                        }
                        // 修改岗位信息
                        postAssignmentService.updateById(newPostAssignment);
                    }
                }// 新增岗位信息
                else{
                    newPostAssignment.setActivityAssignmentId(activityAssignment.getId());
                    postAssignmentMapper.insert(newPostAssignment);
                    String key = "sk:"+newPostAssignment.getId()+":qt";
                    redisCache.setCacheObject(key,newPostAssignment.getPeopleNumber().intValue());
                }
            });
        });
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult deleteActivity(Long id) {
        //删除班次信息
        deleteActivityAssignment(id);
        //删除活动信息
        removeById(id);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult changeActivityStatus(ChangeActivityStatusDto changeActivityStatusDto) {
        Activity activity = getById(changeActivityStatusDto.getActivityId());
        activity.setStatus(changeActivityStatusDto.getStatus());
        updateById(activity);
        return ResponseResult.okResult();
    }



    public void deleteActivityAssignment(Long activityId){
        // 查询当前活动的班次
        LambdaQueryWrapper<ActivityAssignment> activityAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        activityAssignmentLambdaQueryWrapper.eq(ActivityAssignment::getActivityId,activityId);
        List<ActivityAssignment> activityAssignments = activityAssignmentService.list(activityAssignmentLambdaQueryWrapper);
        activityAssignments.stream().forEach(o-> {
            //删除岗位信息
            deletePostAssignment(o.getId());
            signInMapper.removeByAssignmentId(o.getId());
            signInUserMapper.removeByAssignmentId(o.getId());
            volunteerRecordMapper.removeByAssignmentId(o.getId());
        });
        //删除该活动班次信息
        activityAssignmentService.remove(activityAssignmentLambdaQueryWrapper);
    }
    public void deletePostAssignment(Long activityAssignmentId){
        //查询该班次对应的岗位信息
        LambdaQueryWrapper<PostAssignment> postAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postAssignmentLambdaQueryWrapper.eq(PostAssignment::getActivityAssignmentId,activityAssignmentId);
        List<PostAssignment> postAssignments = postAssignmentService.list(postAssignmentLambdaQueryWrapper);
        postAssignments.stream().forEach(e->{
            //删除招募信息
            redisCache.deleteObject("sk:"+e.getId()+":qt");
            redisCache.deleteObject("sk:"+e.getId()+":user");
            //删除志愿者信息
            deleteVolunteer(e.getId());
        });
        // 删除该班次的岗位信息
        postAssignmentService.remove(postAssignmentLambdaQueryWrapper);
    }
    public void deleteVolunteer(Long postAssignmentId){
        //查询该岗位信息填报的志愿者
        LambdaQueryWrapper<Scheduled> scheduledLambdaQueryWrapper = new LambdaQueryWrapper<>();
        scheduledLambdaQueryWrapper.eq(Scheduled::getPostAssignmentId,postAssignmentId);
        // 删除该岗位志愿者报名信息
        scheduledService.remove(scheduledLambdaQueryWrapper);
        //删除redis中的数据
        String key = "sk:"+postAssignmentId;
        redisCache.deleteObject(key+":qt");
        redisCache.deleteObject(key+":user");
    }

    static String secKillScript =
            "local tempUserId=KEYS[1];\r\n" +
            "local userId = '\"' .. tempUserId .. '\"';\r\n" +

                    "local prodId=KEYS[2];\r\n" +
                    "local qtKey='sk:'..prodId..\":qt\";\r\n" +
                    "local usersKey='sk:'..prodId..\":user\"\r\n" +
                    "local userExists=redis.call(\"sismember\",usersKey,userId);\r\n" +
                    "if tonumber(userExists)==1 then \r\n" +
                    "   return 2;\r\n" +
                    "end\r\n" +
                    "local num= redis.call(\"get\" ,qtKey);\r\n" +
                    "if tonumber(num)<=0 then \r\n" +
                    "   return 0;\r\n" +
                    "else \r\n" +
                    "   redis.call(\"decr\",qtKey);\r\n" +
                    "   redis.call(\"sadd\",usersKey,userId);\r\n" +
                    "end\r\n" +
                    "return 1" ;
    public boolean doSecKill(Long userId, Long postAssignmentId)  {
        //非空判断
        if (Objects.isNull(userId) || Objects.isNull(postAssignmentId)) {
            throw new SystemException(AppHttpCodeEnum.PARAM_NOT_NULL);
        }
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(secKillScript);
        redisScript.setResultType(Long.class);
        String result = redisTemplate.execute(redisScript, Arrays.asList(userId.toString(), postAssignmentId.toString())).toString();
        if ("0".equals( result )  ) {
            throw new SystemException(AppHttpCodeEnum.KILL_PASS);
        }else if("1".equals( result )  )  {
            return true;
        }else if("2".equals( result )  )  {
            throw new SystemException(AppHttpCodeEnum.REQUEST_AGAIN);
        }else {
            throw new SystemException(AppHttpCodeEnum.KILL_NOT_START);
        }
    }
}
