package com.junyou.bus.cuilian.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.cuilian.service.CuilianService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.CUI_LIAN, groupName = EasyGroup.BUS_CACHE)
public class CuilianAction {

	@Autowired
	private CuilianService cuilianService;

	@EasyMapping(mapping = ClientCmdType.GET_CUILIAN_INFO)
	public void getShenQiInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();

		Object[] result = cuilianService.getCuilianInfo(userRoleId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CUILIAN_INFO,
					result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.CUILIAN_GO)
	public void cuilian(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer type = inMsg.getData();
		Object[] result = cuilianService.cuilian(userRoleId, type);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.CUILIAN_GO, result);
		}
	}
}
