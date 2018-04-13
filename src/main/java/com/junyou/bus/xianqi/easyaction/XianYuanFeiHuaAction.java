package com.junyou.bus.xianqi.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xianqi.service.XianYuanFeiHuaService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;


@Controller
@EasyWorker(moduleName = GameModType.XIANYUANFEIHUA, groupName = EasyGroup.BUS_CACHE)
public class XianYuanFeiHuaAction {
	@Autowired
	private XianYuanFeiHuaService xianYuanFeiHuaService;
	
	@EasyMapping(mapping = ClientCmdType.XYFH_NORMAL_UPGRADE)
	public void normalUpgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=xianYuanFeiHuaService.startUpgrade(userRoleId, busMsgQueue, isAutoGm);
		busMsgQueue.flush();
		BusMsgSender.send2One(userRoleId, ClientCmdType.XYFH_NORMAL_UPGRADE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.XYFH_INFO)
	public void info(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result=xianYuanFeiHuaService.getRoleXianyuanFeihuaInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XYFH_INFO, result);
	}

}
