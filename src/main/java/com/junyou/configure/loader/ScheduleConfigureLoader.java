package com.junyou.configure.loader;

import java.util.ArrayList;
import java.util.List;

import com.junyou.gameconfig.export.AbsRefreshAbleConfigureInit;

/**
 * @description 定时配置读取
 * @author ShiJie Chi
 * @date 2012-5-10 上午10:04:14 
 */
public class ScheduleConfigureLoader {
	
	private List<AbsRefreshAbleConfigureInit> scheduleConfigures = new ArrayList<AbsRefreshAbleConfigureInit>();
	
	/**
	 * 设置需定时刷新配置
	 */
	public void setScheduleConfigures(List<AbsRefreshAbleConfigureInit> scheduleConfigures) {
		this.scheduleConfigures = scheduleConfigures;
	}



	/**
	 * 执行load
	 */
	public void load(){
		
		for(AbsRefreshAbleConfigureInit absConfigureInit : scheduleConfigures){
			
			absConfigureInit.versionRefresh();
		}
	}
	
}
