package com.junyou.bus.huanhua.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.huanhua.service.HuanHuaService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.HUANHUA, groupName = EasyGroup.BUS_CACHE)
public class HuanhuaAction {
	@Autowired
	private HuanHuaService huanHuaService;

	@EasyMapping(mapping = ClientCmdType.HUANHUA)
	public void huanhua(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer type = (Integer) data[0];
		Integer configId = (Integer) data[1];
		Object[] result = huanHuaService.huanhua(userRoleId, type, configId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUANHUA, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.HUANHUA_ACTIVATE)
	public void huanhuaActivate(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer type = (Integer) data[0];
		Integer configId = (Integer) data[1];
		Object[] result = huanHuaService.huanhuaActivate(userRoleId, type,
				configId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUANHUA_ACTIVATE,
					result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.HUANHUA_INFO)
	public void huanhuaInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer type = (Integer) inMsg.getData();
		Object[] result = huanHuaService.getHuanhuaInfo(userRoleId, type);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUANHUA_INFO,
					result);
		}
	}
}
