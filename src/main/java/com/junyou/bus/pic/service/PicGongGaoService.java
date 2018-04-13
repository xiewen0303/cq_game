package com.junyou.bus.pic.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.login.configure.RefabuLoginConfigExportService;
import com.junyou.bus.login.configure.RefabuLoginConfigGroup;
import com.junyou.bus.pic.configure.export.PicNoticeConfigExportService;
import com.junyou.bus.pic.utils.PicQiangTanMap;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;

/**
 * @author zhongdian
 * 2016-3-15 下午2:51:29
 */
@Service
public class PicGongGaoService {

	
	public void onlineHandle(Long userRoleId){
		Map<Integer,String> groups = PicNoticeConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, String> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			if(PicQiangTanMap.getDataByUserAndSubId(userRoleId, configSong.getActivityId())){
				BusMsgSender.send2One(userRoleId, ClientCmdType.TUPIAN_GONGGAO_TUISONG, configSong.getActivityId());
			}
			
		}
		
		
		
	}
	
	
}
