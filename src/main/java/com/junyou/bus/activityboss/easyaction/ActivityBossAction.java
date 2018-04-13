package com.junyou.bus.activityboss.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.activityboss.service.ActivityBossService;
import com.junyou.bus.activityboss.service.DingShiShuaGuaiService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;  
import com.kernel.pool.executor.Message;
 
/**
 * 野外boss
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */

@Controller
@EasyWorker(moduleName = GameModType.BOSS_ACTIVITY,groupName=EasyGroup.BUS_CACHE)
public class ActivityBossAction {
	
	@Autowired
	private DingShiShuaGuaiService dingShiShuaGuaiService;
	@Autowired
	private ActivityBossService activityBossService;
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_REFRESH_BOSS)
	public void dingshiRefreshBoss(Message inMsg) {
		Object[] datas = inMsg.getData();
		
		int id = (int)datas[0];
		int disappearDuration = (int)datas[1];
		
		dingShiShuaGuaiService.produceMonster(id,disappearDuration);
	}
	
	
	//获取排行榜数据需要从场景中获得
	@EasyMapping(mapping = ClientCmdType.YW_BOSS_SHOW)
	public void ywBossShow(Message inMsg){
		long userRoleId = inMsg.getRoleId();
		 
		Object[] result = activityBossService.ywBossShow();
		BusMsgSender.send2One(userRoleId, ClientCmdType.YW_BOSS_SHOW, result);
	}
	
	//开启野外boss排行榜数据(此方法已废弃：Liuyu)
	@EasyMapping(mapping = InnerCmdType.OPEN_BOSS_RANK)
	public void openBossRank(Message inMsg){
		long userRoleId = inMsg.getRoleId();
		long monsterId = inMsg.getData();
		
		activityBossService.openBossRank(userRoleId, monsterId);
	}
}
