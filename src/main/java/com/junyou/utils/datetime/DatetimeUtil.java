package com.junyou.utils.datetime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.constants.GameConstants;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

public class DatetimeUtil {
	/**
	 * yyyy/MM/dd/HH:mm:ss
	 */
	public static final String FORMART = "yyyy/MM/dd/HH:mm:ss";
	/**
	 * yyyy-MM-dd
	 */
	public static final String FORMART2 = "yyyy-MM-dd";
	
	/** yyyy/MM/dd */
	public static final String FORMART8 = "yyyy/MM/dd";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String FORMART3 = "yyyy-MM-dd HH:mm:ss";
	/**
	 * HH:mm
	 */
	public static final String FORMART4 = "HH:mm";
	/**
	 * yyyy-MM-dd HH
	 */
	public static final String FORMART5 = "yyyy-MM-dd HH";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String FORMART6 = "yyyy-MM-dd HH:mm";
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final String FORMART7 = "yyyy/MM/dd HH:mm:ss";
	/**
	 * 1秒对应的毫秒数:1000
	 */
    public static final long SECOND_MILLISECOND = 1000;
    /**
     * 1分钟对应的毫秒数:60*1000
     */
    public static final long MINUTE_MILLISECOND = 60 * SECOND_MILLISECOND;
    /**
     * 1小时对应的毫秒数:60*60*1000
     */
    public static final long HOUR_MILLISECOND = 60 * MINUTE_MILLISECOND;
    /**
     * 1天对应的毫秒数:24*60*60*1000
     */
    public static final long DAY_MILLISECOND = 24 * HOUR_MILLISECOND;
	
	
	
	private static final Logger LOG = LogManager.getLogger(DatetimeUtil.class);
	
	/**
	 * 当前日期基础上增加分钟
	 * @param minute 分钟
	 * @return 毫秒数
	 */
	public static int addMinuteTime(int minute) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(GameSystemTime.getSystemMillTime());
		c.add(Calendar.MINUTE, minute);
		
		return CovertObjectUtil.object2int(c.getTimeInMillis());
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
     * day天后的24点毫秒
     * @author wind
     * @return
     */
    public static Long getCalaDayTime(int day, int hour,int minute,int second){
    	Calendar d1 = Calendar.getInstance();
    	d1.setTimeInMillis(GameSystemTime.getSystemMillTime());
    	d1.set(Calendar.HOUR_OF_DAY, hour);
    	d1.set(Calendar.MINUTE, minute);
    	d1.set(Calendar.SECOND, second);
    	d1.set(Calendar.MILLISECOND, 0);
    	
    	d1.add(Calendar.DAY_OF_YEAR, day);
    	
    	return d1.getTimeInMillis();
    }
    
    /**
	 * 获取当前时间是周几
	 * @return
	 */
	public static int getCurWeek(){
		Calendar calendarMonday = Calendar.getInstance();
		calendarMonday.setTimeInMillis(GameSystemTime.getSystemMillTime());
		
		// 获取今天是星期几
		return calendarMonday.get(Calendar.DAY_OF_WEEK) == 1 ? 7 :calendarMonday.get(Calendar.DAY_OF_WEEK) - 1 ;
	}
    
	/**
	 * 判断时间是否同一周（每周的周日为一周的开始）
	 * @param time
	 * @return 是同一周返回 true 
	 */
	public static boolean dayIsToWeek(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int weekFoYear = calendar.get(Calendar.WEEK_OF_YEAR);
		
		calendar.setTimeInMillis(GameSystemTime.getSystemMillTime());
		int newWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		if( newWeek == weekFoYear ){
			return true;
		}
		return false;
	}
	/**
	 * 获取下个周几的0点时间戳
	 * @param 周几（1:周日，2：周一)
	 * @return 
	 */
	public static long getNextWeek(int weekEnd){
		long cur = GameSystemTime.getSystemMillTime();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(cur);
		calendar.set(Calendar.DAY_OF_WEEK, weekEnd);
		calendar.set( Calendar.HOUR , 0);
		calendar.set( Calendar.HOUR_OF_DAY , 0);
		calendar.set( Calendar.MINUTE , 0);
		calendar.set( Calendar.SECOND , 0);
		calendar.set( Calendar.MILLISECOND , 0);
		
		long time = calendar.getTimeInMillis();
		if(time < cur){
			time += 7 * GameConstants.DAY_TIME;
		}
		
		return time;
		
	}
	/**
	 * 判断时间是否同一周（每周的周日为一周的开始）
	 * @param time
	 * @param 以周几为时间节点（1:周一，2：周二)
	 * @return 是同一周返回 true 
	 */
	public static boolean dayIsToWeek(Long time,int weekEnd){
		Calendar calendar = Calendar.getInstance();
		time -= weekEnd * GameConstants.DAY_TIME;
		calendar.setTimeInMillis(time);
		int weekFoYear = calendar.get(Calendar.WEEK_OF_YEAR);

		long cur = GameSystemTime.getSystemMillTime() - weekEnd * GameConstants.DAY_TIME;
		calendar.setTimeInMillis(cur);
		int newWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		if( newWeek == weekFoYear ){
			return true;
		}
		return false;
	}
	/**
	 * 判断时间是否同一月
	 * @param time
	 * @return 是同一周返回 true 
	 */
	public static boolean isSameMonth(Long time){
		if( time == null )return false;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(GameSystemTime.getSystemMillTime());
		if( calendar.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
				&&  calendar.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) ){
			return true;
		}
		return false;
	}
	
	/**
	 *  判断时间是否为当天
	 */
	public static boolean dayIsToday(Timestamp time){
		if( time == null ) return false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String day = sdf.format(new Date(time.getTime()));
		
		String today = sdf.format(new Date(GameSystemTime.getSystemMillTime()));
		
		return today.equals(day);
	}
	
	/**
	 *  判断时间是否为当天
	 */
	public static boolean dayIsToday(long t1,long t2){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String day = sdf.format(new Date(t1));
		
		String today = sdf.format(new Date(t2));
		
		return today.equals(day);
	}
	
	private static final long MILLDAY = 24*60*60*1000l;
	/**
	 * 计算两个日期的相差天数
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int twoDaysDiffence(Calendar c1, Calendar c2){
		c1.add(Calendar.HOUR_OF_DAY, getZoneOffHour());//由于当前时区与格林威治时间所处时区不一致，所以需加上8小时
		c2.add(Calendar.HOUR_OF_DAY, getZoneOffHour());
		
		long time1 = c1.getTimeInMillis()/MILLDAY;
		long time2 = c2.getTimeInMillis()/MILLDAY;
		
		return Long.valueOf( Math.abs(time1 - time2) ).intValue();
	}
	/**
	 * 计算两个日期的相差天数
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int twoDaysDiffence(long l1, long l2){
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(l1);
		c1.add(Calendar.HOUR_OF_DAY, getZoneOffHour());//由于当前时区与格林威治时间所处时区不一致，所以需加上8小时
		
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(l2);
		c2.add(Calendar.HOUR_OF_DAY, getZoneOffHour());
		
		long time1 = c1.getTimeInMillis()/MILLDAY;
		long time2 = c2.getTimeInMillis()/MILLDAY;
		
		return Long.valueOf( Math.abs(time1 - time2) ).intValue();
	}
	
	/**
	 * 获取时区偏移小时（+13 ~ -12）
	 * @return
	 */
	private static int getZoneOffHour(){
		TimeZone timeZone = TimeZone.getDefault();
		timeZone.getRawOffset();
		
		return timeZone.getRawOffset() / (3600 * 1000);
	}
	
	/**
	 * 计算日期1距离今天的天数
	 * @param c1
	 * @return
	 */
	public static int twoDaysDiffence(Calendar c1){
		c1.add(Calendar.HOUR_OF_DAY, getZoneOffHour());
		
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis( GameSystemTime.getSystemMillTime() );
		
		return twoDaysDiffence(c1, c2);
	}
	
	/**
	 * 计算日期1距离今天的天数(返回的始终是正数)
	 * @param c1
	 * @return
	 */
	public static int twoDaysDiffence(Long time1){
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis( time1 );
		
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
		c2.setTimeInMillis( GameSystemTime.getSystemMillTime() );
		
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
		cal.setTimeInMillis(GameSystemTime.getSystemMillTime());
		return getTime(cal,hour, minute);
	}
	
	/**
	 * 获取前一天指定时间的毫秒数
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getBefore1Time(int hour, int minute){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(GameSystemTime.getSystemMillTime());
		cal.add( Calendar.DAY_OF_YEAR , -1);
		return getTime(cal,hour, minute);
	}
	
	/**
	 * 获取下一个指定时间的毫秒数
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getNextTime(int hour, int minute){
		long l1 = getTheTime(hour, minute);
		long nowTime = GameSystemTime.getSystemMillTime();
		if(nowTime > l1) {
			return getAfter1Time(hour, minute);
		}
		return l1;
	}
	
	/**
	 * 获取指定指定时间的毫秒数
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getTheDayTheTime(int hour, int minute,long time){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return getTime(cal,hour, minute);
	}
	
	
	/**
	 * 获取后一天指定时间的毫秒数
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getAfter1Time(int hour, int minute){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(GameSystemTime.getSystemMillTime());
		cal.add( Calendar.DAY_OF_YEAR , 1);
		return getTime(cal,hour, minute);
	}
	
	private static long getTime(Calendar cal,int hour, int minute){
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
		
		return cal.getTime();
	}
	
	/**
	 * 返回date1,date2相隔天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int twoDaysDiffence(Date date1, Date date2){
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		
		long l1 = time1 / (24*60*60*1000l);
		long l2 = time2 / (24*60*60*1000l);
		
		return Long.valueOf( Math.abs(l1 - l2) ).intValue();
	}
	/**
	 * 获取明日凌晨的毫秒数
	 * @return
	 */
	public static long getTomorrow00Time(){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(GameSystemTime.getSystemMillTime());
		cal.add( Calendar.DAY_OF_YEAR , 1);
		cal.set( Calendar.HOUR , 0);
		cal.set( Calendar.HOUR_OF_DAY , 0);
		cal.set( Calendar.MINUTE , 0);
		cal.set( Calendar.SECOND , 0);
		cal.set( Calendar.MILLISECOND , 0);
		
		return cal.getTimeInMillis();
	}
	
	
	/**
	 * 获取指定日期的凌晨几点时间戳
	 * @return
	 */
	public static long getTimeByTargetD(long time,int day,int minute){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.add( Calendar.DAY_OF_YEAR , day);
		cal.set( Calendar.HOUR , 0);
		cal.set( Calendar.HOUR_OF_DAY , 0);
		cal.set( Calendar.MINUTE , minute);
		cal.set( Calendar.SECOND , 0);
		cal.set( Calendar.MILLISECOND , 0);
		
		return cal.getTimeInMillis();
	}

	/**
	 * 获取calendar凌晨23:59:59
	 * @return
	 */
	public static String getDate23Time(){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(GameSystemTime.getSystemMillTime());
		calendar.set( Calendar.HOUR , 11);
		calendar.set( Calendar.HOUR_OF_DAY , 23);
		calendar.set( Calendar.MINUTE , 59);
		calendar.set( Calendar.SECOND , 59);
		calendar.set( Calendar.MILLISECOND , 0);
		
		return formatTime(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm:ss");
	}
	
	
	/**
	 * 获取calendar凌晨0:0:0的毫秒数
	 * @return
	 */
	public static long getDate00Time(Calendar calendar){
		
		if( calendar == null ){
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(GameSystemTime.getSystemMillTime());
		}
		calendar.set( Calendar.HOUR , 0);
		calendar.set( Calendar.HOUR_OF_DAY , 0);
		calendar.set( Calendar.MINUTE , 0);
		calendar.set( Calendar.SECOND , 0);
		calendar.set( Calendar.MILLISECOND , 0);
		
		return calendar.getTimeInMillis();
	}
	/**
	 * 获取date凌晨0:0:0的毫秒数
	 * @return
	 */
	public static long getDate00Time(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getDate00Time(cal);
	}
	/**
	 * 获取date凌晨0:0:0的毫秒数
	 * @return
	 */
	public static long getDate00Time(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(GameSystemTime.getSystemMillTime());
		return getDate00Time(calendar);
	}
	/**
	 * 格式yyyy-MM-dd HH:mm:ss转换为毫秒数
	 * @param timeStr
	 * @return
	 */
	public static long parseDateMillTime(String timeStr){
		
		if( timeStr == null || timeStr.trim().isEmpty() ){
			return 0;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try{
			Date date = dateFormat.parse(timeStr);
			return date.getTime();
		}catch (Exception e) {
			LOG.error("", e);
		}
		return 0;
	}
	/**
	 * 格式yyyy-MM-dd HH:mm:ss转换为毫秒数
	 * @param timeStr
	 * @param pattern 
	 * @return
	 */
	public static long parseDateMillTime(String timeStr,String pattern){
		
		if( timeStr == null || timeStr.trim().isEmpty() ){
			return 0;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		
		try{
			Date date = dateFormat.parse(timeStr);
			return date.getTime();
		}catch (Exception e) {
			LOG.error("", e);
		}
		return 0;
	}
	
	public static Date parseDate(String timeStr,String pattern){
		try{
			return new SimpleDateFormat(pattern).parse(timeStr);
		}catch (Exception e) {
			throw new RuntimeException("time parse error:"+timeStr);
		}
	}
	
	/**
	 * 在date时间上增加addSeconds秒
	 * @param date
	 * @param addSeconds
	 * @return
	 */
	public static long addSeconds(Date date, long addSeconds){
		long result = date.getTime();
		
		return result + addSeconds * 1000;
	}
	
	/**
	 * 在date时间上增加addMinutes秒
	 * @param date
	 * @param addSeconds
	 * @return
	 */
	public static long addMinutes(Date date, long addMinutes){
		long result = date.getTime();
		
		return result + addMinutes * 1000L*60;
	}
	/**
	 * 在date时间上增加addDays天
	 * @param date
	 * @param addDays
	 * @return
	 */
	public static Date addDays(Date date, Integer addDays){
		long result = date.getTime();
		result = result + addDays * 24L * 60 * 60 * 1000;
		return new Date(result);
	}
	
	/**
	 * 在time的时间上增加addHours小时
	 * @param time
	 * @param addHours
	 * @return
	 */
	public static Long addHours(Long time,int addHours){
		return time + addHours * 60L * 60 * 1000;
	}
	
	/**
	 * 在time的时间上增加addDays天
	 * @param time
	 * @param addDays
	 * @return
	 */
	public static Long addDays(Long time,int addDays){
		return time + addDays * 24L * 60 * 60 * 1000;
	}
	
	/**
	 * 比较2个时间相差的分钟<br>
	 * 格式 time2(20:30)-time1(21:30) = 60
	 * @param time1 
	 * @param time2
	 * @return
	 */
	public static int compare2TimesDiffMinutes(String time1, String time2){
		if( ObjectUtil.strIsEmpty(time1) || ObjectUtil.strIsEmpty(time2) ){
			throw new IllegalArgumentException();
		}
		String[] str1s = time1.split(":");
		String[] str2s = time2.split(":");
		
		int hour1 = Integer.valueOf(str1s[0]);
		int minute1 = Integer.valueOf(str1s[1]);
		int hour2 = Integer.valueOf(str2s[0]);
		int minute2 = Integer.valueOf(str2s[1]);
		
		return (hour2 - hour1) * 60 + (minute2 - minute1);
	}
	
	/**
	 * 下一月的1号的00:00
	 * @param date
	 * @return
	 */
	public static long getNextMonth00Time(long time) {
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTimeInMillis(time);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	
	
	/**
	 * 判断是否是同一周 (以每周周一为第一天)
	 * @param  是返回 time
	 * @return
	 */
	public static boolean tsToWeek(long time){
		Calendar calendarMonday = Calendar.getInstance();
		calendarMonday.setTimeInMillis(GameSystemTime.getSystemMillTime());

		// 获取firstDate在当前周的第几天. （星期一~星期日：1~7）
		int monday = calendarMonday.get(Calendar.DAY_OF_WEEK);

		long startTime = 0, endTime = 0;

		// 星期一开始时间
		calendarMonday.add(Calendar.DAY_OF_MONTH, -monday + 2);
		calendarMonday.set(Calendar.HOUR, 0);
		calendarMonday.set(Calendar.MINUTE, 0);
		calendarMonday.set(Calendar.SECOND, 0);

		startTime = calendarMonday.getTimeInMillis();

		calendarMonday.add(Calendar.DAY_OF_MONTH, 6);
		calendarMonday.set(Calendar.HOUR, 23);
		calendarMonday.set(Calendar.MINUTE, 59);
		calendarMonday.set(Calendar.SECOND, 59);

		endTime = calendarMonday.getTimeInMillis();
		
		return startTime <= time  && time <= endTime;
	}
	
	/**
	 * 判断当前时间是否在指定星期内
	 * @param week1 星期几
	 * @param week2	星期几
	 * @return
	 */
	public static boolean psToWeek(int week1,int week2){
		Calendar calendarMonday = Calendar.getInstance();
		calendarMonday.setTimeInMillis(GameSystemTime.getSystemMillTime());
		
		// 获取今天是星期几
		int monday = calendarMonday.get(Calendar.DAY_OF_WEEK) == 1 ? 7 :calendarMonday.get(Calendar.DAY_OF_WEEK) - 1 ;
		
		return week1<= monday && monday <= week2;
	}
	
	/**
	 * 以自定义的时间分隔点 判断是否是同一周
	 * @param timeStr （2|00:00 以每周2 00:00作为分隔点）
	 * @param time （需要判别的时间）
	 * @return 同一周 返回true
	 */
	public static boolean tsToWeek(String timeStr, long time) {
		long[] times = getTimes(timeStr, time);
		long now = GameSystemTime.getSystemMillTime();
		return now >= times[0] && now <= times[1];
	}
	
	
	
	/**
	 * 自定义时间分隔点 获取该分隔点的开启和结束时间
	 * @param sj （2|00:00 以每周2 00:00作为分隔点）
	 * @param time [startTime,endTime]
	 * @return
	 */
	public  static long[] getTimes(String sj,long time){
		// 2|00:00
		String[] str = sj.split("\\|");
		String[] hours = str[1].split(":");
		
		int configWeek = Integer.valueOf(str[0]) + 1, configHour = Integer.valueOf(hours[0]), configMinute = Integer.valueOf(hours[1]);
		if( configWeek >= 8){
			configWeek = 1;
		}
		
		long startTime = 0, endTime = 0;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int nowWeek = calendar.get(Calendar.DAY_OF_WEEK);

		
		String timeStr = formatTime(calendar.getTimeInMillis(), FORMART4);

		if (nowWeek > configWeek || (nowWeek == configWeek && compare(timeStr, str[1]))) {
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
		calendar.set(Calendar.DAY_OF_WEEK,configWeek);
		calendar.set(Calendar.HOUR_OF_DAY,configHour);
		calendar.set(Calendar.MINUTE,configMinute);
		calendar.set(Calendar.SECOND,0);

		endTime = calendar.getTimeInMillis();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(calendar.getTimeInMillis() - 7 * 24 * 60 * 60 * 1000 + 1000);
		startTime = calendar2.getTimeInMillis();
		
		return new long[] { startTime, endTime };
	}
	
	/**
	  * args1 > args2
	  * @param args1
	  * @param args2
	  * @return
	  */
	 private  static boolean compare(String args1,String args2){
		 String[] args1Array = args1.split(":");
		 String[] args2Array = args2.split(":");
		 
		 int i1 = Integer.valueOf(args1Array[0]);
		 int i2 = Integer.valueOf(args1Array[1]);
		 
		 int i3 = Integer.valueOf(args2Array[0]);
		 int i4 = Integer.valueOf(args2Array[1]);
		 
		 if( (i1 > i3) || (i1 == i3 && i2 >= i4)){
			 return true;
		 }
		 return false;
	 }

	/**
	 * 比较时间间隔(根据具体时间单位)
	 */
	public static int getMonthInterval(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		
		return Math.abs(c2.get(Calendar.YEAR)*12+c2.get(Calendar.MONTH) - c1.get(Calendar.YEAR)*12-c1.get(Calendar.MONTH));
	}
	
	/**
	 * 比较时间间隔(根据具体时间单位)
	 * <br> 不建议用，不准确，在当月中计算天数差，在当年中计算月数天数差可以用
	 */
	public static int getInterval(Date date1, Date date2, int calanderUnit) {
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		
		return Math.abs(c2.get(calanderUnit) - c1.get(calanderUnit));
	}
	
	
	public static String formatTime(long time1, String pattern){
		return new SimpleDateFormat(pattern).format(time1);
	}

	public static String formatTime(Date date, String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 判断当前时间是不是在24小时之内
	 * 
	 * @return 是,返回true
	 */
	public static boolean isNowTime24Hour(long t) {
		long t2 = 24 * 60 * 60 * 1000;
		return Math.abs(GameSystemTime.getSystemMillTime() - t) < t2;
	}
	
	 /**
     * day天后的24点毫秒
     * @author wind
     * @return
     */
    public static Long getCalaDay24Time(int day){
    	Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(GameSystemTime.getSystemMillTime());
    	d1.set(Calendar.HOUR_OF_DAY, 24);
    	d1.set(Calendar.MINUTE, 0);
    	d1.set(Calendar.SECOND, 0);
    	d1.set(Calendar.MILLISECOND, 0);
    	
    	d1.add(Calendar.DAY_OF_YEAR, day);
    	
    	return d1.getTimeInMillis();
    }
    
    public static String getDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return c.getTimeInMillis()+"";
	}
    
    /**
     * 获取今日周几（周日：1,周一:2）
     * @return
     */
    public static int getCurrentWeek(){
    	Calendar d1 = Calendar.getInstance();
    	return d1.get(Calendar.DAY_OF_WEEK);
    }
	
    /**
     * 获取当天指定时分秒的毫秒数
     * @param time
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Long getCalcTimeCurTime(long time,int hour,int minute,int second){
    	Calendar d1 = Calendar.getInstance();
    	d1.setTimeInMillis(time);
    	
    	d1.set(Calendar.HOUR_OF_DAY, hour);
    	d1.set(Calendar.MINUTE, minute);
    	d1.set(Calendar.SECOND, second);
    	d1.set(Calendar.MILLISECOND, 0);
    	
    	return d1.getTimeInMillis();
    	
    }
    
    public static Long getRankCurNextHourTime(){
    	Calendar d1 = Calendar.getInstance();
    	d1.set(Calendar.MINUTE, 0);
    	d1.set(Calendar.SECOND, 0);
    	d1.set(Calendar.MILLISECOND, 0);
    	
    	d1.add(Calendar.HOUR, 1);
    	d1.add(Calendar.MINUTE, 2);
    	
    	return d1.getTimeInMillis();
    }
    /**
     * 获取当前时间添加之后的时间
     * @param addDay 添加的天
     * @param hour 小时
     * @param minute 分
     * @return
     */
    public static Long getCalcTimeKuang(int addDay,int hour,int minute){
    	Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(GameSystemTime.getSystemMillTime());
    	
    	if(addDay > 0){
    		d1.add(Calendar.DAY_OF_YEAR, addDay);
    	}
    	d1.set(Calendar.HOUR_OF_DAY, hour);
    	d1.set(Calendar.MINUTE, minute);
    	d1.set(Calendar.SECOND, 0);
    	
    	return d1.getTimeInMillis();
    }
    
    /**
	 * 获取几个小时前的整小时数
	 * @param hour
	 * @return
	 */
	public static long getBeforeHourTimeStr(int hour){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(GameSystemTime.getSystemMillTime());
		cal.add(Calendar.HOUR_OF_DAY , -hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	/**
	 * 计算两个时间相差几周，与美国标准时间不同，这里以周一为每周的第一天
	 * 第一个时间戳 必须 小于 第二个时间戳
	 * @param beforeTimeStamp
	 * @param afterTimeStamp
	 * @return
	 */
	public static int weekInterval(long beforeTimeStamp,long afterTimeStamp){
		if(afterTimeStamp<beforeTimeStamp){
			throw new RuntimeException("param error:$2 must after $1");
		}
		if(afterTimeStamp == beforeTimeStamp){
			return 0;
		}
		Calendar beforeC = Calendar.getInstance();
		beforeC.setTime(new Date(beforeTimeStamp));
		beforeC.set(Calendar.HOUR_OF_DAY, 0);
		beforeC.set(Calendar.MINUTE, 0);
		beforeC.set(Calendar.SECOND, 0);
		beforeC.set(Calendar.MILLISECOND,0);
		if(beforeC.get(Calendar.DAY_OF_WEEK)==1){
			beforeC.add(Calendar.DAY_OF_WEEK, -1);
		}
		beforeC.set(Calendar.DAY_OF_WEEK, 1);
		
		Calendar afterC = Calendar.getInstance();
		afterC.setTime(new Date(afterTimeStamp));
		afterC.set(Calendar.HOUR_OF_DAY, 0);
		afterC.set(Calendar.MINUTE, 0);
		afterC.set(Calendar.SECOND, 0);
		afterC.set(Calendar.MILLISECOND,0);
		if(afterC.get(Calendar.DAY_OF_WEEK)==1){
			afterC.add(Calendar.DAY_OF_WEEK, -1);
		}
		afterC.set(Calendar.DAY_OF_WEEK, 1);
//		System.out.println(beforeC.getTime());
//		System.out.println(afterC.getTime());
		Long dayInterval = (afterC.getTimeInMillis()-beforeC.getTimeInMillis())/(1000*60*60*24);

		return dayInterval.intValue()/7;
	}
	
	
	public static long getNow(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 获取前30天的0点时间戳(Long)
	 * @param str
	 * @param nextCount
	 * @return
	 */
	public static Long getMonthDayLong() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(GameSystemTime.getSystemMillTime());
		cal.add( Calendar.DAY_OF_YEAR , -30);
		return getTime(cal,0, 0);
	}

	public static int getBetweenDay(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int d1 = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTimeInMillis(System.currentTimeMillis());
		int d2 = cal.get(Calendar.DAY_OF_YEAR);
		return d2 - d1 + 1;
	}
}
