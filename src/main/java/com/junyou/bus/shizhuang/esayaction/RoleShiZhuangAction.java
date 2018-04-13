package com.junyou.bus.shizhuang.esayaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.shizhuang.service.RoleShiZhuangService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-8-7 下午7:44:09
 */
@Component
@EasyWorker(moduleName = GameModType.SHIZHUANG_MODULE)
public class RoleShiZhuangAction {
	@Autowired
	private RoleShiZhuangService roleShiZhuangService;
	
	@EasyMapping(mapping = ClientCmdType.GET_SHIZHUANG_INFO)
	public void getShiZhuangInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = roleShiZhuangService.getRoleShiZhuangInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_SHIZHUANG_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.ACTIVE_SHIZHUANG)
	public void activeShizhuang(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] result = roleShiZhuangService.activeShiZhuang(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ACTIVE_SHIZHUANG, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.SHIZHUANG_LEVEL_UP)
	public void levelUpShiZhuang(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] result = roleShiZhuangService.levelUpShiZhuang(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHIZHUANG_LEVEL_UP, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.CHANGE_SHIZHUANG)
	public void changeShizhuang(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		roleShiZhuangService.changeShizhuang(userRoleId, id);
	}
	
	@EasyMapping(mapping = InnerCmdType.SHIZHUANG_EXPIRE)
	public void shizhuangExpire(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		roleShiZhuangService.shizhuangExpire(userRoleId, 1);
	}
	
	@EasyMapping(mapping = ClientCmdType.XIANSHI_SHIZHUANG_XUFEI)
	public void shizhuangXufei(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] result = roleShiZhuangService.xufei(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANSHI_SHIZHUANG_XUFEI, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.SHIZHUANG_JINJIE_INFO)
	public void getShiZhuangJinJieInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = roleShiZhuangService.getRoleShiZhuangJinJieInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHIZHUANG_JINJIE_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.SHIZHUANG_JINJIE_UP)
	public void activeShizhuangJinJie(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int id = (Integer)data[0];
		int level = (Integer)data[1];
		Object[] result = roleShiZhuangService.shiZhuangJinJie(userRoleId, id,level);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHIZHUANG_JINJIE_UP, result);
	}
	
}
