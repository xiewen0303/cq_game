package com.junyou.bus.active.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.active.service.CampWarService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 阵营战Action
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-8 下午2:58:04 
 */
@Controller
@EasyWorker(moduleName = GameModType.CAMP_WAR)
public class CampWarAction {
	@Autowired
	private CampWarService campWarService;

	@EasyMapping(mapping = ClientCmdType.ENTER_CAMP_WAR)
	public void enterCampWar(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = campWarService.enterCampWar(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_CAMP_WAR, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.LEVEL_CAMP_WAR)
	public void levelCampWar(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = campWarService.levelCampWar(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEVEL_CAMP_WAR, result);
	}
}
