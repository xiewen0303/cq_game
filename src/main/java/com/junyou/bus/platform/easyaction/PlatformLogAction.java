package com.junyou.bus.platform.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.service.PlatformLogService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 平台日志打印指令
 * @author DaoZheng Yuan
 * 2015年6月15日 下午12:10:57
 */
@Controller
@EasyWorker(moduleName = GameModType.PLATFORM_TJ)
public class PlatformLogAction {

	@Autowired
	private PlatformLogService platformLogService;
	
	/**
	 * 打印平台统计日志
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.PLATFORM_LOG_PRINT)
	public void printPlatformLog(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String url = inMsg.getData();
		
		platformLogService.printPlatformLog(userRoleId, url);
	}
}
