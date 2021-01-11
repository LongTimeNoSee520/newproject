package com.zjtc.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.StringUtils;

/**
 * 时间工具类
 * @author yuchen
 * @date 2020/05/07
 */
public class TimeUtil {


  /**
   * 时间戳字符串转时间
   *
   * @param timestamp 毫秒
   */
  public static Date formatTimestampToDate(String timestamp) {
    Date date = null;
    if (StringUtils.isNotBlank(timestamp)) {
      Long time = Long.parseLong(timestamp);
      date = new Date(time);
    }
    return date;
  }

  /**
   * 获取当天时间字符串
   */
  public static String getTodayTimeStr() {
    String result = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date date = new Date();
      result = sdf.format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 获取明天时间字符串
   */
  public static String getTomorrowTimeStr() {
    String result = null;
    SimpleDateFormat sdf = null;
    Calendar c = null;
    try {
      sdf = new SimpleDateFormat("yyyy-MM-dd");
      c = new GregorianCalendar();
      c.setTime(new Date());
      c.add(c.DATE, 1);
      result = sdf.format(c.getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 获取当日时间字符串 yyyyMMdd
   */
  public static String formatTimeyyyyMMdd() {
    String result = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      Date date = new Date();
      result = sdf.format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 将时间字符串转换成时间戳字符串
   *
   * @param timeStr yyyy-MM-dd HH:mm:ss
   */
  public static String formatTimeToTimestamp(String timeStr) {
    String timestamp = "";
    if (StringUtils.isNotBlank(timeStr)) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        Date d = sdf.parse(timeStr);
        timestamp = d.getTime() + "";
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return timestamp;
  }

  /**
   * 将时间字符串转换成日期时间
   *
   * @param timeStr yyyy-MM-dd HH:mm:ss
   */
  public static Date formatTimeToDate(String timeStr) {
    Date date = null;
    if (StringUtils.isNotBlank(timeStr)) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        date = sdf.parse(timeStr);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return date;
  }

  /**
   * 将时间转换成字符串
   *
   * @param time yyyy-MM-dd HH:mm:ss
   */
  public static String formatTimeToDate(Date time) {
    String timeStr = null;
    if (null!=time) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        timeStr = sdf.format(time);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return timeStr;
  }

  /**
   * 将时间转换成字符串
   *
   * @param time yyyyMMddHHmmss
   */
  public static String formatTimeyyyyMMddHHmmss(Date time) {
    String timeStr = null;
    if (null!=time) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      try {
        timeStr = sdf.format(time);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return timeStr;
  }

  /**
   * 时间转字符串
   *
   * @param date yyyy-MM-dd
   */
  public static String formatTimeStr(Date date) {
    String timeStr = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      timeStr = sdf.format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return timeStr;
  }

  /**
   * 获取明天时间
   *
   * @param date yyyy-MM-dd
   */
  public static String formatTomorrow(Date date) {
    String timeStr = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(Calendar.DAY_OF_MONTH, +1);//时间加一天
      Date tomorrow = calendar.getTime();
      timeStr = sdf.format(tomorrow);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return timeStr;
  }

  /**
   * 获取月底时间
   *
   * @param date yyyy-MM-dd
   */
  public static String getNextMonthFirst(Date date) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DATE, 0);
    return df.format(calendar.getTime());
  }


  /**
   * 获取计算相差天数的时间
   *
   * @param timeStr yyyy-MM-dd
   */
  public static String formatEndTime(String timeStr,int value) {
    if (StringUtils.isNotBlank(timeStr)) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      try {
        Date d = sdf.parse(timeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_MONTH, +value);//时间加一天
        return sdf.format(calendar.getTime());
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * 时间字符串转时间
   *
   * @param dateStr yyyy-MM-dd
   */
  public static Date formatDate(String dateStr) {
    Date date = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      date = sdf.parse(dateStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  /**
   * 时间字符串转时间
   *
   * @param dateStr yyyy/MM/dd
   */
  public static Date excelformatDate(String dateStr) {
    Date date = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    try {
      date = sdf.parse(dateStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }
  /**
   * 日期转换成时间字符串
   * yyyyMMddHHmmss
   */
  public static String formatDateToTime(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String time = sdf.format(date);
    return time;
  }

  /**
   * 获取当月第一天
   *
   * @return yyyy-MM-dd
   */
  public static String getMonthOfFirst() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cale = Calendar.getInstance();
    cale.add(Calendar.MONTH, 0);
    cale.set(Calendar.DAY_OF_MONTH, 1);
    String monthFirst = sdf.format(cale.getTime());
    return monthFirst;
  }

  /**
   * 获取当年第一天
   *
   * @return yyyy-MM-dd
   */
  public static String getYearOfFirst() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cale = Calendar.getInstance();
    cale.add(Calendar.YEAR, 0);
    cale.set(Calendar.DAY_OF_YEAR, 1);
    String yearFirst = sdf.format(cale.getTime());
    return yearFirst;
  }


  /**
   * 时间字符串转日期
   * yyyy-MM-dd
   */
  public static Date dateStrFormatDate(String dateStr) {
    Date d = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      d = sdf.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return d;
  }

  /**
   * yyyy年MM月dd日转换为时间
   * @param dateStr
   * @return
   */
  public static Date format1(String dateStr){
    Date d = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    try {
      d = sdf.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return d;
  }

  /**
   * 参数为时间类型，获取该时间为星期几
   * @param date
   * @return
   */
  public static String getWeek(Date date){
    String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if(week_index<0){
      week_index = 0;
    }
    return weeks[week_index];
  }

  /**
   * 参数为字符串类型，获取该时间为星期几 yyyy年MM月dd日格式的字符串
   * @param dateStr
   * @return
   */
  public  static String getWeek(String dateStr){
    Date date = formatDate(dateStr);
    String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if(week_index<0){
      week_index = 0;
    }
    return weeks[week_index];
  }

  /**
   * 获取传入时间所属月份相差monthDiff个月的第一天
   * @param date，monthDiff
   * @return
   */
  public static Date getMonthFirstDay(Date date, int monthDiff) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, 1);//获取当月第一天
    calendar.add(Calendar.MONTH, monthDiff);//monthDiff: 0是指本月,-1是向前推一个月,1是向后推一个月
    return calendar.getTime();
  }
  /**
   * 获取传入时间所属年份相差yearDiff年的第一天
   * @param date，yearDiff
   * @return
   */
  public static Date getMonthFirstYear(Date date, int yearDiff) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_YEAR, 1);//获取当年第一天
    calendar.add(Calendar.YEAR, yearDiff);//monthDiff: 0是指本月,-1是向前推一个月,1是向后推一个月
    return calendar.getTime();
  }
  /**
   * 获取传入时间是当年第几个季度
   * @param date
   * @return
   */
  public static int toQuarter(Date date) {
    date=formatDate(formatTimeStr(date));
    long january = getMonthFirstYear(date,0).getTime();//1月的第一天
    long april = getMonthFirstDay(getMonthFirstYear(date,0),3).getTime();//4月的第一天
    long july = getMonthFirstDay(getMonthFirstYear(date,0),6).getTime();//7月的第一天
    long october = getMonthFirstDay(getMonthFirstYear(date,0),9).getTime();//10月的第一天
    long nextYear =getMonthFirstYear(date,1).getTime(); //下年的第一天
    if (date.getTime() >= january && date.getTime() < april){
      return  1;
    }
    else  if (date.getTime() >= april && date.getTime() < july){
     return  2;
    }
    else if(date.getTime() >= july && date.getTime() < october){
      return  3;
    }
    else if(date.getTime() >= october && date.getTime() < nextYear){
      return  4;
    }
    return -1;
  }
}
