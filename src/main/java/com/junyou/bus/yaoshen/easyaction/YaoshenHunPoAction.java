package com.junyou.bus.yaoshen.easyaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.service.YaoshenHunPoService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.YAOSHEN_HUNPO_MODULE)
public class YaoshenHunPoAction {
	@Autowired
	private YaoshenHunPoService yaoshenHunPoService;

	//魂魄信息
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_INFO)
	public void getYaoshenInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = yaoshenHunPoService.getHunpoInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_INFO, ret);
		}
	}

	// 升级魂魄
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_SJ)
	public void yaoshenUpgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < data.length; i++) {
			Long id = LongUtils.obj2long(data[i]);
			if (!itemIds.contains(id)) {
				itemIds.add(id);
			}
		}
		Object[] ret = yaoshenHunPoService.upgrade(userRoleId, itemIds);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_SJ, ret);
		}
	}

	// 请求镶嵌魄神
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_TAIGUANG_ON)
	public void poshenOn(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int hunpoId1 = (Integer)data[0];
		int index = (Integer)data[1];
		long poshenId = LongUtils.obj2long(data[2]);
		Object[] ret = yaoshenHunPoService.poshenOn(userRoleId, hunpoId1, index, poshenId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_TAIGUANG_ON, ret);
		}
	}
	// 请求卸载魄神
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_TAIGUANG_OFF)
	public void poshenOff(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int hunpoId1 = (Integer)data[0];
		int index = (Integer)data[1];
		Object[] ret = yaoshenHunPoService.poshenOff(userRoleId, hunpoId1, index);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_TAIGUANG_OFF, ret);
		}
	}
	// 请求某魂魄信息
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_INFO_ONE)
	public void getHunpoInfoById(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int id1 = (Integer)inMsg.getData();
		Object[] ret = yaoshenHunPoService.getHunpoInfoById(userRoleId, id1);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_INFO_ONE, ret);
		}
	}

	// 兑换道具
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_POSHEN_DUIHUAN)
	public void duiHuanItem(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		long guid = LongUtils.obj2long(inMsg.getData());
		Object[] ret = yaoshenHunPoService.poshenDuiHuan(userRoleId, guid);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_POSHEN_DUIHUAN, ret);
		}
	}
}
