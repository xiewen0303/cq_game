package com.junyou.bus.zhuansheng.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zhuansheng.service.ZhuanshengService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-11-3 上午11:44:34
 */
@Controller
@EasyWorker(moduleName = GameModType.ZHUANSHENG, groupName = EasyGroup.BUS_CACHE)
public class ZhuanShengAction {
	@Autowired
	private ZhuanshengService zhuanshengService;
	
	@EasyMapping(mapping = ClientCmdType.ZHUANSHENG_DUIHUAN_XIUWEI)
	public void duihuan(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer value = inMsg.getData();
		Object[] result = zhuanshengService.duihuan(userRoleId, value);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZHUANSHENG_DUIHUAN_XIUWEI, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.ZHUANSHENG_LEVEL_UP)
	public void levelUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = zhuanshengService.zhuansheng(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZHUANSHENG_LEVEL_UP, result);
	}
	
	

}
