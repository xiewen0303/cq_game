package com.junyou.bus.tanbao.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tanbao.service.TanBaoService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.TANBAO_MODULE, groupName = EasyGroup.BUS_CACHE)
public class TanBaoAction {

	@Autowired
	private TanBaoService tanBaoService;
	/**
	 * 进入探宝
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ENTER_TANBAO)
	public void enterTanBao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();

		Object[] result = tanBaoService.enterXianGong(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_TANBAO, result);
		}
	}
	
}
