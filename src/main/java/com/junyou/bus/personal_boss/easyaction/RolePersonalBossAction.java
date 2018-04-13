package com.junyou.bus.personal_boss.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.personal_boss.service.RolePersonalBossService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.PERSONAL_BOSS)
public class RolePersonalBossAction {
	@Autowired
	private RolePersonalBossService service;

	@EasyMapping(mapping = ClientCmdType.PERSONAL_BOSS_INFO)
	public void info(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = service.info(userRoleId);
		if(result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.PERSONAL_BOSS_INFO, result);
		}
	}
	@EasyMapping(mapping = ClientCmdType.PERSONAL_BOSS_ENTER)
	public void enter(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int configId = (int) inMsg.getData();
//		Object[] result = 
		service.challenge(userRoleId,configId);
//		if(result != null) {
//			BusMsgSender.send2One(userRoleId, ClientCmdType.PERSONAL_BOSS_ENTER, result);
//		}
	}
	@EasyMapping(mapping = ClientCmdType.PERSONAL_BOSS_EXIT)
	public void exit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		service.exitStage(userRoleId,busMsgQueue);
		busMsgQueue.flush();
		
	}
	
	@EasyMapping(mapping = ClientCmdType.PERSONAL_BOSS_REWARD)
	public void reward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int configId = inMsg.getData();
		Object[] result = service.reward(userRoleId,configId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.PERSONAL_BOSS_REWARD, result);
	}
	///////////////////////////////////////////////////////////////////////////
	@EasyMapping(mapping = InnerCmdType.PERSONAL_BOSS_LEAVE)
	public void leave(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		service.exit(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.PERSONAL_BOSS_RESULT)
	public void result(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		service.killHandler(userRoleId);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PERSONAL_BOSS_RESULT, new Object[]{AppErrorCode.SUCCESS});
	}
	
}
