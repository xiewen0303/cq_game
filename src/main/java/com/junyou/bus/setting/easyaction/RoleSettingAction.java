package com.junyou.bus.setting.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.setting.service.RoleSettingService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 角色设置（快捷键等）
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-16 下午7:22:12 
 */
@Controller
@EasyWorker(moduleName = GameModType.ROLE_BUS_MODULE)
public class RoleSettingAction {
	@Autowired
	private RoleSettingService roleSettingService;

	@EasyMapping(mapping = ClientCmdType.CHANGE_QUICK_BAR)
	public void changeQuickBar(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		String index = String.valueOf(obj[0]);
		Integer type = (Integer) obj[1];
		String data = (String) obj[2];
		
		roleSettingService.changeQuickBar(userRoleId, index, type, data);
	}
	
	@EasyMapping(mapping = ClientCmdType.SAVE_GUAJI_SET)
	public void changeGuaji(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		Integer type = (Integer) obj[0];
		Object data = obj[1];
		
		roleSettingService.changeGuaji(userRoleId, type, data);
	}
}
