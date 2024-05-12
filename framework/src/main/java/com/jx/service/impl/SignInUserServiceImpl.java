package com.jx.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.ListSignInDto;
import com.jx.domain.entity.SignInUser;
import com.jx.domain.entity.UserImportDetail;
import com.jx.domain.vo.ExcelSignInUserVo;
import com.jx.domain.vo.ListSignInUserVo;
import com.jx.domain.vo.PageVo;
import com.jx.domain.vo.SignInUserExportVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.SignInUserMapper;
import com.jx.service.SignInUserService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.PageUtils;
import com.jx.utils.TimeUtils;
import com.jx.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * (SigninUser)表服务实现类
 *
 * @author makejava
 * @since 2023-10-19 11:01:39
 */
@Service("signInUserService")
public class SignInUserServiceImpl extends ServiceImpl<SignInUserMapper, SignInUser> implements SignInUserService {

    @Autowired
    SignInUserMapper signInUserMapper;
    @Override
    public ResponseResult listSignIns(Long pageNum, Long pageSize, ListSignInDto listSignInDto) {
        List<ListSignInUserVo> listSignInUserVos = signInUserMapper.listSignIns(listSignInDto);
        Page<ListSignInUserVo> listSignInUserVoPage = PageUtils.listToPage(listSignInUserVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(listSignInUserVoPage.getRecords(),listSignInUserVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public void export(HttpServletResponse response, ListSignInDto listSignInDto) {
        //查询导出签到记录
        List<SignInUserExportVo> listSignInUserVos = signInUserMapper.exportSignInUser(listSignInDto);
        boolean flag = "1".equals(listSignInDto.getFlag());
        listSignInUserVos.stream().forEach(o->{
            //将isRedCrossMember标识转换成字符串,userNeed标识转换成字符串
            o.setIsRedCrossMember(("1".equals(o.getIsRedCrossMember())?"是":"否"));
            if(!StringUtils.hasText(o.getUserNeed())){
                o.setUserNeed("暂未选择方式");
            }
            else{
                o.setUserNeed("1".equals(o.getUserNeed())?"开志愿证明":"录入时长进入系统");
            }
            //计算服务时长
            if(flag) {
                double volunteerTime = TimeUtils.calculateHour(o.getSignInTime(), o.getSignOutTime());
                o.setVolunteerTime(volunteerTime);
            }
        });
        try {
            ClassPathResource resource = new ClassPathResource("/template/签到记录导出模板.xlsx");
            String filename  = URLEncoder.encode("签到记录" + System.currentTimeMillis(),"UTF-8");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xlsx");
            ExcelWriter writer = EasyExcel.write(response.getOutputStream()).withTemplate(resource.getInputStream()).build();
            writer.fill(listSignInUserVos,EasyExcel.writerSheet(0).build());
            writer.finish();
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.TEMPLATE_DOWNLOAD_ERROR);
        }
    }
}
