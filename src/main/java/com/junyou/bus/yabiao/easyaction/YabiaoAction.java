package com.junyou.bus.yabiao.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yabiao.service.YabiaoService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * 押镖Action
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-13 上午11:12:02 
 */
@Controller
@EasyWorker(moduleName = GameModType.YABIAO_MODULE)
public class YabiaoAction {
	@Autowired
	private YabiaoService yabiaoService;
	
	@EasyMapping(mapping = ClientCmdType.GET_YABIAO_INFO)
	public void getYabiaoInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = yabiaoService.getYabiaoInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_YABIAO_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.RECEIVE_YABIAO)
	public void receiveYabiao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = yabiaoService.receiveYabiao(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RECEIVE_YABIAO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.FINISH_YABIAO)
	public void finishYabiao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = yabiaoService.finishYabiao(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FINISH_YABIAO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.REFRESH_BIAOCHE)
	public void refreshBiaoche(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer type = inMsg.getData();
		Object[] result = yabiaoService.refreshBiaoche(userRoleId,type);
		BusMsgSender.send2One(userRoleId, ClientCmdType.REFRESH_BIAOCHE, result);
	}
	
	@EasyMapping(mapping = InnerCmdType.B_BIAOCHE_DEAD)
	public void yabiaoDead(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		Integer bcConfigId = (Integer) data[0];
		Long benefitRoleId = CovertObjectUtil.obj2long(data[1]);
		
		yabiaoService.yabiaoDead(userRoleId, bcConfigId, benefitRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.B_BIAOCHE_STATUS)
	public void yabiaoStatus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		yabiaoService.yabiaoStatus(userRoleId);
	}

	@EasyMapping(mapping = ClientCmdType.BIAOC_STATU)
	public void biaocStatu(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int statu = yabiaoService.getBiaocStatu(userRoleId);
		if(statu != -1){
			BusMsgSender.send2One(userRoleId, ClientCmdType.BIAOC_STATU, statu);
		}
	}
}