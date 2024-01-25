package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.bo.*;
import com.jx.domain.dto.*;
import com.jx.domain.entity.*;
import com.jx.domain.vo.*;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.*;
import com.jx.service.*;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.PageUtils;
import com.jx.utils.RedisCache;
import com.jx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

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
    ActivityAssignmentService activityAssignmentService;
    @Autowired
    PostMapper postMapper;
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
    /**
     * 获取活动列表
     * 返回活动名、活动主题、活动时间
     * @return
     */
    @Override
    public ResponseResult getActivityList(String status,Long locationId,Long categoryId,String activityName,Long pageNum,Long pageSize) {
        //根据status、location、department、category查询活动信息
        //其中department为用户的department 当仅当该部门成员或会长团可以访问该活动
        User user = SecurityUtils.getLoginUser().getUser();
        List<Activity>activityList = activityMapper.getActivityList(status,locationId,user.getDepartmentId(),categoryId,activityName);
        //封装
        List<ListActivityVo>activityVos = BeanCopyUtils.copyBeanList(activityList, ListActivityVo.class);
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
    public ResponseResult addSchedule(Scheduled scheduled) {
        Long userId = SecurityUtils.getUserId();
        scheduled.setUserId(userId);
        //TODO 判断该活动是否还招募
        //TODO 判断该班次是否还招募
        //TODO 该用户角色和部门是否符合条件
        //判断在同一时间段是否报班
        Long cnt = activityAssignmentMapper.getUserIsInThisTimeSlot(userId,scheduled.getPostAssignmentId());
        if(cnt>0L){
            throw new SystemException(AppHttpCodeEnum.JUST_ONE_POST);
        }
        if(doSecKill(scheduled.getUserId(),scheduled.getPostAssignmentId())){
            scheduledService.save(scheduled);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getActivityDetail(Long activityId) {
        //根据活动id获取活动的基本信息
        ActivityDetailVo activityDetailVo = activityMapper.getActivityDetails(activityId);
        //获取活动的举办地点
        activityDetailVo.setLocations(activityMapper.getActivityLocations(activityId));
        //活动排班信息为列表 时间+地点+班次列表为元素
        //获取活动的排班信息中的时间和地点
        List<ActivityAssignmentsBo> schedule = activityAssignmentMapper.getActivityAssignmentsVoList(activityId);
        //根据时间和地点获取班次列表
        if(schedule.size()>0) {
            schedule.stream().forEach(
                    o -> {
                        List<ClassBo> classBos = new ArrayList<>();
                        activityAssignmentMapper.getTimeSlotVoList(activityId,o.getTypeId(),o.getLocationId(), o.getTime()).stream().forEach(
                                e -> {
                                    //根据时间段id、时间、地点获取岗位列表
                                    User user = SecurityUtils.getLoginUser().getUser();
                                    List<PostNeedBo> postNeedBoList = postMapper.getPostNeedVoList(activityId,o.getTypeId(),e.getId(), o.getLocationId(), o.getTime(),null,user.getDepartmentId(),user.getRoleId());
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
                                    ClassBo classBo = new ClassBo(e, postNeedBoList);
                                    if (!Objects.isNull(classBo)) classBos.add(classBo);
                                }
                        );
                        o.setAssignmentVoList(classBos);
                    }
            );
        }
        activityDetailVo.setScheduled(schedule);

        //班次岗位列表以排班岗位+所需人数为元素
        return ResponseResult.okResult(activityDetailVo);
    }


    @Override
    public ResponseResult userActivityList(Long pageNum, Long pageSize) {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Scheduled> scheduledLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //获取用户参与的排班
        scheduledLambdaQueryWrapper.eq(Scheduled::getUserId,userId);
        List<Scheduled> scheduled = scheduledService.list(scheduledLambdaQueryWrapper);
        List<UserActivityVo> userActivityVos = new ArrayList<>();
        for (Scheduled s:scheduled) {
            String postName = postMapper.getPostByActivityAssignmentId(s.getPostAssignmentId());
            //封装活动信息
            //根据用户所报岗位信息 查询班次id
            PostAssignment postAssignment = postAssignmentService.getById(s.getPostAssignmentId());
            ActivityAssignmentVo activityAssignmentVoList = activityAssignmentMapper.getActivityAssignmentVo(postAssignment.getActivityAssignmentId());
            UserActivityVo userActivityVo = new UserActivityVo(activityAssignmentVoList,postName,null,null);
            //封装leader信息
            if(!postName.equals(SystemConstants.IS_LEADER)){
                UserInfoVo leaderInfo = userMapper.getLeaderInfo(s.getPostAssignmentId());
                userActivityVo.setLeaderInfo(leaderInfo);
            }
            userActivityVos.add(userActivityVo);
        }
        Page<UserActivityVo> pageUserActivityVos = PageUtils.listToPage(userActivityVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(pageUserActivityVos.getRecords(),pageUserActivityVos.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult userAsLeaderActivityList(Long pageNum, Long pageSize,String type) {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Scheduled> scheduledLambdaQueryWrapper = new LambdaQueryWrapper<>();
        scheduledLambdaQueryWrapper.eq(Scheduled::getUserId,userId);
        List<Scheduled> scheduled = scheduledService.list(scheduledLambdaQueryWrapper);
        List<UserActivityVo> userActivityVos = new ArrayList<>();
        for (Scheduled s:scheduled) {
            String postName = postMapper.getPostByActivityAssignmentId(s.getPostAssignmentId());
            //封装活动信息
            if(postName.equals(SystemConstants.IS_LEADER)) {
                PostAssignment postAssignment = postAssignmentService.getById(s.getPostAssignmentId());
                ActivityAssignmentVo activityAssignmentVoList = activityAssignmentMapper.getActivityAssignmentVo(postAssignment.getActivityAssignmentId());
                UserActivityVo userActivityVo = new UserActivityVo(activityAssignmentVoList, null, null, null);
                //封装volunteer信息
                if (type.equals("1")) {
                    //获取当前排班的信息
                    ActivityAssignment activityAssignment = activityAssignmentService.getById(postAssignment.getActivityAssignmentId());
                    //查询本班次的岗位信息
                    List<PostNeedBo> postNeedBoList = postMapper.getPostNeedVoList(activityAssignmentVoList.getActivityId(),
                            activityAssignment.getTypeId(),activityAssignment.getTimeSlotId(), activityAssignment.getLocationId(),
                            activityAssignment.getTime(), SystemConstants.IS_LEADER,0L,0L);
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
                    userActivityVo.setVolunteersInfo(postNeedBoList);
                }
                userActivityVos.add(userActivityVo);
            }
        }
        Page<UserActivityVo> pageUserActivityVos = PageUtils.listToPage(userActivityVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(pageUserActivityVos.getRecords(),pageUserActivityVos.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult userVolunteerInfo(Long pageNum, Long pageSize) {
        Long userId = SecurityUtils.getUserId();
        //获取用户参加的活动id列表
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
                Integer reqVolunteerNum =postAssignment.getPeopleNumber().intValue();
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
                    System.err.println(activityAssignmentId);
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
    public ResponseResult getActivityAssignmentDetail(Long id) {
        ActivityAssignment activityAssignment = activityAssignmentService.getById(id);
        ActivityAssignmentDetailVo activityAssignmentVo = BeanCopyUtils.copyBean(activityAssignment,ActivityAssignmentDetailVo.class);
        LambdaQueryWrapper<PostAssignment> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PostAssignment::getActivityAssignmentId,id);
        List<PostAssignmentVo> postAssignmentVos = BeanCopyUtils.copyBeanList(postAssignmentService.list(lambdaQueryWrapper),PostAssignmentVo.class);
        postAssignmentVos.stream().forEach(o->{
            o.setPostName(postService.getById(o.getPostId()).getName());
            o.setReqPeople(postAssignmentService.getById(o.getId()).getPeopleNumber());
        });
        activityAssignmentVo.setPostList(postAssignmentVos);
        return ResponseResult.okResult(activityAssignmentVo);
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
    public boolean doSecKill(Long userId, Long postAssignmentId)  {
        //非空判断
        if (Objects.isNull(userId) || Objects.isNull(postAssignmentId)) {
            throw new SystemException(AppHttpCodeEnum.PARAM_NOT_NULL);
        }

        //3 拼接key
        // 3.1 库存key
        String kcKey = "sk:" + postAssignmentId + ":qt";
        // 3.2 秒杀成功用户key
        String userKey = "sk:" + postAssignmentId + ":user";

        //4 获取库存，如果库存null，秒杀还没有开始
        if (Objects.isNull(redisCache.getCacheObject(kcKey))) {
            throw new SystemException(AppHttpCodeEnum.KILL_NOT_START);
        }
        int kc = redisCache.getCacheObject(kcKey) ;
        // 5 判断用户是否重复秒杀操作
        if (redisCache.isMember(userKey,String.valueOf(userId))) {
            throw new SystemException(AppHttpCodeEnum.REQUEST_AGAIN);
        }

        //6 判断如果商品数量，库存数量小于1，秒杀结束
        if (kc <= 0) {
            throw new SystemException(AppHttpCodeEnum.KILL_PASS);
        }

        //7 秒杀过程
        //7.1 库存-1
        redisCache.decrementCount(kcKey,1L);
        //7.2 把秒杀成功用户添加清单里面
        redisCache.addMember(userKey,String.valueOf(userId));
        System.out.println("秒杀成功了..");
        return true;
    }
}
