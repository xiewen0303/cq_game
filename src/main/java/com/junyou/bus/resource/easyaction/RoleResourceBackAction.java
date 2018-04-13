package com.junyou.bus.resource.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.resource.service.RoleResourceBackService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 资源找回
 * @author LiuYu
 * @date 2015-7-10 下午3:26:02
 */
@Controller
@EasyWorker(moduleName = GameModType.RESOURCE_BACK)
public class RoleResourceBackAction {
	
	@Autowired
	private RoleResourceBackService resourceBackService;
	
	/**
	 * 资源找回初始化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RESOURCE_BACK_INIT)
	public void onlineRewardInit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = resourceBackService.getResource(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RESOURCE_BACK_INIT, result);
	}
	
	
	/**
	 * 类型找回
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RESOURCE_BACK_TYPE_REWARD)
	public void typeBack(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer id = (Integer)data[0];
		Integer costType = (Integer)data[1];
		Object[] result = resourceBackService.reciveResourceById(userRoleId, id, costType);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RESOURCE_BACK_TYPE_REWARD, result);
		
	}
	
	/**
	 * 全部找回
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RESOURCE_BACK_KEY_EXTREME)
	public void oneKeyBack(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer costType = inMsg.getData();
		Object[] result = resourceBackService.reciveResourceAll(userRoleId, costType);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RESOURCE_BACK_KEY_EXTREME, result);
	}
	
}
