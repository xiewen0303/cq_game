package com.junyou.bus.platform.taiwan.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.taiwan.service.TaiWanService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.TAIWAN_PLATFORM_MODULE)
public class TaiWanAction {
	@Autowired
	private TaiWanService taiWanService;
	
	@EasyMapping(mapping = InnerCmdType.TAIWAN_PF_BANGDING)
	public void tencentViaUser(Message inMsg){
		Long userRoleId =inMsg.getData();
		taiWanService.tencentViaUser(userRoleId);
	}
	
}
