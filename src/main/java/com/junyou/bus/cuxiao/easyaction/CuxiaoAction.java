package com.junyou.bus.cuxiao.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.cuxiao.service.CuxiaoService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.CU_XIAO)
public class CuxiaoAction {

	@Autowired
	private  CuxiaoService cuxiaoService;
	
//	@EasyMapping(mapping = ClientCmdType.CUXIAO_GET_REWARD)
//	public void getInfo(Message inMsg) {
//		Long userRoleId = inMsg.getRoleId();
//		int configId = inMsg.getData();
//		Object[] ret = cuxiaoService.getRewards(userRoleId,configId);
//
//		if (ret != null) {
//			BusMsgSender.send2One(userRoleId, ClientCmdType.CUXIAO_GET_REWARD,ret);
//		}
//	}


	@EasyMapping(mapping = ClientCmdType.CUXIAO_GET_REWARD)
	public void getInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int configId = inMsg.getData();
		Object[] ret = cuxiaoService.getRewards2(userRoleId,configId);

		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.CUXIAO_GET_REWARD,ret);
		}
	}
}
