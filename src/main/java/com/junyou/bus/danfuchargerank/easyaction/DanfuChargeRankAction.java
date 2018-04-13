package com.junyou.bus.danfuchargerank.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.danfuchargerank.service.DanfuChargeRankService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAF_CHARGE_RANK, groupName = EasyGroup.BUS_CACHE)
public class DanfuChargeRankAction {
	@Autowired
	private DanfuChargeRankService danfuChargeRankService;

	@EasyMapping(mapping = InnerCmdType.DANFU_CHARGE_RANK)
	public void chargeRecord(Message inMsg) {
		Object[] data = inMsg.getData();
		Long chargeTime = LongUtils.obj2long(data[0]);
		Long yb = LongUtils.obj2long(data[1]);
		Long userRoleId = LongUtils.obj2long(data[2]);
		danfuChargeRankService.handleUserCharge(userRoleId, chargeTime, yb.intValue());
	}
	
	/*@EasyMapping(mapping = InnerCmdType.DANFU_CHARGE_RANK_END_SCHEDULE)
	public void activeEnd(Message inMsg) {
		Object[] data =  inMsg.getData();
		Integer subId = (Integer)data[0];
		Long startTime = LongUtils.obj2long(data[1]);
		Long endTime = LongUtils.obj2long(data[2]);
		danfuChargeRankService.rankReward(subId,startTime,endTime);
	}*/
	
	@EasyMapping(mapping = ClientCmdType.GET_DANFU_CHARGE_RANK_INFO)
	public void getKuafuChargeRankInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data =  inMsg.getData();
		Integer subId = (Integer)data[0];
		Integer version = (Integer)data[1];
		Object[] ret = danfuChargeRankService.getRankInfo(true,userRoleId,version, subId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_DANFU_CHARGE_RANK_INFO,
					ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.GET_DANFU_CHARGE_MY_INFO)
	public void getKuafuChargeMyInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data =  inMsg.getData();
		Integer subId = (Integer)data[0];
		Integer version = (Integer)data[1];
		Object[] ret = danfuChargeRankService.getMyInfo(userRoleId,subId,version);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_DANFU_CHARGE_MY_INFO,
					ret);
		}
	}
	@EasyMapping(mapping = InnerCmdType.MODIFY_NAME_EVENT_5)
	public void modifyNameHandle(Message inMsg) {
		Object[] data =  inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String afterName = (String) data[2];
		danfuChargeRankService.handleUserModifyNameEvent(userRoleId, afterName);
	}
}
