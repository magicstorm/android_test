package com.lansun.tests.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by felix.fan on 2016/9/26.
 */

public class TimeUtil {
    public static String createLastRefreshTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String updateTime = simpleDateFormat.format(date);
        return updateTime;
    }

    public static String calculateDuration(String duration){
        int hoursDuration = Integer.valueOf(duration) / 60;
        int minDuration = Integer.valueOf(duration)%60;
        return "时长  " + String.valueOf(hoursDuration) + "小时" + String.valueOf(minDuration) + "分钟";
    }

    public static String beforeDuration(String timeStamp, String date, String time){
        //wait
        Long timeStampNumber = Long.valueOf(timeStamp);
        Long curTime = System.currentTimeMillis();
        Long deltaInSecond = (curTime - timeStampNumber)/1000;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date curDate = cal.getTime();
        String curDateString = sdf.format(curDate);
        if(!curDateString.equals(date)){
            Long deltaInDay = deltaInSecond/86400;
            if(deltaInDay<31){
                return String.valueOf(deltaInSecond/86400)+"天前";
            }
            else{
                return date;
            }
        }
        else{
            if(deltaInSecond>3600*12){
                return time;
            }
            else if(deltaInSecond>3600){
                return String.valueOf(deltaInSecond/3600)+"小时前";
            }
            else if(deltaInSecond>60){
                return String.valueOf(deltaInSecond/60)+"分钟前";
            }
            else{
                return String.valueOf(deltaInSecond)+"秒前";
            }
        }
    }

    public static String beforDuration(long timeStamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(timeStamp);
        String time = timeFormat.format(timeStamp);
        return beforeDuration(String.valueOf(timeStamp), date, time);
    }
//    private static Long getTodayStartTimeStamp(){
//
//    }

    public static String getDuration(String startTime, String endTime){
        String[] start = startTime.split(":");
        String[] end = endTime.split(":");

        int borrowHour = 0;
        int deltaMin = Integer.valueOf(end[1])-Integer.valueOf(start[1]);
        if(deltaMin<0){
            deltaMin += 60;
            borrowHour = 1;
        }


        int deltaHour = Integer.valueOf(end[0])-Integer.valueOf(start[0])-borrowHour;

        return String.valueOf(deltaHour) + "小时" + String.valueOf(deltaMin) + "分";


//        //milliseconds
//        long different = endDate.getTime() - startDate.getTime();
//
//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : "+ endDate);
//        System.out.println("different : " + different);
//
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;
//
//        return elapsedHours + "小时" + elapsedMinutes + "分";
    }


    public static String getFormattedTime(String timestamp, String date, String time){
        Calendar cal = Calendar.getInstance();
        Date oriCurDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String curDateString = sdf.format(oriCurDate);
        Date curDate = null;
        try {
            curDate = sdf.parse(curDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date sessionDate = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(date!=null){
                sessionDate = sdf.parse(date);
            }
            else{
                sessionDate = new Date(Long.parseLong(timestamp));
                String sessionDateString = sdf.format(sessionDate);
                sessionDate = sdf.parse(sessionDateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long curTime = oriCurDate.getTime();

        long delta = curTime-Long.valueOf(timestamp);

        Date d = new Date(Long.parseLong(timestamp));
//        ||curDate.after(sessionDate)
        if(delta>=1000*60*60*24){
            if(date==null||time==null){
                SimpleDateFormat sdfAll = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return sdfAll.format(d);
            }else{
                return date + " " + time;
            }
        }
        else if(delta>=1000*60*60){
            if(time==null){
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                return sdfTime.format(d);
            }
            return time;
        }
        else{
            return String.valueOf(Math.floor(delta/(1000*60)))+"分钟前";
        }
    }
}
