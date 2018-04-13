package com.junyou.bus.role.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.role.service.UserRoleService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 沉迷Action
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午4:53:56
 */
@Controller
@EasyWorker(moduleName = GameModType.ROLE_BUS_MODULE)
public class ChenmiAction{
	
	@Autowired
	private UserRoleService userRoleService;
	
	@EasyMapping(mapping = InnerCmdType.CHENMIN_CMD)
	public void chenmi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		userRoleService.setChenmi(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.HALF_CHENMIN_CMD)
	public void halfChenmi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		userRoleService.setHalfChenmi(userRoleId);
	}
	
	/**
	 * 领取微端奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LQ_WEIDUAN)
	public void lqWeiDuanAward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = userRoleService.lqWeiDuanAward(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LQ_WEIDUAN, result);
	}

	/**
	 * 领取快捷连接奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LQ_WEIDUAN2)
	public void lqWeiDuanAward2(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();

		Object[] result = userRoleService.lqWeiDuanAward2(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LQ_WEIDUAN2, result);
	}
}
