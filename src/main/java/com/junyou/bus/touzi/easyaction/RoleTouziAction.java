package com.junyou.bus.touzi.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.touzi.service.RoleTouziService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 投资计划
 */
@Controller
@EasyWorker(moduleName = GameModType.TOUZI)
public class RoleTouziAction {
	@Autowired
	private RoleTouziService roleTouziService;

	@EasyMapping(mapping = ClientCmdType.GET_TOUZI_DATA)
	public void getTouziData(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = roleTouziService.getTouziData(userRoleId,GameConstants.TOUZHI_TYPE);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TOUZI_DATA, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.TOUZI_PLAN)
	public void touziPlan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		
		Object[] result = roleTouziService.touziPlan(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TOUZI_PLAN, result);
	}
	
//	@EasyMapping(mapping = ClientCmdType.RECEVIE_TOUZI)
//	public void recevieTouzi(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		Integer id = inMsg.getData();
//		
//		Object[] result = roleTouziService.recevieTouzi(userRoleId, id);
//		BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_TOUZI, result);
//	}
	
	
	//-----------------基金--------------------------
	@EasyMapping(mapping = ClientCmdType.GET_JIJIN_DATA)
	public void getJiJinData(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = roleTouziService.getTouziData(userRoleId,GameConstants.JIJING_TYPE);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_JIJIN_DATA, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.JIJIN_PLAN)
	public void jiJinPlan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		
		Object[] result = roleTouziService.jijinPlan(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JIJIN_PLAN, result);
	}
	
//	@EasyMapping(mapping = ClientCmdType.RECEVIE_JIJIN)
//	public void recevieJiJin(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		Integer id = inMsg.getData();
//		
//		Object[] result = roleTouziService.recevieJiJin(userRoleId, id);
//		BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_JIJIN, result);
//	}
}
