package com.jx.utils;

import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;

import java.util.Date;

public class TimeUtils {
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
}
