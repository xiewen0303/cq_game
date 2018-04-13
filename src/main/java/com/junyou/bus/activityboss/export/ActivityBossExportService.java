package com.junyou.bus.activityboss.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.activityboss.service.ActivityBossService;
import com.junyou.stage.model.core.stage.IStage;

/**
 * 活动boss 对外业务
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-29 上午10:00:53
 */
@Service
public class ActivityBossExportService {
	
	@Autowired
	private ActivityBossService activityBossService;
	
	/**
	 * 修改伤害排行数据
	 * @param userRoleId
	 * @param monsterId
	 * @param harmVal
	 * @param isDead
	 */
	public void modifyHurtValue(long userRoleId,long monsterId,long harmVal,boolean isDead){
		activityBossService.modifyHurtValue(userRoleId, monsterId, harmVal, isDead);
	}

	public void init() { 
		activityBossService.initBossState();
	}

	public void innerDSBossRank(long monsterId, IStage stage) {
		activityBossService.innerDSBossRank(monsterId,stage);
	}

	public void retrieveBoss(long monsterId) {
		activityBossService.retrieveBoss(monsterId);
	}
	
	public void closeBossRank(long monsterId,IStage stage) {
		activityBossService.closeBossRank(monsterId,stage);
	}
}
