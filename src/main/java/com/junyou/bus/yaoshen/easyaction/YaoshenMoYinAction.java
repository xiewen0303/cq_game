package com.junyou.bus.yaoshen.easyaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.service.YaoshenMoYinService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.YAOSHEN_MOYIN_MODULE)
public class YaoshenMoYinAction {
	@Autowired
	private YaoshenMoYinService yaoshenMoYinService;

	// 魔印信息
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOYIN_INFO)
	public void getYaoshenInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = yaoshenMoYinService.getMoYinInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_INFO, ret);
	}

	// 获取当前魔印属性
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOYIN_ATTR_CHANGE)
	public void getAttr(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Map<String, Long> attrMap = yaoshenMoYinService.getCurrentAttribute(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_ATTR_CHANGE, attrMap);
	}

	// 升级
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOYIN_SJ)
	public void yaoshenUpgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		List<Long> itemIds = null;
		if (data != null) {
			itemIds = new ArrayList<Long>();
			for (int i = 0; i < data.length; i++) {
				Long id = LongUtils.obj2long(data[i]);
				if (!itemIds.contains(id)) {
					itemIds.add(id);
				}
			}
		}
		Object[] ret = yaoshenMoYinService.upgrade(userRoleId, itemIds);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_SJ, ret);
		}
	}
}
