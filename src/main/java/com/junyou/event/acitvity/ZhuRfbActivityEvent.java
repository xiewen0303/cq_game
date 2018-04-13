package com.junyou.event.acitvity;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 下载主活动事件(启动时调用ReFaBuGxConfigExportService.init)
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:02:20
 */
public class ZhuRfbActivityEvent extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	private Map<Integer,String> zuIds;
	
	private boolean isInit;
	
	public ZhuRfbActivityEvent(Map<Integer,String> zuIds,boolean isInit) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		
		this.zuIds = zuIds;
		this.isInit = isInit;
	}

	public Map<Integer, String> getZuIds() {
		return zuIds;
	}

	/**
	 * 是否是初始化
	 * @return
	 */
	public boolean isInit() {
		return isInit;
	}
	
}
