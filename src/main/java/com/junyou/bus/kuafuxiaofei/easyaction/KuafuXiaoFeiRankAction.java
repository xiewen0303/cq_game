package com.junyou.bus.kuafuxiaofei.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.kuafuxiaofei.service.KuafuXiaoFeiRankService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAF_CHARGE_RANK, groupName = EasyGroup.BUS_CACHE)
public class KuafuXiaoFeiRankAction {
	@Autowired
	private KuafuXiaoFeiRankService kuafuXiaoFeiRankService;

	@EasyMapping(mapping = InnerCmdType.KUAFU_XIAOFEI_RANK)
	public void chargeRecord(Message inMsg) {
		Object[] data = inMsg.getData();
		Long xiaoFeiTime = LongUtils.obj2long(data[0]);
		Long yb = LongUtils.obj2long(data[1]);
		Long userRoleId = LongUtils.obj2long(data[2]);
		kuafuXiaoFeiRankService.handleUserXiaoFei(userRoleId, xiaoFeiTime, yb.intValue());
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_XIAOFEI_RANK)
	public void getKuafuChargeRankInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data =  inMsg.getData();
		Integer subId = (Integer)data[0];
		Integer version = (Integer)data[1];
		Integer pageIndex = (Integer)data[2];
		Integer recordsPerPage = (Integer)data[3];
		Object[] ret = kuafuXiaoFeiRankService.getRankInfoList(true,userRoleId,version, subId, pageIndex, recordsPerPage);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_KUAFU_XIAOFEI_RANK,
					ret);
		}
	}
	
	
	
	@EasyMapping(mapping = InnerCmdType.MODIFY_NAME_EVENT_6)
	public void modifyNameHandle(Message inMsg) {
		Object[] data =  inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String afterName = (String) data[2];
		kuafuXiaoFeiRankService.handleUserModifyNameEvent(userRoleId, afterName);
	}
}
