package com.junyou.bus.platform.qq.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.constants.QQGuanjiaConstants;
import com.junyou.bus.platform.qq.service.QqGuanjiaService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqGuanjiaAction {
	@Autowired
	private QqGuanjiaService qqGuanjiaService;
	
	@EasyMapping(mapping = ClientCmdType.TENCENT_GUANJIA_INFO)
	public void getTgpInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] s = qqGuanjiaService.getInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TENCENT_GUANJIA_INFO,s);
	}
	@EasyMapping(mapping = ClientCmdType.TENCENT_GUANJIA_FIRST)
	public void getNewRewards(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] s =  qqGuanjiaService.getRewards(userRoleId,QQGuanjiaConstants.FLAG_0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TENCENT_GUANJIA_FIRST,s);
	}
	@EasyMapping(mapping = ClientCmdType.RFB_ONLINE_REWARDS_DAY)
	public void getDayRewards(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] s = qqGuanjiaService.getRewards(userRoleId,QQGuanjiaConstants.FLAG_1);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RFB_ONLINE_REWARDS_DAY,s);
	}
	
	 
	
	
}
