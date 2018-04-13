package com.junyou.event.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.junyou.event.CampRankEvent;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.campwar.entity.CampRank;
import com.junyou.utils.datetime.GameSystemTime;

/**
* 阵营排行榜
 */
@Service
public class CampRankLogEventListener implements SmartApplicationListener {
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		 
		CampRankEvent campRankEvent = (CampRankEvent)event;
		List<CampRank> list  = campRankEvent.getList();
		Map<String, Object>  map  = new HashMap<>();
		for (CampRank campRank : list) {
			map.clear();
			map.put("userRoleId", campRank.getUserRoleId());
			map.put("name", campRank.getName());
			map.put("jifen", campRank.getJifen());
			map.put("timestamp", GameSystemTime.getSystemMillTime());
			LogPrintHandle.printLog(LogPrintHandle.CAMPWAR_STAGE,JSON.toJSON(map).toString());
		}  
		
		//集合对象引用置空,加快JVM回收(这个操作要认真理解才这像做，一般尽量不要这样弄)
		list = null;
	}

	@Override
	public int getOrder() {
		return 12;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是活动的子类
		return CampRankEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
