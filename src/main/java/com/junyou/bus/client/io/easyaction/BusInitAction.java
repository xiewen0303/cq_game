package com.junyou.bus.client.io.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.client.io.service.IoServiceImpl;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.CLIENT_IO,groupName = EasyGroup.BUS_INIT)
public class BusInitAction {

	@Autowired
	private IoServiceImpl ioServiceImpl;
	
	/**
	 * 角色进入游戏前数据初始化
	 * @param context
	 */
	@EasyMapping(mapping=InnerCmdType.BUS_INIT_IN)
	public void roleIn(Message msg){
		
		String ip = msg.getData();

		ioServiceImpl.roleIn(msg.getRoleId(),ip);
		
	}
	
	/**
	 * 角色退出游戏
	 * @param context
	 */
	@EasyMapping(mapping=InnerCmdType.BUS_INIT_OUT)
	public void roleOut(Message msg){

		ioServiceImpl.roleOut(msg.getRoleId());
		
	}

}
