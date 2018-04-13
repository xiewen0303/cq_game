package com.junyou.bus.miaosha.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.miaosha.service.RefbMiaoshaService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2016-3-7 上午11:36:35
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RefbMiaoshaAction {
	@Autowired
	private RefbMiaoshaService refbMiaoshaService;
	
	@EasyMapping(mapping = ClientCmdType.GET_MIAOSHA_INFO)
	public void getMiaoShaInfo(Message inMsg) {
		long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int subId = CovertObjectUtil.object2int(data[0]);
		int version = CovertObjectUtil.object2int(data[1]);
		Object[] result = refbMiaoshaService.getInfo(userRoleId, subId, version);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_MIAOSHA_INFO, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.MIAOSHA_BUY)
	public void miaoShaBuy(Message inMsg) {
		long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int subId = CovertObjectUtil.object2int(data[0]);
		int version = CovertObjectUtil.object2int(data[1]);
		int boxId = CovertObjectUtil.object2int(data[2]);
		Object[] result = refbMiaoshaService.buy(userRoleId, subId, version, boxId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.MIAOSHA_BUY, result);
		}
	}
	
}
