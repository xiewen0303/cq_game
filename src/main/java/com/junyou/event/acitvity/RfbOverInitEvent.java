package com.junyou.event.acitvity;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 启动服务器后热发布配置解析完毕后抛出事件
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-6-15 下午9:42:33
 */
public class RfbOverInitEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;

	public RfbOverInitEvent() {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
	}

}
