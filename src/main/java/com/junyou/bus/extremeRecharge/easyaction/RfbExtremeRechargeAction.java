package com.junyou.bus.extremeRecharge.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.extremeRecharge.service.RfbExtremeRechargeService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RfbExtremeRechargeAction {

	@Autowired
	private RfbExtremeRechargeService rfbExtremeRechargeService;
	
	@EasyMapping(mapping = ClientCmdType.EXTREME_RECHARGE_RECEIVE_REWARD)
	public void receiveReward(Message inMsg){
	    Long userRoleId = inMsg.getRoleId();
	    
	    Object[] data = inMsg.getData();
	    Integer subId = (Integer) data[0];
	    Integer version = (Integer) data[1];
	    
	    Object[] result = rfbExtremeRechargeService.receiveReward(userRoleId, version, subId);
	    if(result != null){//返回null,则版本号不一致，处理配置数据
	        BusMsgSender.send2One(userRoleId, ClientCmdType.EXTREME_RECHARGE_RECEIVE_REWARD, result);
	    }
	}
}
