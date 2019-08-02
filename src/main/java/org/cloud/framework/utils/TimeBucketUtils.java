package org.cloud.framework.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimeBucketUtils {
    public static void main(String[] args) throws Exception {

        String start = "2013";
        String end = "2018";

        /*List<Object> dateList = getYear(start, end);
        for (Object date : dateList) {
            System.out.println(date);
        }*/
        //System.out.println(newDate());
       /* String lastDay = getFisrtDayOfMonth(2014,5);
        System.out.println("脚本之家测试结果：");
        System.out.println("获取当前月的最后一天：" + lastDay);*/
        /*Calendar now = Calendar.getInstance();
        System.out.println("年: " + now.get(Calendar.YEAR));
        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        System.out.println("时: " + now.get(Calendar.HOUR_OF_DAY));
        System.out.println("分: " + now.get(Calendar.MINUTE));
        System.out.println("秒: " + now.get(Calendar.SECOND));
        System.out.println("当前时间毫秒数：" + now.getTimeInMillis());*/
        System.out.println(newsYear());
        System.out.println(newsMonth());
        System.out.println(newsDay());
    }
    //J获取某段时间内的所有年月日
    public static  LinkedHashMap<String,Object> getday(String start, String end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dStart = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);
        LinkedHashMap<String,Object> r = new LinkedHashMap<String,Object>();
        List<Object> dateList = new ArrayList<Object>();
        LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        //别忘了，把起始日期加上
        dateList.add(sdf.format(dStart));
        map.put(sdf.format(dStart),"0");
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            String formatTime = sdf.format(cStart.getTime());
            dateList.add(formatTime);
            map.put(formatTime,"0");
        }
        r.put("dateList",dateList);
        r.put("dateMap",map);
        return r;
    }
    //J获取某段时间内的所有年月
    public static LinkedHashMap<String,Object> getMonths(String start, String end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date dStart = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);
        LinkedHashMap<String,Object> r = new LinkedHashMap<String,Object>();
        List<Object> dateList = new ArrayList<Object>();
        LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        //别忘了，把起始日期加上
        dateList.add(sdf.format(dStart));
        map.put(sdf.format(dStart),"0");
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.MONTH, 1);
            String formatTime = sdf.format(cStart.getTime());
            dateList.add(formatTime);
            map.put(formatTime,"0");
        }
        r.put("dateList",dateList);
        r.put("dateMap",map);
        return r;
    }
    //J获取某段时间内的所有年月
    public static LinkedHashMap<String,Object> getYear(String start, String end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date dStart = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);
        LinkedHashMap<String,Object> r = new LinkedHashMap<String,Object>();
        List<Object> dateList = new ArrayList<Object>();
        LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        //别忘了，把起始日期加上
        dateList.add(sdf.format(dStart));
        map.put(sdf.format(dStart),"0");
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.YEAR, 1);
            String formatTime = sdf.format(cStart.getTime());
            dateList.add(formatTime);
            map.put(formatTime,"0");
        }
        r.put("dateList",dateList);
        r.put("dateMap",map);
        return r;
    }
    public static String forwardDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal1 = Calendar.getInstance();
        //昨天
        cal1.add(Calendar.DATE,-6);
        Date time = cal1.getTime();
        return sdf.format(time);
    }
    public static String newDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal1 = Calendar.getInstance();
        //昨天
        cal1.add(Calendar.DATE,0);
        Date time = cal1.getTime();
        return sdf.format(time);
    }
    public static String getFisrtDayOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }
    public static String getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }
    public static String newsYear(){
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }
    public static String newsMonth(){
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }
    public static String newsDay(){
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }
}
