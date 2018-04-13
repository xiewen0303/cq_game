package com.junyou.bus.rfbactivity.configure.export;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 热发布运营活动-子活动
 */
public class ActivityConfigSon {
	private Integer id;//子活动ID
	private Long startTime;//开始日期（年月日）
	private Long endTime;//结束日期（年月日）
	private String subName;//子活动名字
	private String startHour;//开始时间（时分秒）
	private String endHour;//结束时间（时分秒）
	private int[] startTimeArr;//开始时间（0:时,1:分,2秒）
	private int[] endTimeArr;//结束时间（0:时,1:分,2秒）
	private int startDay;//开始天数
	private int endDay;//结束天数
	private Integer timeType;//活动的时间类型
	private Integer subActivityType;//子活动类型
	private Integer order;//排序
	private Integer skin;//子活动显示样式
	private boolean isDel;//是否删除标识
	private String skey;//客户端需要活动的 特殊key服务器只传过去的
	private int clientVersion = 1;//客户端版本号
	private Integer activityId;//主活动Id
	private Integer ascription;//活动所属
	private int cycleDays;//活动循环天数  (间隔天数)
	private int xhEndDay;//结束循环天数 （0为永久循环，其他为活动结束天数）
	
	private int xyEndDay;  //当前循环结束天数

	public int getXyEndDay() {
		return xyEndDay;
	}

	public void setXyEndDay(int xyEndDay) {
		this.xyEndDay = xyEndDay;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	/**
	 * 是否在子活动时间内（时间类型为0的情况下，读取startTime和endTime，其他的情况下读取getsTime和geteTime）
	 * @return true:在活动期间内
	 */
	private boolean inTime2Sub(boolean needChangeNotify){
		Long startTime = null;
		Long endTime = null;
		switch (timeType) {
		case ActivityTimeType.TIME_0_SJ:
			startTime = this.getStartTime();
			endTime = this.getEndTime();
			break;

		default:
			if(needChangeNotify){
				
				startTime = getsTime();
				endTime = geteTime();
			}
			break;
		}
		
		if(startTime != null && endTime != null){
			return GameSystemTime.getSystemMillTime() >= startTime && GameSystemTime.getSystemMillTime() <= endTime;
		}
		return false;
	}
	
	
	/**
	 * 获取子活动结束时间
	 * @return
	 */
	public Long getEndTimeByMillSecond(){
		Long endTime = getEndTime();
		
		switch (timeType) {
		case ActivityTimeType.TIME_0_SJ:
			
			return getEndTime();
		case ActivityTimeType.TIME_1_WEEK:
			
			int eDay = this.getEndDay();
			//周
			if(!DatetimeUtil.psToWeek(this.getStartDay(), this.getEndDay())){
				eDay = eDay + 7;
			}
			return getWeekDayTime(eDay, endTimeArr);
		case ActivityTimeType.TIME_2_KAI_FU:
			
			//-1表示永久 和客户端协商直接给他们-1
			if(this.getEndDay() == -1){
				return DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
			}else{
				//开服天数（该活动是在未开始）
				Long openTime = ServerInfoServiceManager.getInstance().getServerStartTime().getTime();
				openTime = DatetimeUtil.getCalcTimeCurTime(openTime, endTimeArr[0], endTimeArr[1], endTimeArr[2]);
				endTime = DatetimeUtil.addDays(openTime, this.getEndDay() - 1);
			}
			
			return endTime;
		case ActivityTimeType.TIME_3_HE_FU:
			if(ServerInfoServiceManager.getInstance().getServerHefuTimes() < 1 ){
				return 0l;
			}
			
			//-1表示永久 和客户端协商直接给他们-1
			if(this.getEndDay() == -1){
				return DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
			}else{
				//合服天数（该活动是在未开始）
				Long heFuTime = ServerInfoServiceManager.getInstance().getServerHefuTime().getTime();
				heFuTime = DatetimeUtil.getCalcTimeCurTime(heFuTime, endTimeArr[0], endTimeArr[1], endTimeArr[2]);
				endTime = DatetimeUtil.addDays(heFuTime, this.getEndDay() - 1);
			}
			
			return endTime;
		case ActivityTimeType.TIME_4_KAI_FU_LOOP:
			
			int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			//-1表示永久 和客户端协商直接给他们-1
			if(this.getEndDay() == -1){
				return DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
			}else if(this.getEndDay() > kfDays){
				//结束开服天数在区间内，是配置的时分秒
				//endTime = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
				Timestamp kfTime = ServerInfoServiceManager.getInstance().getServerStartTime();
				endTime = getActivityTime(kfTime, this.getEndDay(),this.endTimeArr);
			}else if(this.getEndDay() == kfDays){
				//结束开服天数是当天，是配置的时分秒
				endTime = geteTime();
			}else{
				//结束天服天数，过了计算过期那天的配置时间
				/*int day = this.getStartDay() - kfDays;//这个值应该是一个负数
				long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), day);
				endTime = DatetimeUtil.getCalcTimeCurTime(tmp, endTimeArr[0], endTimeArr[1], endTimeArr[2]);*/
				int end = this.getXyEndDay();
				Timestamp kfTime = ServerInfoServiceManager.getInstance().getServerStartTime();
				endTime = getActivityTime(kfTime, end,this.endTimeArr);
			}
			
			return endTime;
		case ActivityTimeType.TIME_5_HE_FU_LOOP:
			
			int hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			//-1表示永久 和客户端协商直接给他们-1
			if(this.getEndDay() == -1){
				return DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
			}else if(this.getEndDay() > hfDays){
				//结束开服天数在区间内，是配置的时分秒
				endTime = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
			}else if(this.getEndDay() == hfDays){
				//结束开服天数是当天，是配置的时分秒
				endTime = geteTime();
			}else{
				//结束天服天数，过了计算过期那天的配置时间
				/*int day = this.getStartDay() - hfDays;//这个值应该是一个负数
				long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), day);
				endTime = DatetimeUtil.getCalcTimeCurTime(tmp, endTimeArr[0], endTimeArr[1], endTimeArr[2]);*/
				int end = this.getXyEndDay();
				Timestamp hfTime = ServerInfoServiceManager.getInstance().getServerHefuTime();
				endTime = getActivityTime(hfTime, end,this.endTimeArr);
			}
			
			return endTime;
		default:
			return geteTime();
		}
	}
	
	/**
	 * 根据时间和间隔天数  获取时间戳
	 * @param kfTime
	 * @param cycleDays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private long getActivityTime(Timestamp kfTime,int cycleDays,int[] timeArr){
		try {
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//开服那天0点的时间戳
			long starTime = sim.parse((kfTime.getYear()+1900)+"-"+(kfTime.getMonth()+1)+"-"+kfTime.getDate()+" "+timeArr[0]+":"+timeArr[1]+":"+timeArr[2]).getTime();//开始时间
			long time = (cycleDays-1L) * 24 * 60 * 60 * 1000l;
			return starTime + time;
		} catch (ParseException e) {
			ChuanQiLog.error("",e);
		}
		return 0;
	}
	
	/**
	 * 获取子活动开始时间
	 * @return
	 */
	public Long getStartTimeByMillSecond(){
		Long startTime = getStartTime();
		
		switch (timeType) {
		case ActivityTimeType.TIME_0_SJ:
			
			//3	start	Number
			return getStartTime();
		case ActivityTimeType.TIME_1_WEEK:
			
			int sDay = this.getStartDay();
			//周
			if(!DatetimeUtil.psToWeek(this.getStartDay(), this.getEndDay())){
				sDay = sDay + 7;
			}
			//3	start	Number
			return getWeekDayTime(sDay, startTimeArr);
		case ActivityTimeType.TIME_2_KAI_FU:
			
			//开服天数（该活动是在未开始）
			startTime = ServerInfoServiceManager.getInstance().getServerStartTime().getTime();
			if(this.getStartDay() > 0){
				startTime = DatetimeUtil.addDays(startTime,this.getStartDay() - 1);
			}
			startTime = DatetimeUtil.getCalcTimeCurTime(startTime, startTimeArr[0], startTimeArr[1], startTimeArr[2]);
			
			return startTime;
		case ActivityTimeType.TIME_3_HE_FU:
			
			//合服天数（该活动是在未开始）
			startTime = ServerInfoServiceManager.getInstance().getServerHefuTime().getTime();
			if(this.getStartDay() > 0){
				startTime = DatetimeUtil.addDays(startTime,this.getStartDay() - 1);
			}
			startTime = DatetimeUtil.getCalcTimeCurTime(startTime, startTimeArr[0], startTimeArr[1], startTimeArr[2]);
			
			return startTime;
		case ActivityTimeType.TIME_4_KAI_FU_LOOP:
			//开始时间
			int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			if(kfDays > this.getStartDay()){
				int start = this.getStartDay();
				this.setXyEndDay(this.getEndDay());
				for (int i = 0; i < 10000; i++) {
					if(xhEndDay > 0 && (start + cycleDays) > xhEndDay){
						break;
					}
					if(kfDays >= (start + cycleDays)){
						start += cycleDays;
						this.setXyEndDay(this.getXyEndDay()+cycleDays);
					}else{
						break;
					}
				}
				Timestamp kfTime = ServerInfoServiceManager.getInstance().getServerStartTime();
				startTime = getActivityTime(kfTime, start,this.startTimeArr);
				//开服天数在区间内，是每天0点0分0秒开始
				//startTime = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 0, 0, 0);
			}else if(kfDays == this.getStartDay()){
				//开服天数是当天，是配置的时分秒
				startTime = getsTime();
			}else{
				//还没到开始的开服天数，计算到那天的时分秒
				long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), this.getStartDay() - kfDays);
				startTime = DatetimeUtil.getCalcTimeCurTime(tmp, startTimeArr[0], startTimeArr[1], startTimeArr[2]);
			}
			return startTime;
		case ActivityTimeType.TIME_5_HE_FU_LOOP:
		/*	
			int hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			if(hfDays == 0){
				//容错-一定是配置错了
				return getsTime();
			}else{
				if(hfDays > this.getStartDay()){
					//合服天数在区间内，是每天0点0分0秒开始
					startTime = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 0, 0, 0);
				}else if(hfDays == this.getStartDay()){
					//合服天数是当天，是配置的时分秒
					startTime = getsTime();
				}else{
					//还没到开始的合服天数，计算到那天的时分秒
					long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), this.getStartDay() - hfDays);
					startTime = DatetimeUtil.getCalcTimeCurTime(tmp, startTimeArr[0], startTimeArr[1], startTimeArr[2]);
				}
			}
			
			return startTime;	*/	
			//开始时间
			int hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			if(hfDays > this.getStartDay()){
				int start = this.getStartDay();
				this.setXyEndDay(this.getEndDay());
				for (int i = 0; i < 10000; i++) {
					if(xhEndDay > 0 && (start + cycleDays) > xhEndDay){
						break;
					}
					if(hfDays >= (start + cycleDays)){
						start += cycleDays;
						this.setXyEndDay(this.getXyEndDay()+cycleDays);
					}else{
						break;
					}
				}
				Timestamp kfTime = ServerInfoServiceManager.getInstance().getServerHefuTime();
				startTime = getActivityTime(kfTime, start,this.startTimeArr);
				//开服天数在区间内，是每天0点0分0秒开始
				//startTime = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 0, 0, 0);
			}else if(hfDays == this.getStartDay()){
				//开服天数是当天，是配置的时分秒
				startTime = getsTime();
			}else{
				//还没到开始的开服天数，计算到那天的时分秒
				long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), this.getStartDay() - hfDays);
				startTime = DatetimeUtil.getCalcTimeCurTime(tmp, startTimeArr[0], startTimeArr[1], startTimeArr[2]);
			}
			return startTime;
		default:
			return getsTime();
		}
	}
	
	/**
	 * 判断当前活动是否已经结束（异常中断活动除外，后台删除或取消）
	 * @return true:结束
	 */
	public boolean isEndTime(){
		//是否删除
		if(isDel){
			return false;
		}
		
		switch (timeType) {
		case ActivityTimeType.TIME_1_WEEK:
			
			int eDay = this.getEndDay();
			//周
			if(!DatetimeUtil.psToWeek(this.getStartDay(), this.getEndDay())){
				eDay = eDay + 7;
			}
			return GameSystemTime.getSystemMillTime() >= getWeekDayTime(eDay, endTimeArr);
		case ActivityTimeType.TIME_2_KAI_FU:
			//开服天数（该活动是在未开始）
			int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			if(this.getEndDay() != -1 && (this.getEndDay() < kfDays || (this.getEndDay() == kfDays && GameSystemTime.getSystemMillTime() >= geteTime() ))){
				return true;
			}
			break;
		case ActivityTimeType.TIME_3_HE_FU:
			//合服天数（该活动是在未开始）
			int hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			if(this.getEndDay() != -1 && (this.getEndDay() < hfDays || (this.getEndDay() == hfDays && GameSystemTime.getSystemMillTime() >= geteTime() ))){
				return true;
			}
			break;
		case ActivityTimeType.TIME_4_KAI_FU_LOOP:
			//开服天数（该活动是在未开始）
			kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			if(this.getEndDay() != -1 && (this.getEndDay() < kfDays || (this.getEndDay() == kfDays && GameSystemTime.getSystemMillTime() >= geteTime() ))){
				return true;
			}
			break;
		case ActivityTimeType.TIME_5_HE_FU_LOOP:
			//合服天数（该活动是在未开始）
			hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			if(this.getEndDay() != -1 && (this.getEndDay() < hfDays || (this.getEndDay() == hfDays && GameSystemTime.getSystemMillTime() >= geteTime() ))){
				return true;
			}
			break;

		default:
			break;
		}
		
		
		return false;
	}
	
	/**
	 * 获取开始结束时间
	 * @return
	 */
	private Long[] getStartEndTime(int kfDays){
		Long[] time = new Long[2];
		Long s = 0l;
		Long e = 0l;
		if(kfDays > this.getStartDay()){
			int start = this.getStartDay();
			this.setXyEndDay(this.getEndDay());
			for (int i = 0; i < 10000; i++) {
				if(xhEndDay > 0 && (start + cycleDays) > xhEndDay){
					break;
				}
				if(kfDays >= (start + cycleDays)){
					start += cycleDays;
					this.setXyEndDay(this.getXyEndDay()+cycleDays);
				}else{
					break;
				}
			}
			Timestamp kfTime = ServerInfoServiceManager.getInstance().getServerStartTime();
			s = getActivityTime(kfTime, start,this.startTimeArr);
			int end = this.getXyEndDay();
			e = getActivityTime(kfTime, end,this.endTimeArr);
		}
		time[0] = s;
		time[1] = e;
		
		return time;
		
	}
	
	/**
	 * 是否是在运行中的子活动，相对于本服
	 * @param kfDay
	 * @param hfDay
	 * @return true:正在运行中，false:已下架
	 */
	public boolean isRunActivity(){
		//是否需要通知
		boolean needChangeNotify = false;
		
		ReFaBuGxConfig gxConfug = ReFaBuGxConfigExportService.getInstance().getReFaBuGxConfig(this.getActivityId());
		//如果不在关系配置的开服多少天后开启
		int serverDay = ServerInfoServiceManager.getInstance().getKaifuDays();
		if(gxConfug != null){
			if(!gxConfug.isServerDays(serverDay)){
				return false;
			}
		}
		
		switch (timeType) {
		case ActivityTimeType.TIME_1_WEEK:
			if(DatetimeUtil.psToWeek(this.getStartDay(), this.getEndDay())){
				needChangeNotify = true;
			}
			
			break;
			
		case ActivityTimeType.TIME_2_KAI_FU:
			//开服天数
			int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			//activityConfig.getEndDay()为-1时 代表永久
			if(this.getStartDay() <= kfDays && (this.getEndDay() == -1 || kfDays <= this.getEndDay())){
				needChangeNotify = true;
			}
			break;
			
		case ActivityTimeType.TIME_3_HE_FU:
			//合服天数
			int hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			//activityConfig.getEndDay()为-1时 代表永久
			if(hfDays != 0 && this.getStartDay() <= hfDays && (this.getEndDay() == -1 || hfDays <= this.getEndDay())){
				needChangeNotify = true;
			}
			break;
			
		case ActivityTimeType.TIME_4_KAI_FU_LOOP:
			//开服天数
			kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			//activityConfig.getEndDay()为-1时 代表永久
			if(this.getStartDay() <= kfDays && (this.getEndDay() == -1 || kfDays <= this.getEndDay())){
				needChangeNotify = true;
			}else{
				Long[] time = getStartEndTime(kfDays);//活动开始结束时间
				long d = System.currentTimeMillis();
				if(d >= time[0] && d <= time[1]){
					needChangeNotify = true;
				}
				
				
			}
			break;
			
		case ActivityTimeType.TIME_5_HE_FU_LOOP:
			//合服天数
			hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			//activityConfig.getEndDay()为-1时 代表永久
			if(hfDays != 0 && this.getStartDay() <= hfDays && (this.getEndDay() == -1 || hfDays <= this.getEndDay())){
				needChangeNotify = true;
			}else{
				Long[] time = getStartEndTime(hfDays);//活动开始结束时间
				long d = System.currentTimeMillis();
				if(d >= time[0] && d <= time[1]){
					needChangeNotify = true;
				}
				
				
			}
			break;
			
		default:
			break;
		}
		
		//具体时间
		if(inTime2Sub(needChangeNotify)){
			needChangeNotify = true;
		}else{
			needChangeNotify = false;
		}
		
		return needChangeNotify;
	}
	
	
	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}



	//子活动数据
	private Object[] subData = new Object[10];
	
	/**
	 * 获取客户端需要的子活动数据<br/>
	 	0	id	int	子活动全局标识（在不同主活动中的子活动标识也不同)<br/>
		1	name	String	子活动名称<br/>
		2	type	int	子活动的活动类型<br/>
		3	start	Number	开始时间时间戳（服务端计算好）<br/>
		4	end	Number	结束时间时间戳（服务端计算好）<br/>
		5	order	int	排序（按从小到大顺序排序)<br/>
		6	limit	Map	活动显示限制 #HDLimit，如果无任何限制，此值为null<br/>
		7	skin	int	子活动显示样式(如果前端找不到指定样式，会使用样式0)<br/>
		8	key	String	用于处理运营特殊需求，如引导要求打开指定的活动面板之类
		9	ascription int 1-开服活动，2-精彩活动,3-节日活动，4-全服排行，5-特殊活动 
	 * 
	 * @return
	 */
	public Object[] getSubMsg(){
		
		setActivityTime();
		
		return subData;
	}
	
	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
		
		//8	key	String	用于处理运营特殊需求，如引导要求打开指定的活动面板之类
		subData[8] = skey;
	}
	
	/**
	 * 根据时间类型改开始和结束时间
	 */
	private void setActivityTime(){
		switch (timeType) {
		
		case ActivityTimeType.TIME_0_SJ:
			//3	start	Number
			subData[3] = getStartTime();
			//4	end	Number
			subData[4] = getEndTime();
			break;
			
		case ActivityTimeType.TIME_1_WEEK:
			//周
			//3	start	Number
			subData[3] = getStartTimeByMillSecond();
			//4	end	Number
			subData[4] = getEndTimeByMillSecond();
			break;
			
		case ActivityTimeType.TIME_2_KAI_FU:
			//开服天数
			
			//3	start	Number
			subData[3] = getStartTimeByMillSecond();
			//4	end	Number
			subData[4] = getEndTimeByMillSecond();
			break;
		case ActivityTimeType.TIME_3_HE_FU:
			//合服天数
			
			//3	start	Number
			subData[3] = getStartTimeByMillSecond();
			//4	end	Number
			subData[4] = getEndTimeByMillSecond();
			break;
		case ActivityTimeType.TIME_4_KAI_FU_LOOP:
			//开服天数
			
			//3	start	Number
			subData[3] = getStartTimeByMillSecond();
			//4	end	Number
			subData[4] = getEndTimeByMillSecond();
			break;
		case ActivityTimeType.TIME_5_HE_FU_LOOP:
			//合服天数
			
			//3	start	Number
			subData[3] = getStartTimeByMillSecond();
			//4	end	Number
			subData[4] = getEndTimeByMillSecond();
			break;
			
		default:
			//3	start	Number
			subData[3] = getsTime();
			//4	end	Number
			subData[4] = geteTime();
			break;
		}
	}
	/**
	 * 获取周的开始时间和结束时间
	 * @param day 开始天或结束天
	 * @return
	 */
	private Long getWeekDayTime(int day, int[] time){
		int curWeek = DatetimeUtil.getCurWeek();
		int d = day - curWeek;
		return DatetimeUtil.getCalaDayTime(d, time[0], time[1], time[2]);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
		
		//0	id	int	子活动全局标识（在不同主活动中的子活动标识也不同)
		subData[0] = id;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
		
		//1	name	String	子活动名称<br/>
		subData[1] = subName;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		if(timeType > 0){//时间类型为0，不解
			
			this.startHour = startHour;
			
			String[] tmpArr = startHour.split(":");
			startTimeArr = new int[]{Integer.parseInt(tmpArr[0]),Integer.parseInt(tmpArr[1]),Integer.parseInt(tmpArr[2])};
		}
	}
	public String getEndHour() {
		return endHour;
	}
	public void setEndHour(String endHour) {
		if(timeType > 0){//时间类型为0，不解
			
			this.endHour = endHour;
			
			String[] tmpArr = endHour.split(":");
			endTimeArr = new int[]{Integer.parseInt(tmpArr[0]),Integer.parseInt(tmpArr[1]),Integer.parseInt(tmpArr[2])};
		}
	}
	public Integer getSubActivityType() {
		return subActivityType;
	}
	public void setSubActivityType(Integer subActivityType) {
		this.subActivityType = subActivityType;
		
		//2	type	int	子活动的活动类型<br/>
		subData[2] = subActivityType;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
		
		//5	order	int	排序（按从小到大顺序排序)<br/>
		subData[5] = order;
	}
	public Integer getSkin() {
		return skin;
	}
	public void setSkin(Integer skin) {
		this.skin = skin;
		
		//7	skin	int	子活动显示样式(如果前端找不到指定样式，会使用样式0)<br/>
		subData[7] = skin;
	}


	public int getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(int clientVersion) {
		this.clientVersion = clientVersion;
	}

	/**
	 * 当天开始时间(相对时间，每天都会变)
	 * @return
	 */
	public Long getsTime() {
		return DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), startTimeArr[0], startTimeArr[1], startTimeArr[2]);
	}
	
	/**
	 * 当天结束时间(相对时间，每天都会变)
	 * @return
	 */
	public Long geteTime() {
		return  DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), endTimeArr[0], endTimeArr[1], endTimeArr[2]);
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}
	public int getCycleDays() {
		return cycleDays;
	}

	public void setCycleDays(int cycleDays) {
		this.cycleDays = cycleDays;
	}

	public int getXhEndDay() {
		return xhEndDay;
	}

	public void setXhEndDay(int xhEndDay) {
		this.xhEndDay = xhEndDay;
	}

	public Integer getAscription() {
		return ascription;
	}

	public void setAscription(Integer ascription) {
		this.ascription = ascription;
		subData[9] = ascription;
	}
	
}
