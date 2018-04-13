package com.junyou.bus.new_hecheng.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.new_hecheng.service.NewHechengService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 
 * @description:新道具和装备合成 
 *
 *	@author ChuBin
 *
 * @date 2016-12-26
 */
@Component
@EasyWorker(moduleName = GameModType.NEW_HECHENG)
public class NewHechengAction {
     @Autowired
     private NewHechengService newHechengService;

	@EasyMapping(mapping = ClientCmdType.GOODS_HECHENG)
	public void goodsHecheng(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int configId = (int)data[0];
		int count = (int)data[1];
		int loop = (int)data[2];
		float clientSuccessRate = Float.valueOf(data[3].toString());
		Object[]  result = newHechengService.hecheng(userRoleId,configId,count,loop, clientSuccessRate);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GOODS_HECHENG, result);
	}
}
