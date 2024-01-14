package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddSignInDto;
import com.jx.domain.dto.AddSignInUserDto;
import com.jx.domain.dto.ListSignInDto;
import com.jx.domain.entity.SignIn;
import com.jx.domain.entity.SignInUser;
import com.jx.domain.entity.VolunteerRecord;
import com.jx.domain.vo.*;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.ActivityAssignmentMapper;
import com.jx.mapper.SignInMapper;
import com.jx.service.*;
import com.jx.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * (SignIn)表服务实现类
 *
 * @author makejava
 * @since 2023-10-19 11:01:48
 */
@Service("signInService")
public class SignInServiceImpl extends ServiceImpl<SignInMapper, SignIn> implements SignInService {

    @Autowired
    SignInService signInService;
    @Autowired
    ActivityAssignmentService activityAssignmentService;
    @Autowired
    VolunteerRecordService volunteerRecordService;
    @Autowired
    ActivityAssignmentMapper activityAssignmentMapper;
    @Autowired
    SignInUserService signInUserService;
    @Autowired
    SignInMapper signInMapper;
    @Autowired
    UserService userService;
    @Override
    public ResponseResult getSignInList(Long pageNum,Long pageSize) {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<SignIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SignIn::getCreateBy,userId);
        List<SignIn> signInList = list(lambdaQueryWrapper);
        List<ActivityAssignmentVo> assignmentVoList = signInList.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(SignIn::getAssignmentId))), ArrayList::new))
                .stream()
                .map(
                            o->activityAssignmentMapper.getActivityAssignmentVo(o.getAssignmentId())).
                collect(Collectors.toList());
        Page<ActivityAssignmentVo> listToPage = PageUtils.listToPage(assignmentVoList,pageNum,pageSize);
        PageVo pageVo = new PageVo(listToPage.getRecords(),listToPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Transactional
    @Override
    public ResponseResult addSignIn(AddSignInDto addSignInDto) {
        if(Objects.isNull(addSignInDto.getAssignmentId())||Objects.isNull(addSignInDto.getSignInBegin())||Objects.isNull(addSignInDto.getSignInEnd())){
            throw new SystemException(AppHttpCodeEnum.PARAM_NOT_NULL);
        }
        //时间判断
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String beginTime = format.format(addSignInDto.getSignInBegin());
        String endTime = format.format(addSignInDto.getSignInEnd());
        TimeUtils.timeJudge(beginTime, endTime);
        //签到数量判断
        LambdaQueryWrapper<SignIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SignIn::getAssignmentId,addSignInDto.getAssignmentId());
        Integer cnt = count(lambdaQueryWrapper);
        if(cnt>=3) {
            throw new SystemException(AppHttpCodeEnum.SIGN_IN_EXCEED_UPPER_LIMIT);
        }
        SignIn newSignIn = BeanCopyUtils.copyBean(addSignInDto,SignIn.class);
        newSignIn.setType(String.valueOf(cnt+1));
        signInMapper.insert(newSignIn);
        // 创建签到后 负责人自己也签到
        //获取签到id
        Long signInId = newSignIn.getId();
        Long userId = SecurityUtils.getUserId();
        SignInUser newSignInUser = new SignInUser();
        newSignInUser.setSignInId(signInId);
        newSignInUser.setUserId(userId);
        signInUserService.save(newSignInUser);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult QRCodeSignIn(AddSignInUserDto addSignInUserDto) {
        SignIn sign = signInService.getById(addSignInUserDto.getSignInId());
        if(sign.getSignInEnd().compareTo(new Date())<0) {
            throw new SystemException(AppHttpCodeEnum.SIGN_IN_TIME_PASS);
        }
        else if(sign.getSignInBegin().compareTo(new Date())>0){
            throw new SystemException(AppHttpCodeEnum.SIGN_NOT_START);
        }
        LambdaQueryWrapper<SignInUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SignInUser::getUserId,addSignInUserDto.getUserId());
        lambdaQueryWrapper.eq(SignInUser::getSignInId,addSignInUserDto.getSignInId());
        SignInUser signInUser = signInUserService.getOne(lambdaQueryWrapper);
        if(!Objects.isNull(signInUser)){
            throw new SystemException(AppHttpCodeEnum.SIGNED_IN);
        }
        SignInUser newSignInUser = new SignInUser();
        newSignInUser.setSignInId(addSignInUserDto.getSignInId());
        newSignInUser.setUserId(addSignInUserDto.getUserId());
        signInUserService.save(newSignInUser);
        //判断是否是签退 计算时长
        if(sign.getType().equals("3")){
            List<Date> dateList = signInMapper.getSignInTimeList(sign.getAssignmentId(), addSignInUserDto.getUserId());
            //查询活动id
            VolunteerRecord volunteerRecord = new VolunteerRecord();
            volunteerRecord.setUserId(addSignInUserDto.getUserId());
            volunteerRecord.setActivityAssignmentId(sign.getAssignmentId());
            if(dateList.size()==3){
                //计算时长
                Long time = dateList.get(2).getTime() - dateList.get(0).getTime();
                time/=1000*60;//转化为分钟
                double hour = (double)time/60;
                BigDecimal b = new BigDecimal(hour);
                //保留两位小数
                double resultHour = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                volunteerRecord.setVolunteerTime(resultHour);
                System.err.println(hour);
            }
            else{
                volunteerRecord.setStatus(SystemConstants.STATUS_ERROR);
                String reason = "签到次数只有"+dateList.size()+"次";
                volunteerRecord.setReason(reason);
            }
            volunteerRecordService.save(volunteerRecord);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getChildrenSign(Long activityAssignmentId) {
        LambdaQueryWrapper<SignIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SignIn::getAssignmentId,activityAssignmentId);
        List<SignIn> signInList = list(lambdaQueryWrapper);
        List<SignInVo> signInVoList = BeanCopyUtils.copyBeanList(signInList,SignInVo.class);
        return ResponseResult.okResult(signInVoList);
    }

    @Override
    public ResponseResult getQRCodeSignInList(Long signInId) {
        LambdaQueryWrapper<SignInUser> signInUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        signInUserLambdaQueryWrapper.eq(SignInUser::getSignInId,signInId);
        //获取最新的前10条数据
        signInUserLambdaQueryWrapper.orderByDesc(SignInUser::getCreateTime);
        List<SignInUser> signInUsers = signInUserService.list(signInUserLambdaQueryWrapper);
        List<QRCodeSignInListVo> qrCodeSignInListVos =
                signInUsers.stream().limit(10)
                        .map(
                                o->new QRCodeSignInListVo(o.getSignInId(),userService.getById(o.getUserId()).getName(),o.getCreateTime()))
                        .collect(Collectors.toList());
        return ResponseResult.okResult(qrCodeSignInListVos);
    }

}
