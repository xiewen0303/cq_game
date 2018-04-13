package com.junyou.configure.schedule;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.AbsRefreshAbleConfigureParser;


/**
 * @description 定时配置读取
 * @author ShiJie Chi
 * @date 2012-5-10 上午10:04:14 
 */
@Component
public class RefreshableConfigureScheduler {
	
	private List<AbsRefreshAbleConfigureParser> scheduleConfigures = new ArrayList<AbsRefreshAbleConfigureParser>();
	
	/**
	 * 设置需定时刷新配置
	 */
	public void setScheduleConfigures(List<AbsRefreshAbleConfigureParser> scheduleConfigures) {
		this.scheduleConfigures = scheduleConfigures;
	}
	
	/**
	 * 动态增加需定时刷新配置解析器
	 */
	public void addRefreshableParser(AbsRefreshAbleConfigureParser parser){
		scheduleConfigures.add(parser);
	}


	/**
	 * 执行load
	 */
	public void refreshAll(){
		//定时业务放到新线程中处理，以免影响其他的定时业务逻辑
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(AbsRefreshAbleConfigureParser absConfigureInit : scheduleConfigures){
					
					absConfigureInit.versionRefresh();
				}
			}
		},"Refreshable-Configure-Thread").start();
		
	}
	
	@PostConstruct
	public void init(){
		
		//每分钟
//		quartzScheduleExportService.schedule(this, "refreshAll", null, "0 */1 * * * ?");
		
	}
	
}
