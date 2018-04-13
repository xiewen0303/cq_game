package com.junyou.bus.tongtian.easyaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tongtian.service.TongtianRoadService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.TONGTIAN_ROAD)
public class TongtianRoadAction {
	
	@Autowired
	private  TongtianRoadService tongtianLoadService;
	
	@EasyMapping(mapping = ClientCmdType.TONGTIAN_ROAD_INFO)
	public void getInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = tongtianLoadService.getInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.TONGTIAN_ROAD_INFO,ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.TONGTIAN_ROAD_CUILIAN)
	public void cuilian(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		List<Long> itemIds = null;
		if (data != null && data.length>0) {
			itemIds = new ArrayList<Long>();
			for (int i = 0; i < data.length; i++) {
				Long id = LongUtils.obj2long(data[i]);
				if (!itemIds.contains(id)) {
					itemIds.add(id);
				}
			}
		}
		Object[] ret = tongtianLoadService.itemCuilian(userRoleId,itemIds);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.TONGTIAN_ROAD_CUILIAN,ret);
		}
		
	}
}
