package com.junyou.bus.platform.yuenan.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.yuenan.service.YuenanYaoqingService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.YUENAN_PLATFORM_MODULE)
public class YueNanAction {
	@Autowired
	private YuenanYaoqingService yuenanYaoqingService;
	
	@EasyMapping(mapping = ClientCmdType.YUENAN_GET_INFO)
	public void getLingQuInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int info = yuenanYaoqingService.getLingQuCount(userRoleId);
			
		BusMsgSender.send2One(userRoleId, ClientCmdType.YUENAN_GET_INFO,info);
	}
	
	@EasyMapping(mapping = ClientCmdType.YUENNA_LINGQU_JITEM)
	public void getRolePlatformInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] info = yuenanYaoqingService.lingQu(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YUENNA_LINGQU_JITEM,info);
	}
	@EasyMapping(mapping = ClientCmdType.LAQU_YAOQING_ID)
	public void getYaoQingId(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String info = yuenanYaoqingService.getYaoqingId(userRoleId);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.LAQU_YAOQING_ID,info);
	}
	@EasyMapping(mapping = ClientCmdType.CUNCHU_YAOQING_ID)
	public void cunChuYaoQingId(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ids = inMsg.getData();
		yuenanYaoqingService.cunchuYaoQingId(userRoleId, ids);
	}
	
}
