package com.junyou.utils.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;

import com.junyou.constants.GameConstants;
import com.junyou.utils.datetime.GameSystemTime;

public class ObjectUtil {
	
	/**
	 * 判断字符串是否为空<br>
	 * null, "", "  "均返回true
	 * @param obj
	 * @return
	 */
	public static boolean strIsEmpty(String str){
		if( str==null || str.trim().length() == 0 ){
			return true;
		}
		return false;
	}
	
	public static String double2Str(Object obj){
		if( obj instanceof Double ){
			return String.valueOf(((Double)obj).longValue());
		}else if( obj instanceof Float ){
			return String.valueOf(((Float)obj).longValue());
		}else if( obj instanceof Long ){
			return ( (Long)obj ).toString();
		}else if( obj instanceof Integer ){
			return ( (Integer)obj ).toString();
		}
		return obj.toString();
	}
	
	/**
	 *  判断时间是否为当天
	 */
	public static boolean dayIsToday(Long time){
		if( time == null || time == 0 ){
			return false;
		}
		return dayIsToday(new Timestamp(time));
	}
	/**
	 *  判断时间是否为当天
	 */
	public static boolean dayIsToday(Timestamp time){
		if( time == null ) return false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String day = sdf.format(new Date(time.getTime()));
		
		String today = sdf.format(new Date());
		
		return today.equals(day);
	}
	
	private static final long MILLDAY = 24*60*60*1000l;
	private static final int MILLHOUR = 60*60*1000;
	/**
	 * 计算两个日期的相差天数
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int twoDaysDiffence(Calendar c1, Calendar c2){
		c1.add(Calendar.HOUR_OF_DAY, getCurTimeZoneDiffWithUTC());//由于当前时区与格林威治时间差
		c2.add(Calendar.HOUR_OF_DAY, getCurTimeZoneDiffWithUTC());
		
		long time1 = c1.getTimeInMillis()/MILLDAY;
		long time2 = c2.getTimeInMillis()/MILLDAY;
		
		return Long.valueOf( Math.abs(time1 - time2) ).intValue();
	}
	
	/** 获取当前时区与标准时区的时间差,单位小时(北京:8,韩国9) */
	private static int getCurTimeZoneDiffWithUTC(){
		return TimeZone.getTimeZone(TimeZone.getDefault().getID()).getRawOffset()/MILLHOUR;
	}
	
	/**
	 * 计算日期1距离今天的天数
	 * @param c1
	 * @return
	 */
	public static int twoDaysDiffence(Calendar c1){
		c1.add(Calendar.HOUR_OF_DAY, getCurTimeZoneDiffWithUTC());
		
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis( GameSystemTime.getSystemMillTime() );
		
		return twoDaysDiffence(c1, c2);
	}
	
	/**
	 * 计算日期1距离今天的天数(返回的始终是正数)
	 * @param c1
	 * @return
	 */
	public static int twoDaysDiffence(Date date1){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		
		Calendar c2 = Calendar.getInstance();
		
		return twoDaysDiffence(c1, c2);
	}
	
	/**
	 * 获取当日指定时间的毫秒数
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getTheTime(int hour, int minute){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTimeInMillis();
	}
	/**
	 * 获取diff天后的日期
	 * @param date
	 * @param diff
	 * @return
	 */
	public static Date getDiffDay(Date date, int diff){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.add( Calendar.DAY_OF_YEAR , diff);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		
		return cal.getTime();
	}
	
	/**
	 * 得到哪一天哪个小时的时间戳
	 * @param date
	 * @param hours
	 * @return
	 */
	public static long getDayHuor(Date date, int hours){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.set( Calendar.HOUR_OF_DAY , hours);
		cal.set( Calendar.MINUTE , 0);
		cal.set( Calendar.SECOND , 0);
		cal.set( Calendar.MILLISECOND , 0);
		
		return cal.getTime().getTime();
	}
	
	/**
	 * 是否在不同的小时
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isDeffHour(long time1,long time2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(time1);
		
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		
		if(!DateUtils.isSameDay(cal1, cal2)){
			return true;
		}
		
		return cal1.get(Calendar.HOUR_OF_DAY) != cal2.get(Calendar.HOUR_OF_DAY);
		
	}
	
	/**
	 * 返回date1,date2相隔天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int twoDaysDiffence(Date date1, Date date2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		
		return twoDaysDiffence(calendar1, calendar2);
	}
	/**
	 * 返回time1,time2相隔天数
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int twoDaysDiffence(long time1, long time2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(time1);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(time2);
		
		return twoDaysDiffence(calendar1, calendar2);
	}
	/**
	 * 获取明日凌晨的毫秒数
	 * @return
	 */
	public static long getTomorrow00Time(){
		Calendar cal = Calendar.getInstance();
//		System.out.println(cal.toString());
		cal.add( Calendar.DAY_OF_YEAR , 1);
		cal.set( Calendar.HOUR , 0);
		cal.set( Calendar.HOUR_OF_DAY , 0);
		cal.set( Calendar.MINUTE , 0);
		cal.set( Calendar.SECOND , 0);
		cal.set( Calendar.MILLISECOND , 0);
//		System.out.println( cal.toString() );
		
		return cal.getTimeInMillis();
	}

	/**
	 * 获取date凌晨0:0:0的毫秒数
	 * @return
	 */
	public static long getDate00Time(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set( Calendar.HOUR , 0);
		cal.set( Calendar.HOUR_OF_DAY , 0);
		cal.set( Calendar.MINUTE , 0);
		cal.set( Calendar.SECOND , 0);
		cal.set( Calendar.MILLISECOND , 0);
//		System.out.println( cal.toString() );
		
		return cal.getTimeInMillis();
	}
	/**
	 * 把字符串List转换为逗号分隔的字符串
	 * @param strList
	 * @return
	 */
	public static String convertList2Str(List<String> strList){
		if( strList == null || strList.size() == 0 ){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for( String str : strList ){
			builder.append(",").append(str);
		}
		return builder.substring(1);
	}
	
	/**
	 * string数组转换成int
	 * @param strs
	 * @return
	 */
	public static int[] convertToInt(String[]strs){
		int [] result = new int[strs.length];
		for(int i=0;i<strs.length;i++){
			result[i] = Integer.valueOf(strs[i]);
		}
		return result;
	}

	/**
	 * string数组转换成int
	 * @param strs
	 * @return
	 */
	public static int[] convertToInt(List<Integer> data){
		if(data == null) {
			return null;
		}
		int [] result = new int[data.size()];
		for(int i=0;i<data.size();i++){
			result[i] = (int)data.get(i);
		}
		return result;
	}
	/**
	 * 两个Map相加
	 * @param main	主map
	 * @param add	需要加的map
	 */
	public static <T> void mapAdd(Map<T,Integer> main,Map<T,Integer> add){
		if(add == null || add.size() < 1){
			return;
		}
		for (Entry<T, Integer> entry : add.entrySet()) {
			Integer value = main.get(entry.getKey());
			if(value == null){
				value = entry.getValue();
			}else{
				value += entry.getValue();
			}
			main.put(entry.getKey(), value);
		}
	}
	/**
	 * Map值放大N倍
	 * @param main	主map
	 * @param times	放大倍数
	 */
	public static <T> void mapTimes(Map<T,Integer> main,int times){
		for (Entry<T, Integer> entry : main.entrySet()) {
			main.put(entry.getKey(), entry.getValue() * times );
		}
	}
	
	/**
	 * Map值放大N倍
	 * @param main	主map
	 * @param times	放大倍数
	 */
	public static <T> void mapTimes(Map<T,Integer> main,float times){
		for (Entry<T, Integer> entry : main.entrySet()) {
			main.put(entry.getKey(), (int)(entry.getValue() * times) );
		}
	}
	/**
	 * 两个Map相加
	 * @param main	主map
	 * @param add	需要加的map
	 */
	public static <T> void longMapAdd(Map<T,Long> main,Map<T,Long> add){
		if(add == null || add.size() < 1){
			return;
		}
		for (Entry<T, Long> entry : add.entrySet()) {
			Long value = main.get(entry.getKey());
			if(value == null){
				value = entry.getValue();
			}else{
				value += entry.getValue();
			}
			main.put(entry.getKey(), value);
		}
	}
	/**
	 * Map值放大N倍
	 * @param main	主map
	 * @param times	放大倍数
	 */
	public static <T> void longMapTimes(Map<T,Long> main,int times){
		for (Entry<T, Long> entry : main.entrySet()) {
			main.put(entry.getKey(), entry.getValue() * times );
		}
	}
	
	/**
	 * Map值放大N倍
	 * @param main	主map
	 * @param times	放大倍数
	 */
	public static <T> void longMapTimes(Map<T,Long> main,float times){
		for (Entry<T, Long> entry : main.entrySet()) {
			main.put(entry.getKey(), (long)(entry.getValue() * times) );
		}
	}
	
	/**
	 * 对集合泛型list判空验证
	 * @param list
	 * @return true=空,false=非空
	 */
	public static boolean isEmpty(List<?> list) {
		if (null == list || list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否为空
	 * @param val
	 * @return true:为空null 或 ""
	 */
	public static boolean isEmpty(String val){
		
		if(null == val || val.trim().isEmpty()){
			return true;
		}
		
		return false;
	}

    /**
     * Long判空验证
     * @param Long
     * @return true=空,false=非空
     */
    public static boolean isEmpty(Long long1) {
        if (null == long1 || long1 == 0l) {
            return true;
        }
        return false;
    }
	
	/**
     * 对集合判空验证
     * @param Collection
     * @return true=空,false=非空
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (null == collection || collection.isEmpty()) {
            return true;
        }
        return false;
    }

	/**
	 * 对集合泛型map判空验证
	 * @param map
	 * @return true=空,false=非空
	 */
	public static boolean isEmpty(Map<?,?> map) {
		if (null == map || map.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
     * 对集合数组array判空验证
     * @param list
     * @return true=空,false=非空
     */
    public static boolean isEmpty(Object[] array) {
        if (null == array || array.length <= 0) {
            return true;
        }
        return false;
    }
	
    /**
     * @Description map对象转换对象数组
     * @param map
     * @return
     */
    public static Object[] map2ObjArray(Map<?, ?> map) {
        List<Object[]> objList = new ArrayList<Object[]>();
        if (!isEmpty(map)) {
            for (Entry<?, ?> entry : map.entrySet()) {
                objList.add(new Object[] { entry.getKey(), entry.getValue() });
            }
        }
        return objList.toArray();
    }

    /**
     * @Description map对象转换以"key1:value1|key2:value2|...keyN:valueN"的字符串
     * @param map
     * @return
     */
    public static String map2String(Map<?, ?> map) {
        StringBuilder str = null;
        if (!isEmpty(map)) {
            for (Entry<?, ?> entry : map.entrySet()) {
                if (null == str) {
                    str = new StringBuilder();
                    str.append(entry.getKey()).append(GameConstants.CONFIG_SUB_SPLIT_CHAR).append(entry.getValue());
                } else {
                    str.append(GameConstants.CONFIG_SPLIT_CHAR).append(entry.getKey()).append(GameConstants.CONFIG_SUB_SPLIT_CHAR).append(entry.getValue());
                }
            }
        }
        return null == str ? null : str.toString();
    }

    /**
	 * 检查是否为当前月
	 * @return
	 */
	public static boolean checkIsSameMonth(long date1, long date2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		
		int year1 = calendar1.get(Calendar.YEAR);
		int year2 = calendar2.get(Calendar.YEAR);
		
		int month1 = calendar1.get(Calendar.MONTH);
		int month2 = calendar2.get(Calendar.MONTH);
		if(year1 == year2 && month1 == month2){
			return true;
		}
		return false;
	}
	
    /**
	 * 检查是否为当前月
	 * @return
	 */
	public static int getDayOfMonth(long date){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date);
		return calendar1.get(Calendar.DAY_OF_MONTH);
	}
    
}