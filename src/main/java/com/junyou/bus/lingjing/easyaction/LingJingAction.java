package com.junyou.bus.lingjing.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.lingjing.service.LingJingService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 周天灵境
 * @author LiuYu
 * @date 2015-6-29 下午7:31:49
 */
@Controller
@EasyWorker(moduleName = GameModType.LINGJING_MODULE)
public class LingJingAction {
	 
	@Autowired
	private LingJingService lingJingService;
	
	@EasyMapping(mapping = ClientCmdType.GET_LINGJING_INFO)
	public void getLingJingInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = lingJingService.getLingJingInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_LINGJING_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.ACTIVE_LINGJING)
	public void activeLingJing(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer type = inMsg.getData();
		Object[] result = lingJingService.activeLingJing(userRoleId,type);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ACTIVE_LINGJING, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.TUPO_LINGJING)
	public void tupo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = lingJingService.tupo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TUPO_LINGJING, result);
	}

}
