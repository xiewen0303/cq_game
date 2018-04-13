package com.junyou.bus.happycard.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.happycard.service.HappyCardService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.HAPPY_CARD, groupName = EasyGroup.BUS_CACHE)
public class HappyCardAction {
	@Autowired
	private HappyCardService happyCardService;

	@EasyMapping(mapping = InnerCmdType.HAPPY_CARD_CHARGE)
	public void chargeRecord(Message inMsg) {
		Object[] data = inMsg.getData();
		Long yb = LongUtils.obj2long(data[0]);
		Long userRoleId = LongUtils.obj2long(data[1]);
		happyCardService.handleUserRecharge(userRoleId, yb.intValue());
	}

	/**
	 * 翻牌
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.FAN_CARD)
	public void fanCard(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer index = (Integer) data[2];
		Object[] ret = happyCardService.fanCard(userRoleId, subId, version,
				index);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.FAN_CARD, ret);
		}
	}

	/**
	 * 重置
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RESET_FAN_CARD)
	public void resetFanCard(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Object[] ret = happyCardService.reset(userRoleId, subId, version);
		if (ret != null) {
			BusMsgSender
					.send2One(userRoleId, ClientCmdType.RESET_FAN_CARD, ret);
		}
	}
}
