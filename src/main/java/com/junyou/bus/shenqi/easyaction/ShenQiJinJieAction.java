package com.junyou.bus.shenqi.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.shenqi.service.ShenQiJinJieService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.SHEN_QI_JIN_JIE, groupName = EasyGroup.BUS_CACHE)
public class ShenQiJinJieAction {
	@Autowired
	private ShenQiJinJieService shenQiJinJieService;
	
	@EasyMapping(mapping = ClientCmdType.GET_SHENQI_JINJIE_INFO)
	public void getShenQiInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = shenQiJinJieService.getShenQiJinjieInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_SHENQI_JINJIE_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.SHENQI_UPGRADE)
	public void upgradeShenQi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer shenQiId = (Integer)data[0];
		Integer curLevel = (Integer)data[1];
		Boolean isAutoGM = (Boolean) data[2];
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result = shenQiJinJieService.startUpgrade(userRoleId, shenQiId,curLevel, busMsgQueue, isAutoGM);
		busMsgQueue.flush();
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHENQI_UPGRADE, result);
	}
	

	/**
	 * 不保存洗练属性
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.SHENQI_EQUIP_GEWEI_CHANGE)
	public void shenQiEquipGeWeiChange(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		shenQiJinJieService.chargeGeWeiChange(userRoleId);
	}
}
