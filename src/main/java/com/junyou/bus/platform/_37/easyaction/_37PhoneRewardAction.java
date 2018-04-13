package com.junyou.bus.platform._37.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.platform._37.service._37Service;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType._37_PHONE_REWARD, groupName = EasyGroup.BUS_CACHE)
public class _37PhoneRewardAction {
	@Autowired
	private _37Service _37Service;

	@EasyMapping(mapping = ClientCmdType.GET_PHONE_REWARD_INFO)
	public void getPhoneRewardInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object ret = _37Service.getPhoneRewardStatus(userRoleId);
		if(ret!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PHONE_REWARD_INFO,
					ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.PICK_PHONE_REWARD)
	public void pickPhoneReward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = _37Service.pickPhoneReward(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.PICK_PHONE_REWARD,
					ret);
		}
	}
}
