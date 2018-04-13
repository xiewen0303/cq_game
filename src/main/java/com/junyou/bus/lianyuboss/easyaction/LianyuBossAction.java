package com.junyou.bus.lianyuboss.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.lianyuboss.service.LianyuBossService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author  lxn
 * 2015-11-3 上午11:44:34
 */
@Controller
@EasyWorker(moduleName = GameModType.GUILD_BOSS_LIANYU, groupName = EasyGroup.BUS_CACHE)
public class LianyuBossAction {
	@Autowired
	private LianyuBossService lianyuBossService;
	
	@EasyMapping(mapping = ClientCmdType.LIANYU_BOSS_ENTER)
	public void enterPata(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer cengId = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		lianyuBossService.enterTiaozhan(userRoleId, cengId, busMsgQueue);
		busMsgQueue.flush();
	}
	@EasyMapping(mapping = ClientCmdType.LIANYU_BOSS_CURRENT_INFO)
	public void getCurrentTonguanInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = lianyuBossService.getCurrentTonguanInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LIANYU_BOSS_CURRENT_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.LIANYU_BOSS_TONGGUAN_INFO)
	public void getInfoByConfigId(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int configId = inMsg.getData();
		Object[] result = lianyuBossService.getInfoByConfigId(configId,userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LIANYU_BOSS_TONGGUAN_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.LIANYU_BOSS_EXIT)
	public void exitLian(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		lianyuBossService.exitLianyu(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.LIANYU_BOSS_EXIT)
	public void hasExitLianFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		lianyuBossService.hasExitPata(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.LIANYU_BOSS_FINISH_2_BUS)
	public void finishLianyu(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data  = inMsg.getData();
		int configId = (Integer)data[0];
		Long enterTime = (Long)data[1]; 
		Long completeTime = (Long)data[2];
 		lianyuBossService.tongguanSuccess(userRoleId,configId,enterTime,completeTime.intValue());
	}
	
	
}
