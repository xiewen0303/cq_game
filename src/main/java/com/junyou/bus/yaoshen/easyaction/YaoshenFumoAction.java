package com.junyou.bus.yaoshen.easyaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.service.YaoshenFumoService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.YAOSHEN_FUMO_MODULE)
public class YaoshenFumoAction {
	@Autowired
	private YaoshenFumoService yaoshenFumoService;

	@EasyMapping(mapping = ClientCmdType.YAOSHEN_FUMO_INFO)
	public void getYaoshenInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int  caowei  =  inMsg.getData();
		Object[] ret = yaoshenFumoService.getInfoByCaowei(userRoleId,caowei);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_FUMO_INFO, ret);
	}
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_FUMO_PANEL_INFO)
	public void getYaoshenPanelInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = yaoshenFumoService.getPanelInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_FUMO_PANEL_INFO, ret);
	}
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_FUMO_ATTR_CHANGE)
	public void getAllAttrToClient(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Map<String, Long> ret = yaoshenFumoService.getAllAttrToClient(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_FUMO_ATTR_CHANGE,ret);
	}
	// 升级
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_FUMO_SJ)
	public void yaoshenUpgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] allData = inMsg.getData();
		
		Object[] data = (Object[])allData[0];
		int caowei  = (Integer)allData[1];
		int index = (Integer)allData[2];
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
		Object[] ret = yaoshenFumoService.sj(userRoleId, itemIds,caowei,index);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_FUMO_SJ, ret);
		}
	}
}
