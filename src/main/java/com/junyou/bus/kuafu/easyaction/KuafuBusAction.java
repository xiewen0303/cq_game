package com.junyou.bus.kuafu.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.kuafu.service.KuafuBusService;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1KuafuService;
import com.junyou.bus.shenmo.service.KuafuArena4v4KuafuService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.KUAFU_MODULE)
public class KuafuBusAction {

	@Autowired
	private KuafuBusService kuafuBusService;
	@Autowired
	private KuafuArena1v1KuafuService kuafuArena1v1KuafuService;
	@Autowired
	private KuafuArena4v4KuafuService kuafuArena4v4KuafuService;

	/**
	 * 进入跨服地图
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_KUAFU_ENTER)
	public void enterKuafu(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		Object data = inMsg.getData();

		kuafuBusService.enterKuafu(roleId, data);
	}

	/**
	 * 跨服活动开始
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ACTIVE_START)
	public void kfActiveStart(Message inMsg) {
		Integer activeId = inMsg.getData();
		kuafuBusService.kfActiveStart(activeId);
	}

	/**
	 * 跨服活动结束
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_ACTIVE_END)
	public void kfActiveEnd(Message inMsg) {
		Integer activeId = inMsg.getData();
		kuafuBusService.kfActiveEnd(activeId);
	}

	/**
	 * 退出跨服地图
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_KUAFU_LEAVE)
	public void leaveKuafu(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		Object data = inMsg.getData();
		if (data == null) {
			kuafuBusService.leaveKuafu(roleId);
		} else {
			Boolean flag = (Boolean) data;
			if (flag) {
				boolean ret = kuafuArena1v1KuafuService.handleOffline(roleId);
				if (!ret) {
					kuafuBusService.leaveKuafu(roleId);
				}
			} else {
				kuafuArena4v4KuafuService.handleOffline(roleId);
				kuafuBusService.leaveKuafu(roleId);
			}
		}
	}
}
