package com.junyou.bus.rfbactivity.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 热发布运营活动-主活动
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-5-4 下午7:49:26
 */
public class ActivityConfig {
	private Integer id;//主活动ID
	private Long startTime;//开始日期（年月日）
	private Long endTime;//结束日期（年月日）
	private int startDay;//开始天数
	private int endDay;//结束天数
	private String name;//主活动名字
	private String icon;//主活动图标地址
	private Integer model;//前端显示模版
	private Integer order;//排序
	private Integer display;//提前多久显示活动图标（分）
	private String startHour;//开始时间（时分秒）
	private int[] startTimeArr;//开始时间（0:时,1:分,2秒）
	private String endHour;//结束时间（时分秒）
	private int[] endTimeArr;//结束时间（0:时,1:分,2秒）
	private Integer delay;//活动需要服务器开启多少天后开启
	private int timeType;//活动的时间类型
	private String skey;//客户端需要活动的 特殊key服务器只传过去的
	private Map<Integer, ActivityConfigSon> zihuodongMap;//子活动map  子活动Id = 子活动内容
	private Integer folder;//全局ID
	private boolean isDel = false;
	
	private String md5Sign;//服务器端文件md5值
	private int clientVersion = 1;//和客户端通信版本号
	
	/**
	 * 是否是标识删除
	 * @return
	 */
	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}
	
	/**
	 * 是否在主活动时间内（天）
	 * @return
	 */
	private boolean inTime2Main(){
		return (GameSystemTime.getSystemMillTime() >= this.getStartTime() || DatetimeUtil.dayIsToday(this.getStartTime())) && GameSystemTime.getSystemMillTime() <= this.getEndTime();
	}

	/**
	 * 是否是在运行中的主活动，相对于本服
	 * @return true:正在运行中，false:已下架
	 */
	public boolean isRunActivity(){
		//是否需要通知
		boolean needChangeNotify = false;
		switch (timeType) {
		
		case ActivityTimeType.TIME_0_SJ:
			if(inTime2Main()){
				needChangeNotify = true;
			}
			
			break;
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
			}
			break;
			
		case ActivityTimeType.TIME_5_HE_FU_LOOP:
			//合服天数
			hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			//activityConfig.getEndDay()为-1时 代表永久
			if(hfDays != 0 && this.getStartDay() <= hfDays && (this.getEndDay() == -1 || hfDays <= this.getEndDay())){
				needChangeNotify = true;
			}
			break;
			
		default:
			break;
		}
		
		return needChangeNotify;
	}
	
	//主活动 MainHD Array
	private Object[] mainData = new Object[13];
	/**
	 * 获取主活动 MainHD数据<br/>
	  		0	id	int	活动全局标识<br/>
			1	icon	String	活动图标<br/>
			2	name	String	活动名称<br/>
			3	start	Number	开始时间时间戳(服务端计算好)<br/>
			4	end	Number	结束时间时间戳(服务端计算好)<br/>
			5	show	int	单位：分钟 在活动开始前多少分钟，提前展示图标，并显示一个倒计时<br/>
			6	limit	Map	活动显示限制 #HDLimit，如果无任何限制，此值为null<br/>
			7	model	int	前端显示模板<br/>
			8	order	int	运营活动图标的显示优先级，根据大小调整图标在特定排的显示顺序(项目自行定义是越大越靠左，还是越小越靠左)<br/>
			9	key	String	用于处理运营特殊需求，如引导要求打开指定的活动面板之类<br/>
	 * @return
	 */
	public Object[] getMainHdData(){
		setActivityTime();

		setChildVo();
		return mainData;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
		
		//9	key	String	用于处理运营特殊需求，如引导要求打开指定的活动面板之类<br/>
		mainData[9] = skey;
	}
	
	/**
	 * 根据时间类型改开始和结束时间
	 * @return Object[开始时间戳，结束时间戳]
	 */
	public Object[] setActivityTime(){

		switch (timeType) {

		case ActivityTimeType.TIME_0_SJ:
			//3	start	Number
			mainData[3] = getStartTime();
			//4	end	Number
			mainData[4] = getEndTime();
			break;
			
		case ActivityTimeType.TIME_4_KAI_FU_LOOP:
			
			//开始时间
			int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			if(kfDays > this.getStartDay()){
				//开服天数在区间内，是每天0点0分0秒开始
				mainData[3] = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 0, 0, 0);
			}else if(kfDays == this.getStartDay()){
				//开服天数是当天，是配置的时分秒
				mainData[3] = getsTime();
			}else{
				//还没到开始的开服天数，计算到那天的时分秒
				long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), this.getStartDay() - kfDays);
				mainData[3] = DatetimeUtil.getCalcTimeCurTime(tmp, startTimeArr[0], startTimeArr[1], startTimeArr[2]);
			}
			
			
			//结束时间
			if(this.getEndDay() == -1){
				//结束开服时间是永久
				mainData[4] = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
			}else if(this.getEndDay() > kfDays){
				//结束开服天数在区间内，是23时59分59秒
				mainData[4] = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
			}else if(this.getEndDay() == kfDays){
				//结束开服天数是当天，是配置的时分秒
				mainData[4] = geteTime();
			}else{
				//结束天服天数，过了计算过期那天的配置时间
				int day = this.getStartDay() - kfDays;//这个值应该是一个负数
				long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), day);
				mainData[4] = DatetimeUtil.getCalcTimeCurTime(tmp, endTimeArr[0], endTimeArr[1], endTimeArr[2]);
			}
			
			break;
		case ActivityTimeType.TIME_5_HE_FU_LOOP:
			
			//开始时间
			int hfDays = ServerInfoServiceManager.getInstance().getHefuDays();
			if(hfDays == 0){
				//容错-一定是配置错了
				mainData[3] = getsTime();
				mainData[4] = geteTime();
				break;
			}else{
				if(hfDays > this.getStartDay()){
					//合服天数在区间内，是每天0点0分0秒开始
					mainData[3] = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 0, 0, 0);
				}else if(hfDays == this.getStartDay()){
					//合服天数是当天，是配置的时分秒
					mainData[3] = getsTime();
				}else{
					//还没到开始的合服天数，计算到那天的时分秒
					long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), this.getStartDay() - hfDays);
					mainData[3] = DatetimeUtil.getCalcTimeCurTime(tmp, startTimeArr[0], startTimeArr[1], startTimeArr[2]);
				}
				
				
				//结束时间
				if(this.getEndDay() == -1){
					//结束合服时间是永久
					mainData[4] = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
				}else if(this.getEndDay() > hfDays){
					//结束合服天数在区间内
					mainData[4] = DatetimeUtil.getCalcTimeCurTime(GameSystemTime.getSystemMillTime(), 23, 59, 59);
				}else if(this.getEndDay() == hfDays){
					//结束合服天数是当天，是配置的时分秒
					mainData[4] = geteTime();
				}else{
					//结束合服天数，过了计算过期那天的配置时间
					int day = this.getStartDay() - hfDays;//这个值应该是一个负数
					long tmp = DatetimeUtil.addDays(GameSystemTime.getSystemMillTime(), day);
					mainData[4] = DatetimeUtil.getCalcTimeCurTime(tmp, endTimeArr[0], endTimeArr[1], endTimeArr[2]);
				}
			}
			
			break;
		default:
			//3	start	Number
			mainData[3] = getsTime();
			//4	end	Number
			mainData[4] = geteTime();
			break;
		}
		
		return new Object[]{mainData[3], mainData[4]};
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

	/**
	 * 客户端版本自动加1
	 */
	public void changeClientVersion() {
		this.clientVersion++;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
		
		//0	id	int	活动全局标识
		mainData[0] = id;
	}
	
	public String getMd5Sign() {
		return md5Sign;
	}
	public void setMd5Sign(String md5Sign) {
		this.md5Sign = md5Sign;
	}
	public int getClientVersion() {
		return clientVersion;
	}
	public void setClientVersion(int clientVersion) {
		this.clientVersion = clientVersion;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		
		//2	name	String	活动名称
		mainData[2] = name;
	}
	public Integer getModel() {
		return model;
	}
	public void setModel(Integer model) {
		this.model = model;
		
		//7	model	int	前端显示模板
		mainData[7] = model;
	}
	public Integer getDisplay() {
		return display;
	}
	public void setDisplay(Integer display) {
		this.display = display;
		
		//5	show	int	单位：分钟 在活动开始前多少分钟，提前展示图标，并显示一个倒计时
		mainData[5] = display;
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
	public Integer getDelay() {
		return delay;
	}
	
	//limit map
	Map<String, Integer> limitMap = new HashMap<>();
	
	public void setDelay(Integer delay) {
		this.delay = delay;
		
		//limit
//		limitMap.put("delay", delay);
//		mainData[6] = limitMap;
		//暂时还没实现
		mainData[6] = null;
	}
	public int getTimeType() {
		return timeType;
	}
	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}
	public Map<Integer, ActivityConfigSon> getZihuodongMap() {
		return zihuodongMap;
	}
	public void setZihuodongMap(Map<Integer, ActivityConfigSon> zihuodongMap) {
		this.zihuodongMap = zihuodongMap;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
		
		//1	icon	String	活动图标
		mainData[1] = icon;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
		
		//8	order	int
		mainData[8] = order;
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

	public Integer getFolder() {
		return folder;
	}

	public void setFolder(Integer folder) {
		this.folder = folder;
		
		mainData[11] = folder;
	}
	/**
	 * 设置子活动信息
	 */
	private void setChildVo(){
		if(zihuodongMap == null){
			ChuanQiLog.debug("no child activity,MainId:"+this.getId());
			return;
		}

		List<Object[]> childs = new ArrayList<>();
		for (ActivityConfigSon entry : zihuodongMap.values()) {
            if(entry.isDel()){
                //子活动删除
                continue;
            }


            List<Object> child = new ArrayList<>();
            child.add(entry.getId());
            child.add(entry.getStartTimeByMillSecond());
            child.add(entry.getEndTimeByMillSecond());
            child.add(entry.getAscription());
            child.add(entry.getSkey());
            
            IRfbConfigTemplateService rfbConfigService = RfbConfigTemplateFacotry.getRfbConfigTemplateService(entry.getSubActivityType());
            String childData = rfbConfigService == null ? "" : CovertObjectUtil.object2String(rfbConfigService.getChildData(entry.getId()));
            child.add(childData);

            //只要主活动在活动期间之内则子活动都推送
            childs.add(child.toArray());

			mainData[12] =childs.toArray();
		}
	}
	
}
