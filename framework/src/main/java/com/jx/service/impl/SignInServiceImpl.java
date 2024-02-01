package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddSignInDto;
import com.jx.domain.entity.SignIn;
import com.jx.domain.entity.SignInUser;
import com.jx.domain.entity.VolunteerRecord;
import com.jx.domain.vo.*;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.ActivityAssignmentMapper;
import com.jx.mapper.SignInMapper;
import com.jx.mapper.SignInUserMapper;
import com.jx.service.*;
import com.jx.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    RedisCache redisCache;
    @Autowired
    ActivityAssignmentService activityAssignmentService;
    @Autowired
    VolunteerRecordService volunteerRecordService;
    @Autowired
    ActivityAssignmentMapper activityAssignmentMapper;
    @Autowired
    SignInUserMapper signInUserMapper;
    @Autowired
    SignInUserService signInUserService;
    @Autowired
    SignInMapper signInMapper;
    @Autowired
    UserService userService;
    @Override
    public ResponseResult  getSignInList(Long pageNum,Long pageSize) {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //查询由用户创建的签到
        LambdaQueryWrapper<SignIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SignIn::getCreateBy,userId);
        List<SignIn> signInList = list(lambdaQueryWrapper);
        //查询签到对应班次的信息
        List<ActivityAssignmentVo> assignmentVoList = signInList.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(SignIn::getAssignmentId))), ArrayList::new))
                .stream()
                .map(
                            o->activityAssignmentMapper.getActivityAssignmentVo(o.getAssignmentId())).
                collect(Collectors.toList());
        //分页
        Page<ActivityAssignmentVo> listToPage = PageUtils.listToPage(assignmentVoList,pageNum,pageSize);
        PageVo pageVo = new PageVo(listToPage.getRecords(),listToPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Transactional
    @Override
    public ResponseResult addSignIn(AddSignInDto addSignInDto) {
        if(Objects.isNull(addSignInDto.getAssignmentId())||Objects.isNull(addSignInDto.getExpireTime())){
            throw new SystemException(AppHttpCodeEnum.PARAM_NOT_NULL);
        }
        //时间判断
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String nowTime = format.format(new Date());
        String expireTime = format.format(addSignInDto.getExpireTime());
        TimeUtils.timeJudge(nowTime, expireTime);
        //签到类型重复判断
        LambdaQueryWrapper<SignIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SignIn::getAssignmentId,addSignInDto.getAssignmentId());
        lambdaQueryWrapper.eq(SignIn::getType,addSignInDto.getType());
        if(count(lambdaQueryWrapper)>0) {
            throw new SystemException(AppHttpCodeEnum.SIGN_IN_EXIST);
        }
        //创建签退时判断是否先创建过签到
        if(addSignInDto.getType().equals("3")||addSignInDto.getType().equals("2")){
            LambdaQueryWrapper<SignIn> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SignIn::getAssignmentId,addSignInDto.getAssignmentId());
            queryWrapper.eq(SignIn::getType,"1");
            //说明没有创建过签到
            if(count(queryWrapper)==0){
                throw new SystemException(AppHttpCodeEnum.SING_IN_NEED_BEFORE_SIGN_OUT);
            }
        }
        //创建新签到
        SignIn newSignIn = BeanCopyUtils.copyBean(addSignInDto,SignIn.class);
        //插入数据库中进行持久化 返回主键到newSignIn中
        signInMapper.insert(newSignIn);
        //插入redis中并设置过期时间
        Integer timeout = Math.toIntExact(newSignIn.getExpireTime().getTime() - new Date().getTime());
        redisCache.setCacheObject("signIn:"+newSignIn.getId(),newSignIn,timeout, TimeUnit.MILLISECONDS);
        // 创建签到后 负责人自己签到
        QRCodeSignIn(newSignIn.getId());
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult QRCodeSignIn(Long signInId) {
        //从redis中获取数据，获取不到就说明过期了
        SignIn sign = redisCache.getCacheObject("signIn:"+signInId);
        if(Objects.isNull(sign)){
            throw new SystemException(AppHttpCodeEnum.SIGN_IN_TIME_PASS);
        }
        //判断是否重复签到
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //获取班次id
        Long assignmentId = sign.getAssignmentId();
        //插入签到
        //查询一下有没有签到过
        SignInUser newSignInUser = signInUserMapper.selectByMppId(assignmentId,userId);
        boolean flag = false;
        //为空创建 不为空更新
        if(Objects.isNull(newSignInUser)) {
            newSignInUser = new SignInUser();
            newSignInUser.setAssignmentId(assignmentId);
            newSignInUser.setUserId(userId);
            flag = true;
        }
        Long cnt = newSignInUser.getSignInCount();
        //cnt为空则说明没有签到过
        if(Objects.isNull(cnt)){
            newSignInUser.setSignInCount(0L);
        }
        //根据类型修改时间 同时判断一下是不是重复签到
        setTime(sign.getType(),newSignInUser);
        //为空插入，不为空更新
        if(flag){
            signInUserService.save(newSignInUser);
        }
        else{
            signInUserMapper.updateByMppId(newSignInUser);
        }
        //计算时长
        if(sign.getType().equals("3")){
            //判断签到个数是否正确
            Long count = newSignInUser.getSignInCount();
            LambdaQueryWrapper<SignIn> signInLambdaQueryWrapper = new LambdaQueryWrapper<>();
            signInLambdaQueryWrapper.eq(SignIn::getAssignmentId,assignmentId);
            //志愿记录创建
            VolunteerRecord volunteerRecord = new VolunteerRecord();
            volunteerRecord.setActivityAssignmentId(assignmentId);
            volunteerRecord.setUserId(userId);
            //签到个数与创建个数相同
            if(count(signInLambdaQueryWrapper)==count){
                //计算时长
                double volunteerTime = TimeUtils.calculateHour(newSignInUser.getSignInTime(),newSignInUser.getSignOutTime());
                volunteerRecord.setVolunteerTime(volunteerTime);
            }
            else{
                //设置状态为失败，以及少了签到次数
                volunteerRecord.setStatus(SystemConstants.STATUS_ERROR);
                String reason = "签到次数只有"+count+"次";
                volunteerRecord.setReason(reason);
            }
            volunteerRecordService.save(volunteerRecord);
        }
        //签到记录插入redis中
        String userName = userService.getById(userId).getName();
        QRCodeSignInListVo qrCodeSignInListVo = new QRCodeSignInListVo(userName,new Date());
        redisCache.addListMember("signInUserRecord:"+signInId,qrCodeSignInListVo);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getChildrenSign(Long activityAssignmentId) {
        LambdaQueryWrapper<SignIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SignIn::getAssignmentId,activityAssignmentId);
        List<SignIn> signInList = list(lambdaQueryWrapper);
        List<SignInVo> signInVoList = BeanCopyUtils.copyBeanList(signInList,SignInVo.class);
        signInVoList.stream().forEach(o->{
            //签到已过期
            if(Objects.isNull(redisCache.getCacheObject("signIn:"+o.getId()))){
                o.setStatus(SystemConstants.STATUS_ERROR);
                redisCache.deleteObject("signInUserRecord:"+o.getId());
            }
        });
        return ResponseResult.okResult(signInVoList);
    }

    @Override
    public ResponseResult getQRCodeSignInList(Long signInId) {
        List<QRCodeSignInListVo> qrCodeSignInListVos = redisCache.getCacheList("signInUserRecord:"+signInId,0L,9L);
        return ResponseResult.okResult(qrCodeSignInListVos);
    }

    @Override
    public void getQRCode(Long signInId, HttpServletResponse response) throws IOException {
        //从redis中获取数据，获取不到就说明过期了,此时就前端就获取不到二维码
        SignIn sign = redisCache.getCacheObject("signIn:"+signInId);
        if(Objects.isNull(sign)){
            throw new SystemException(AppHttpCodeEnum.SIGN_IN_TIME_PASS);
        }
        // 设置响应流信息
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream stream = response.getOutputStream();
        //type是1，生成活动详情、报名的二维码，type是2，生成活动签到的二维码
        String content = ("/signIn/QRCodeSignIn?signInId="+signInId+"&&timeStamp="+ new Date().getTime());
        //获取一个二维码图片
        BitMatrix bitMatrix = QRCodeUtils.createCode(content);
        //以流的形式输出到前端
        MatrixToImageWriter.writeToStream(bitMatrix , "jpg" , stream);
    }

    public void setTime(String type,SignInUser sign) {
        //签到数加1
        if (type.equals("1")&&Objects.isNull(sign.getSignInTime())) {
            sign.setSignInTime(new Date());
        } else if (type.equals("2")&&Objects.isNull(sign.getMidTime())) {
            sign.setMidTime(new Date());
        } else if (type.equals("3")&&Objects.isNull(sign.getSignOutTime())) {
            sign.setSignOutTime(new Date());
        } else if(!"123".contains(type)){
            throw new SystemException(AppHttpCodeEnum.SIGN_IN_TYPE_ERROR);
        }
        else {
                throw new SystemException(AppHttpCodeEnum.SIGNED_IN);
        }
        sign.setSignInCount(sign.getSignInCount()+1);
    }
}
