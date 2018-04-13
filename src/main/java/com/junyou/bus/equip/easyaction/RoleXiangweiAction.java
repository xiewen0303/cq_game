package com.junyou.bus.equip.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.equip.service.RoleXiangweiService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.SUIT_XIANGWEI)
public class RoleXiangweiAction {
	@Autowired
	private RoleXiangweiService roleXiangweiService;
	
	//套装象位系统
	@EasyMapping(mapping=ClientCmdType.TAO_ZHUANG_XIANG_WEI_UP)
	public void upgradeTaoZhuangXiangWei(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = CovertObjectUtil.object2Long(inMsg.getData());
		BusMsgQueue busMsgQueue=new BusMsgQueue(); 
		Object[] result=roleXiangweiService.taozhuangXiangWeiUp(userRoleId,guid,busMsgQueue);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAO_ZHUANG_XIANG_WEI_UP, result);
		busMsgQueue.flush();
	}
	
	//套装象位系统
	@EasyMapping(mapping=ClientCmdType.TAO_ZHUANG_XIANG_WEI_INFO)
	public void info(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=roleXiangweiService.getRoleXiangweiInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAO_ZHUANG_XIANG_WEI_INFO, result);
	}
}
