package com.junyou.bus.superduihuan.easyaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.superduihuan.service.RfbSuperDuihuanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.SUPER_DUIHUAN)
public class RefabuSuperDuihuanAction {

	@Autowired
	private RfbSuperDuihuanService superDuihuanService;

	@EasyMapping(mapping = ClientCmdType.SUPER_DUIHUAN)
	public void superDuihuan(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		Object[] items = (Object[]) data[3];
		List<Long> itemIds1 = new ArrayList<Long>();
		List<Long> itemIds2 = new ArrayList<Long>();
		if (items != null && items.length > 0) {
			Object[] items1 = (Object[]) items[0];
			for (int i = 0; i < items1.length; i++) {
				Long id = LongUtils.obj2long(items1[i]);
				if (!itemIds1.contains(id)) {
					itemIds1.add(id);
				}
			}
		}
		if (items != null && items.length > 1) {
			Object[] items2 = (Object[]) items[1];
			for (int i = 0; i < items2.length; i++) {
				Long id = LongUtils.obj2long(items2[i]);
				if (!itemIds2.contains(id)) {
					itemIds2.add(id);
				}
			}
		}
		Object[] ret = superDuihuanService.duihuan(userRoleId, subId, version,
				configId, itemIds1, itemIds2);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.SUPER_DUIHUAN, ret);
		}
	}
}
