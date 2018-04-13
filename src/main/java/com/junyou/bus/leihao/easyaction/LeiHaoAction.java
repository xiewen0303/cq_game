package com.junyou.bus.leihao.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.leihao.service.LeiHaoService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.LEIHAO, groupName = EasyGroup.BUS_CACHE)
public class LeiHaoAction {

	@Autowired
	private LeiHaoService leiHaoService;

	@EasyMapping(mapping = ClientCmdType.PICK_LEI_HAO_REWARD)
	public void pick(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		Integer type = (Integer) data[3];
		Object[] result = null;
		if(type == 1){
			result = leiHaoService.pick(userRoleId, subId, version,configId,type);
		}else if(type == 2){
			result = leiHaoService.dayPick(userRoleId, subId, version,configId,type);
		}else{
			result = AppErrorCode.DATA_ERROR;
		}
		
		if (result != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.PICK_LEI_HAO_REWARD, result);
		}
	}
}
