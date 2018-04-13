package com.junyou.bus.role.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.role.service.UserRoleService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.ROLE_BUS_MODULE)
public class UserRoleAction {
	@Autowired
	private UserRoleService userRoleService;
	/**
	 * 玩家改名
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.USER_MODIFY_NAME)
	public void modifyName(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String name = inMsg.getData();
		Object[] result =userRoleService.modifyName(userRoleId, name);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.USER_MODIFY_NAME, result);
		}
	}
	/**
	 * 获取玩家玩家改名cd
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_MODIFY_NAME_CD)
	public void getModifyNameCd(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result =userRoleService.getModifyNameCd(userRoleId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_MODIFY_NAME_CD, result);
		}
	}
	/**
	 * 玩家转职
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ZHUANZHI)
	public void modifyJob(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer configId = (Integer) data[0];
		String item = (String) data[1];
		/*Integer configId =inMsg.getData();
		String item = "";
*/		Object[] result =userRoleService.modifyJob(userRoleId, configId,item);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ZHUANZHI, result);
		}
	}
	
}
