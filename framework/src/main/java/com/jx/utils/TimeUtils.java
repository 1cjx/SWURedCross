package com.jx.utils;

import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import io.swagger.models.auth.In;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    //比较时间大小
    public static boolean timeJudge(String beginTime,String endTime){
        if(!beginTime.contains(":")||!endTime.contains(":")){
            throw new SystemException(AppHttpCodeEnum.TIME_FORMAT_ERROR);
        }
        int hour1 = getHourOrMinute(beginTime,true);
        int hour2 = getHourOrMinute(endTime,true);
        int minute1 = getHourOrMinute(beginTime,false);
        int minute2 = getHourOrMinute(endTime,false);
        if(!(validHourOrMinute(hour1,true)&&validHourOrMinute(hour2,true)&&validHourOrMinute(minute1,false)&&validHourOrMinute(minute2,false))){
            throw new SystemException(AppHttpCodeEnum.TIME_INFO_ERROR);
        }
        if(hour1>hour2||(hour1==hour2&&minute1>minute2)){
            throw   new SystemException(AppHttpCodeEnum.TIME_INFO_ERROR);
        }
        return true;
    }
    private static int getHourOrMinute(String time,boolean hour){
        if (hour){
            return Integer.parseInt(time.substring(0,time.indexOf(":")));
        }else{
            return  Integer.parseInt(time.substring(time.indexOf(":")+1));
        }
    }
    private static boolean validHourOrMinute(int time,boolean hour){
        if(hour){
            return 0<=time&&time<24;
        }
        else{
            return 0<=time&&time<60;
        }
    }

    //计算志愿时长
    public static double calculateHour(Date start, Date end){
        Long time = end.getTime() - start.getTime() ;
        time/=(1000*60);//转化为分钟
        Long remainder = time%60L;
        Long temp = remainder < 20L ? 0L : (remainder < 40L ? 30L : 60L);
        Long hour = time -remainder + temp;
        double resultHour = (double)hour/60;
        System.err.println(resultHour);
        return resultHour;
    }
    //获取距离本月结束还有多少秒
    public static Integer getSecondToNextMonth(){
        LocalDate today = LocalDate.now();
        //本月的最后一天
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
        Date endTimeInMonth = Date.from(lastDay.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        Calendar calendar = Calendar.getInstance();
        if(null != endTimeInMonth ) {calendar.setTime(endTimeInMonth );}
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return Math.toIntExact((date.getTime() - System.currentTimeMillis())/1000);
    }
}
