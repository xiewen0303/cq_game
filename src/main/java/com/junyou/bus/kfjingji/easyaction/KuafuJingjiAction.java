package com.junyou.bus.kfjingji.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.kfjingji.service.KuafuJingjiService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-10-29 下午2:19:49
 */
@Controller
@EasyWorker(moduleName = GameModType.KUAFU_JINGJI, groupName = EasyGroup.BUS_CACHE)
public class KuafuJingjiAction {

	@Autowired
	private KuafuJingjiService kuafuJingjiService;
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_GET_INFO)
	public void getInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = kuafuJingjiService.getFighter(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_GET_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_BUY_COUNT)
	public void buyCount(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = kuafuJingjiService.buyCount(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_BUY_COUNT, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_MIAO_CD)
	public void miaoCd(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = kuafuJingjiService.miaoCd(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_MIAO_CD, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_TIAOZHAN)
	public void tiaozhan(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer rank = inMsg.getData();
		Object[] result = kuafuJingjiService.tiaoZhan(userRoleId, rank);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_TIAOZHAN, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_RECIVE)
	public void recive(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = kuafuJingjiService.reciveGift(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_RECIVE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_EXIT)
	public void exit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuJingjiService.exitFight(userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_RANK_PLAYERS)
	public void getRankPlayers(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer rank = inMsg.getData();
		Object[] result = kuafuJingjiService.getRankPlayers(rank);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_RANK_PLAYERS, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_FIGHT_LIST)
	public void fightList(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = kuafuJingjiService.getFightRecords(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_FIGHT_LIST, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_RECIVE_INFO)
	public void giftInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = kuafuJingjiService.giftInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_RECIVE_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_CAN_RECIVE)
	public void havaGift(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int state = kuafuJingjiService.canRciveGift(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_CAN_RECIVE, state);
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_JINGJI_CHANGE_TARGETS)
	public void change(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = kuafuJingjiService.changeTargets(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_CHANGE_TARGETS, result);
	}
	@EasyMapping(mapping = InnerCmdType.ENTER_SAFE_SUCCESS_START_FIGHT_KF)
	public void startFight(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		kuafuJingjiService.fight(userRoleId);
	}
	
}
