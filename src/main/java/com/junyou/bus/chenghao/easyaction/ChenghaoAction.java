package com.junyou.bus.chenghao.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.chenghao.service.ChenghaoService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.CHENGHAO_MODULE)
public class ChenghaoAction {
	@Autowired
	private ChenghaoService chenghaoService;

	@EasyMapping(mapping = InnerCmdType.INNER_CHENGHAO_EXPIRE)
	public void chenghaoExpire(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer chenghaoId = inMsg.getData();
		chenghaoService.chenghaoExpire(userRoleId, chenghaoId);
	}

	@EasyMapping(mapping = ClientCmdType.ACTIVATE_CHENGHAO)
	public void activateChenghao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer chenghaoId = inMsg.getData();
		Object[] result = chenghaoService.activateChenghaoByItem(userRoleId,chenghaoId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ACTIVATE_CHENGHAO,result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_CHENGHAO_INFO)
	public void getChenghaoInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = chenghaoService.getChenghaoInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CHENGHAO_INFO,result);
	}

	@EasyMapping(mapping = ClientCmdType.WEAR_CHENGHAO)
	public void wearChenghao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer chenghaoId = inMsg.getData();
		
		Object[] result = chenghaoService.wearChenghao(userRoleId, chenghaoId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.WEAR_CHENGHAO, result);
	}

	@EasyMapping(mapping = ClientCmdType.UNWEAR_CHENGHAO)
	public void unWearChenghao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer chenghaoId = inMsg.getData();
		
		Object[] result = chenghaoService.unWearChenghao(userRoleId, chenghaoId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.UNWEAR_CHENGHAO, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_ALL_DINGZHI_CHENGHAO)
	public void getAllDingzhiChenghao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = chenghaoService.getAllDingzhiChenghao(userRoleId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ALL_DINGZHI_CHENGHAO,result);
		}
	}
}
