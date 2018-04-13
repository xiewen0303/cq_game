package com.junyou.bus.shouchong.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.shouchong.service.RefbRoleShouchongService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 首充
 */
@Controller
@EasyWorker(moduleName = GameModType.CHONGZHI)
public class ShouChongAction {

	@Autowired
	private RefbRoleShouchongService refbRoleShouchongService;
	
	/**
	 * 领取首充礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_SC)
	public void receiveShouChong(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		
		Object[] result = refbRoleShouchongService.receiveShouChongJianLi(userRoleId, subId,version);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_SC, result);
		}
	}
	
	/**
	 * 每日充值领奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LOOP_DAY_CHONGZHI_LJ)
	public void loopDayChongZhiLj(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer subConfigId = (Integer) data[2];
		
		Object[] result = refbRoleShouchongService.receiveLoopDayChongJianLi(userRoleId, subId,version,subConfigId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.LOOP_DAY_CHONGZHI_LJ, result);
		}
	}
}
