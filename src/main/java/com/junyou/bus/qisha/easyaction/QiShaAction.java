package com.junyou.bus.qisha.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.qisha.service.QiShaService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-8-24 上午10:08:56
 */
@Controller
@EasyWorker(moduleName = GameModType.QISHA, groupName = EasyGroup.BUS_CACHE)
public class QiShaAction {
	@Autowired
	private QiShaService qiShaService;
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_QISHA_START)
	public void qishaStart(Message inMsg) {
		Integer id = inMsg.getData();
		qiShaService.ActiveStart(id);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_QISHA_END)
	public void qishaEnd(Message inMsg) {
		String stageId = inMsg.getData();
		qiShaService.ActiveEnd(stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.QISHA_EXP_ADD)
	public void addExp(Message inMsg) {
		String stageId = inMsg.getData();
		qiShaService.addExp(stageId);
	}
	
	@EasyMapping(mapping = ClientCmdType.ENTER_QISHA)
	public void enterQisha(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer activeId = inMsg.getData();
		Object[] result = qiShaService.enterMap(userRoleId, activeId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_QISHA, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.EXIT_QISHA)
	public void exitQisha(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = qiShaService.leaveMap(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.EXIT_QISHA, result);
		}
	}
	
	@EasyMapping(mapping = InnerCmdType.QISHA_BOSS_DEAD)
	public void bossDead(Message inMsg) {
		Object[] data = inMsg.getData();
		String monsterId = CovertObjectUtil.object2String(data[0]);
		Long userRoleId = CovertObjectUtil.object2Long(data[1]);
		String userRoleIds = CovertObjectUtil.object2String(data[2]);
		String bossName = CovertObjectUtil.object2String(data[3]);
		
		qiShaService.sendBossDeadGift(monsterId, userRoleId, userRoleIds,bossName);
	}
	
	
	
	
	
}
