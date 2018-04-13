package com.junyou.bus.jueban.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.jueban.server.RefabuJuebanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.pool.executor.Message;

/**
 * 
 *@Description 绝版礼包指令交互处理
 *@Author Yang Gao
 *@Since 2016-6-6
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RefabuJuebanAction {

	@Autowired
	private RefabuJuebanService refabuJuebanService;
	
	
	// 领取首充返利奖励
	@EasyMapping(mapping = ClientCmdType.JUEBAN_RECEIVE)
	public void receiveJueban(Message inMsg){
	    Long userRoleId = inMsg.getRoleId();
	    
	    Object[] data = inMsg.getData();
	    Integer subId = RequestParameterUtil.object2Integer(data[0]);
	    Integer version = RequestParameterUtil.object2Integer(data[1]);
	    Integer id = RequestParameterUtil.object2Integer(data[2]);
	    
	    Object[] result = refabuJuebanService.receiveJueban(userRoleId, version, subId, id);
	    if(result != null){//返回null,则版本号不一致，处理配置数据
	        BusMsgSender.send2One(userRoleId, ClientCmdType.JUEBAN_RECEIVE, result);
	    }
	}
	
	
}
