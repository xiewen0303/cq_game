package com.junyou.bus.laba.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.laba.service.LaBaService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 拉霸活动
 */
@Controller
@EasyWorker(moduleName = GameModType.LABA, groupName = EasyGroup.BUS_CACHE)
public class LaBaAction {
	@Autowired
	private LaBaService laBaService;
	
//	/**
//	 *拉霸一次
//	 * @param inMsg
//	 */
//	@EasyMapping(mapping = ClientCmdType.LABA_GO)
//	public void lingquLevelLibao(Message inMsg) {
//		Long userRoleId = inMsg.getRoleId();
//		Object[] data = inMsg.getData();
//		Integer subId = (Integer) data[0];
//		Integer version = (Integer) data[1];
//		Object[] result = laBaService.labaAction(userRoleId,subId,version);
//		if (result != null) {// 返回null,则版本号不一致，处理配置数据
//			BusMsgSender.send2One(userRoleId, ClientCmdType.LABA_GO, result);
//		}
//	}
	
	
	/**
	 *拉霸一次
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LABA_GO53)
	public void lingquLevelLibao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Object[] result = laBaService.laba(userRoleId,subId,version);
		if (result != null) {// 返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.LABA_GO53, result);
		}
	}
	 
	
	/**
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LABA_RECORD53)
	public void labaRecord53(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Object[] result = laBaService.labaRecords(userRoleId,subId,version);
		if (result != null) {// 返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.LABA_RECORD53, result);
		}
	}
	
	
}
