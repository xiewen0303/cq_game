package com.junyou.bus.firstChargeRebate.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.firstChargeRebate.server.RefabuFirstChargeRebateService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 
 *@Description 首冲返利命令交互处理
 *@Author Yang Gao
 *@Since 2016-6-6
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RefabuFirstChargeRebateAction {

	@Autowired
	private RefabuFirstChargeRebateService firstChargerRebateService;
	
	@EasyMapping(mapping = InnerCmdType.INNER_FIRST_CHARGER_REBATE)
	public void firstChargerYb(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Integer addVal = (Integer)inMsg.getData();
        firstChargerRebateService.firstChargeYb(userRoleId,addVal);
	}
	
	
	// 获取首充返利元宝面板信息
	@EasyMapping(mapping = ClientCmdType.FIRST_CHARGE_REBATE_INFO)
	public void firstChargeGetInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		
		Object[] result = firstChargerRebateService.firstChargeGetInfo(userRoleId, version, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.FIRST_CHARGE_REBATE_INFO, result);
		}
	}
	
	// 领取首充返利奖励
	@EasyMapping(mapping = ClientCmdType.FIRST_CHARGE_REBATE_RECEIVE)
	public void firstChargeReceive(Message inMsg){
	    Long userRoleId = inMsg.getRoleId();
	    
	    Object[] data = inMsg.getData();
	    Integer subId = (Integer) data[0];
	    Integer version = (Integer) data[1];
	    
	    Object[] result = firstChargerRebateService.firstChargeReceive(userRoleId, version, subId);
	    if(result != null){//返回null,则版本号不一致，处理配置数据
	        BusMsgSender.send2One(userRoleId, ClientCmdType.FIRST_CHARGE_REBATE_RECEIVE, result);
	    }
	}
	
	
}
