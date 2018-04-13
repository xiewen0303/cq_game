package com.junyou.bus.hundun.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.hundun.service.HundunService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-9-7 下午4:33:35
 */
@Controller
@EasyWorker(moduleName = GameModType.HUNDUN, groupName = EasyGroup.BUS_CACHE)
public class HundunAction {

	@Autowired
	private HundunService hundunService;
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_HUNDUN_ADD_EXP)
	public void addExp(Message inMsg) {
		String stageId = inMsg.getData();
		hundunService.addExp(stageId);
	}
	
	@EasyMapping(mapping = ClientCmdType.CHAOS_ENTER)
	public void enter(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer activeId = inMsg.getData();
		Object[] result = hundunService.enterMap(userRoleId, activeId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_ENTER, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.CHAOS_EXIT)
	public void exit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = hundunService.leaveMap(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_EXIT, result);
		}
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_HUNDUN_START)
	public void hundunStart(Message inMsg) {
		Integer id = inMsg.getData();
		hundunService.ActiveStart(id);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_HUNDUN_END)
	public void hundunEnd(Message inMsg) {
		String stageId = inMsg.getData();
		hundunService.ActiveEnd(stageId);
	}
}
