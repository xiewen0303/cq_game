package com.junyou.bus.map.configure.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.map.configure.service.ActiveMapService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-8-5 下午10:18:08
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class ActiveMapAction {
	@Autowired
	private ActiveMapService activeMapService;
	
	/**
	 * 请求进入地图
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ENTER_ACTIVE_GAME)
	public void lingquLevelLibao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Object[] result = activeMapService.enterMap(userRoleId, subId, version);
		if (result != null) {// 返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_ACTIVE_GAME, result);
		}
	}
	
	/**
	 * 内部销毁地图
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_REFB_ACTIVE_MAP_OVER)
	public void mapOver(Message inMsg) {
		String stageId = inMsg.getData();
		activeMapService.mapClear(stageId);
	}
	
	/**
	 * 创建地图
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_REFB_ACTIVE_MAP_CREATE)
	public void mapCreate(Message inMsg) {
		Integer subId = inMsg.getData();
		activeMapService.mapCreate(subId);
	}
}
