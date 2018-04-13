package com.junyou.bus.boss_jifen.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.boss_jifen.service.RoleBossJifenService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@EasyWorker(moduleName = GameModType.BOSS_JIFEN)
@Controller
public class RoleBossJifenAction {
	@Autowired
	private RoleBossJifenService roleBossJifenService;
	@EasyMapping(mapping = ClientCmdType.BOSS_JIFEN_INFO)
	public void info(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = roleBossJifenService.info(userRoleId);
		if(result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.BOSS_JIFEN_INFO, result);
		}
	}
	@EasyMapping(mapping = ClientCmdType.BOSS_JIFEN_UPGRADE)
	public void upgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int targetId =inMsg.getData(); 
		Object[] result = roleBossJifenService.upgrade(userRoleId,targetId);
		if(result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.BOSS_JIFEN_UPGRADE, result);
		}
	}
}
