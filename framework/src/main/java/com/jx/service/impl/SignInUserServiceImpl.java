package com.jx.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.ListSignInDto;
import com.jx.domain.entity.SignInUser;
import com.jx.domain.vo.ExcelSignInUserVo;
import com.jx.domain.vo.ListSignInUserVo;
import com.jx.domain.vo.PageVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.mapper.SignInUserMapper;
import com.jx.service.SignInUserService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.PageUtils;
import com.jx.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
        System.err.println(listSignInDto);
        List<ListSignInUserVo> listSignInUserVos = signInUserMapper.listSignIns(listSignInDto.getActivityName(),listSignInDto.getLocationId(),listSignInDto.getTypeId(),listSignInDto.getTimeSlotId());
        Page<ListSignInUserVo> listSignInUserVoPage = PageUtils.listToPage(listSignInUserVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(listSignInUserVoPage.getRecords(),listSignInUserVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public void export(HttpServletResponse httpServletResponse, ListSignInDto listSignInDto) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("签到记录导出.xlsx",httpServletResponse);
            //获取需要导出的数据
            List<ListSignInUserVo> listSignInUserVos =signInUserMapper.listSignIns(listSignInDto.getActivityName(),listSignInDto.getLocationId(),listSignInDto.getTypeId(),listSignInDto.getTimeSlotId());
            System.err.println(listSignInUserVos);
            List<ExcelSignInUserVo> excelCategoryVos = BeanCopyUtils.copyBeanList(listSignInUserVos, ExcelSignInUserVo.class);
            excelCategoryVos.stream().forEach(o->{
                o.setTimeSlot(o.getTimeSlotBegin()+"-"+o.getTimeSlotEnd());
                Long signInTypeId = o.getSignInTypeId();
                if(signInTypeId==1L){
                    o.setSignInType("签到");
                }
                else if(signInTypeId==2L){
                    o.setSignInType("考勤");
                }
                else if(signInTypeId==3L){
                    o.setSignInType("签退");
                }
                else{
                    o.setSignInType("错误");
                }
            });
            System.err.println(excelCategoryVos);
            //把数据写入到Excel中
            EasyExcel.write(httpServletResponse.getOutputStream(), ExcelSignInUserVo.class).autoCloseStream(Boolean.FALSE).sheet("签到记录")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
        }
    }
}
