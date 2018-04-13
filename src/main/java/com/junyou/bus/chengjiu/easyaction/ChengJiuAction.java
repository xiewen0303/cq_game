package com.junyou.bus.chengjiu.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.chengjiu.server.RoleChengJiuService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 成就
 * @author ZHONGDIAN
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.CHENGJIU,groupName=EasyGroup.BUS_CACHE)
public class ChengJiuAction {
	
	@Autowired
	private RoleChengJiuService roleChengJiuService;
	
	@EasyMapping(mapping = ClientCmdType.CHENGJIU_INFO)
	public void geiChengJiuInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=roleChengJiuService.getChengJiuInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENGJIU_INFO, result);
	}
	
	
	@EasyMapping(mapping = ClientCmdType.CHENGJIU_TYPE_INFO)
	public void getChengJiuByType(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		
		Object[] result=roleChengJiuService.getChengJiuByType(userRoleId, obj);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENGJIU_TYPE_INFO, result);
	}

	
	//成就激活
	@EasyMapping(mapping = InnerCmdType.CHENGJIU_CHARGE,kuafuType=EasyKuafuType.KF2S_HANDLE_TYPE)
	public void zuoqiUpdateShow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		Integer type = (Integer) obj[0];
		Integer cjValue = (Integer) obj[1];
		
		roleChengJiuService.jianCeAndJiHuoCheng(userRoleId, type, cjValue);
	}
	
	//领奖
	@EasyMapping(mapping = ClientCmdType.LINGJIANG)
	public void lingjiang(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int configId = (int)inMsg.getData();
		
		Object[] result = roleChengJiuService.lingjiang(userRoleId, configId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LINGJIANG, result);
	}
}
