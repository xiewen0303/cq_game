package com.junyou.bus.activityboss.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.activityboss.service.DingShiShuaGuaiService;
import com.junyou.stage.model.core.stage.IStage;

/**
 * 活动boss 对外业务
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-29 上午10:00:53
 */
@Service
public class DingShiShuaGuaiExportService {
	
	@Autowired
	private  DingShiShuaGuaiService dingShiShuaGuaiService;

	 
	public void dsBossMonster(int bossId,int mapId,int line,IStage stage,int disappearDuration) {
		dingShiShuaGuaiService.dsBossMonster(bossId,mapId, line, stage, disappearDuration);
	}

	public void dsMonster(int bossId, int line, IStage stage,int disappearDuration) {
		dingShiShuaGuaiService.dsMonster(bossId, line, stage, disappearDuration);
	} 
	
	public void dsGuildHurtMonster(int bossId, int line, IStage stage,int disappearDuration) {
		dingShiShuaGuaiService.dsGuildHurtMonster(bossId, line, stage, disappearDuration);
	}
	
	public void dsMglyBossMonster(int bossId, int line, IStage stage,int disappearDuration) {
	    dingShiShuaGuaiService.dsMglyBossMonster(bossId, line, stage, disappearDuration);
	} 
	
}
