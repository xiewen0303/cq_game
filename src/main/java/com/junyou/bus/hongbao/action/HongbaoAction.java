package com.junyou.bus.hongbao.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.hongbao.service.HongbaoService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
@Component
@EasyWorker(moduleName = GameModType.HONGBAO)
public class HongbaoAction {

	@Autowired
	private HongbaoService hongbaoService;
	
	/**
	 * 指令模拟测试发红包
	 * @param inMsg
	
	@EasyMapping(mapping = 3263)
	public void test(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		hongbaoService.sendHongbao(userRoleId);
	}
	 *********/
	
	/**
	 * 首冲发红包
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.HONGBAO_SEND)
	public void sendHongbao(Message inMsg){
		Long roleId = inMsg.getRoleId();
		hongbaoService.sendHongbao(roleId);   
	}
	/**
	 * 拆红包
	 */
	@EasyMapping(mapping = ClientCmdType.HONGBAO_GET)
	public void getHongbao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  data= inMsg.getData();
		int type = (Integer)data[0];
		Double guild   =(Double)data[1];
		Object[] ret =  hongbaoService.chaiHongbao(userRoleId,type,guild.longValue());
		BusMsgSender.send2One(userRoleId, ClientCmdType.HONGBAO_GET, ret);
	}
}
