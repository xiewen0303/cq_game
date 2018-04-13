package com.junyou.public_.nodecontrol.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.public_.nodecontrol.service.NodeControlService;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.NODE_CONTROL, groupName = EasyGroup.PUBLIC)
public class NodeControlAction {

	@Autowired
	private NodeControlService nodeControlService;
	
	/**
	 * 角色上线
	 * @param message
	 */
	@EasyMapping(mapping=InnerCmdType.NODE_INIT_IN)
	public void roleIn(Message message){
		
		roleInHandle(message);
	}
	
	/**
	 * 上线业务
	 * @param message
	 */
	private void roleInHandle(Message message){
		Long roleid = message.getRoleId();
		String ip = message.getData();
		
		nodeControlService.change2online(roleid);
		
		nodeControlService.nodeLogin(roleid,ip);
	}
	
	
	/**
	 * 角色下线
	 * @param message
	 */
	@EasyMapping(mapping=InnerCmdType.NODE_INIT_OUT)
	public void roleOut(Message message){
		Long roleid = message.getRoleId();

		nodeControlService.nodeExit(roleid);
	}
	
}
