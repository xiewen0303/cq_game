package com.junyou.utils.quartz;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import com.junyou.utils.datetime.DatetimeUtil;
/**
 *      字段	 	允许值	 	允许的特殊字符                                                 <br>
 *		秒	 	0-59	 	, - * /             			<br>
 *		分	 	0-59	 	, - * /							<br>
 *		小时	 	0-23	 	, - * /							<br>
 *		日期	 	1-31	 	, - * ? / L W C 				<br>
 *	          月份	 	1-12 或者 JAN-DEC	 	, - * /					<br>
 *		星期	 	1-7 或者 SUN-SAT	 	, - * ? / L C #			<br>	
 *		年（可选）	 	留空, 1970-2099	 	, - * /			<br>
 */
public class QuartzUtil {
	
	/**
	 * 获取quartz定时表达式
	 * @param str yyyy/MM/dd HH:mm:ss
	 * @return 秒 分 小时 日期 月份 年 
	 */
	public static String getQuartzStr(String str){
		
		return getQuartzStr(DatetimeUtil.parseDate(str, DatetimeUtil.FORMART7));
	}

	/**
	 * 
	 * @param str
	 *            HH:ss
	 * @return
	 */
	public static String getQuartzStr1(String str) {

		String quartzstr = "00 {0} {1} * * ?";
		String[] strArray = str.split(":");
		MessageFormat format = new MessageFormat(quartzstr);
		return format.format(new Object[] { strArray[1], strArray[0] });

	}
	
	/**
	 *  获取quartz定时表达式
	 * @param date
	 * @return 秒 分 小时 日期 月份 年
	 */
	public static String getQuartzStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return getQuartzStr(calendar);
	}
	
	/**
	 * 获取quartz定时表达式
	 * @param calendar
	 * @return 秒 分 小时 日期 月份 年 
	 */
	public static String getQuartzStr(Calendar calendar) {
		String quartzstr = "{0} {1} {2} {3} {4} {5}";
		MessageFormat format = new MessageFormat(quartzstr);
		return format.format(new Object[] { calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.YEAR)});
	}
}
