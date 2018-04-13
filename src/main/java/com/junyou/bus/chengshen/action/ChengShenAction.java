package com.junyou.bus.chengshen.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.chengshen.service.ChengShenService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.CHENG_SHEN)
public class ChengShenAction {

	@Autowired
	private ChengShenService chengShenService;
	/**
	 * 获取 信息
	 */
	@EasyMapping(mapping = ClientCmdType.CHENG_SHEN_INFO)
	public void getInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer result = chengShenService.getInfo(userRoleId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHENG_SHEN_INFO, result);
		}
	}
	/**
	 * 升级
	 */
	@EasyMapping(mapping = ClientCmdType.CHENG_SHEN_SJ)
	public void upgrade(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int currentLevel = inMsg.getData();
		Object[] result = chengShenService.upgrade(userRoleId,currentLevel);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENG_SHEN_SJ, result);
	}
	/**
	 * 模拟获得神魂值
	@EasyMapping(mapping = 3555)
	public void getShenHunZhiZ_test(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int value  = inMsg.getData();
		chengShenService.addSHZFromJJC(userRoleId,value);
	}
	 */
}
