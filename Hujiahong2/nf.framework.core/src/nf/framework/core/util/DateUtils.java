package nf.framework.core.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	  /**定义常量**/
    public static final String DATE_JFP_STR="yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    public static final String DATE_TIME_STR = "HH:mm:ss";
    /**
     * 使用预设格式提取字符串日期
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate,DATE_FULL_STR);
    }
     
    /**
     * 使用用户格式提取字符串日期
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    @SuppressLint("SimpleDateFormat")
	public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
     
    /**
     * 两个时间比较
     * @param date
     * @return
     */
    public static int compareDateWithNow(Date date1){
        Date date2 = new Date();
        int rnum =date1.compareTo(date2);
        return rnum;
    }
     
    /**
     * 两个时间比较(时间戳比较)
     * @param date
     * @return
     */
    public static int compareDateWithNow(long date1){
        long date2 = dateToUnixTimestamp();
        if(date1>date2){
            return 1;
        }else if(date1<date2){
            return -1;
        }else{
            return 0;
        }
    }
    /***
     * 一分钟之内算作接近
     * @param timeStr1
     * @param timeStr2
     * @return
     */
    public static  boolean isCloseEnough(String timeStr1,String timeStr2){
    	
    	return isCloseEnough(timeStr1, timeStr2, 60*1000);
    }
    /**
     * 
     * @param timeStr1
     * @param timeStr2
     * @param timeStamp
     * @return
     */
    public static  boolean isCloseEnough(String timeStr1,String timeStr2,long timeStamp){
    	if(TextUtils.isEmpty(timeStr1)||TextUtils.isEmpty(timeStr2)){
    		return false;
    	}	
    	long time1=dateToUnixTimestamp(timeStr1);
    	long time2=dateToUnixTimestamp(timeStr2);
    	return isCloseEnough(time1, time2,timeStamp);
    }
    /***
     * 一分钟之内算作接近
     * @return
     */
    public static  boolean isCloseEnough(long time1,long time2){

    	return isCloseEnough(time1, time2, 60*1000);
    	
    }
   /**
    * 相隔时间内接近
    * @param time1
    * @param time2
    * @param timeStamp   相隔时间差
    * @return
    */
    public static  boolean isCloseEnough(long time1,long time2,long timeStamp){

    	if(Math.abs(time1-time2)<timeStamp){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 获取系统当前时间
     * @return
     */
    @SuppressLint("SimpleDateFormat")
	public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }
     
    /**
     * 获取系统当前时间
     * @return
     */
    @SuppressLint("SimpleDateFormat")
	public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }
     
    /**
     * 获取系统当前计费期
     * @return
     */
    @SuppressLint("SimpleDateFormat")
	public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_JFP_STR);
        return df.format(new Date());
    }
     
    /**
     * 将指定的日期转换成Unix时间戳
     * @param String date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    @SuppressLint("SimpleDateFormat")
	public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
     
    /**
     * 将指定的日期转换成Unix时间戳
     * @param String date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    @SuppressLint("SimpleDateFormat")
	public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
     
    /**
     * 将当前日期转换成Unix时间戳
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }
     
     
    /**
     * 将Unix时间戳转换成日期
     * @param long timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
    	return unixTimestampToDate(timestamp,DATE_FULL_STR);
    }
    /**
     * //设置时区，慎用
     * @param timestamp
     * @param format
     * @return
     */
	@SuppressLint("SimpleDateFormat") 
	@Deprecated
	public static String unixTimestampToDate(long timestamp,String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }
	
	public static String unixTimestampFormat(long timestamp,String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(new Date(timestamp));
    }
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>()
	{
		@SuppressLint("SimpleDateFormat")
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat(DATE_FULL_STR);
		}
	};
	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate)
	{
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null)
		{
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate))
			{
				b = true;
			}
		}
		return b;
	}
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>()
	{
		@SuppressLint("SimpleDateFormat")
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat(DATE_SMALL_STR);
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate)
	{
		try
		{
			return dateFormater.get().parse(sdate);
		} catch (ParseException e)
		{
			return null;
		}
	}

	public static String friendly_time(long timestamp){
		
		return friendly_time(unixTimestampToDate(timestamp));
	}
	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate)
	{
		if(TextUtils.isEmpty(sdate)){
			return "";
		}
		Date time = toDate(sdate);
		if (time == null)
		{
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate))
		{
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0){
				long timeVal  = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1);
				if(timeVal==1){
					ftime ="刚刚";
				}else if(timeVal>1){
					ftime = timeVal + "分钟前";
				}
			}
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0)
		{
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0){
				long timeVal = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1);
						
				if(timeVal==1){
					ftime ="刚刚";
				}else{
					ftime = timeVal + "分钟前";
				}
			}else{
				ftime = hour + "小时前";
			}
		} else if (days == 1)
		{
			ftime = "昨天";
		} else if (days == 2)
		{
			ftime = "前天";
		} else if (days > 2 && days <= 10)
		{
			ftime = days + "天前";
		} else if (days > 10)
		{
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

}
