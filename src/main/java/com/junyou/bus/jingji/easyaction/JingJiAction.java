package com.junyou.bus.jingji.easyaction;

import com.junyou.bus.jingji.service.FightPowerService;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.jingji.service.JingjiService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.JINGJI_MODULE)
public class JingJiAction {
	@Autowired
	private JingjiService jingjiService;

	@Autowired
	private FightPowerService fightPowerService;
	
	@EasyMapping(mapping = ClientCmdType.GET_JINGJI_INFO)
	public void getJingjiInfo(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = jingjiService.getJingjiInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.GET_JINGJI_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_TOP_THREE_INFO)
	public void getTopThreeInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.getTopThree();
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TOP_THREE_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.BUY_TIAOZHAN_COUNT)
	public void buyTiaoZhanCount(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.buyCount(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BUY_TIAOZHAN_COUNT, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.MIAO_TIAOZHAN_CD)
	public void miaoCd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.miaoCd(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.MIAO_TIAOZHAN_CD, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.JINGJI_GUWU)
	public void guwu(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.guwu(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_GUWU, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.JINGJI_TIAOZHAN)
	public void tiaozhan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer rank = inMsg.getData();
		Object[] result = jingjiService.tiaoZhan(userRoleId, rank);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_TIAOZHAN, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.JINGJI_RECIVE_GIFT)
	public void reciveGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.reciveGift(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_RECIVE_GIFT, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.JINGJI_DUIHUAN_ITEM)
	public void duihuan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String goodsId = inMsg.getData();
		Object[] result = jingjiService.duihuanItem(userRoleId, goodsId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_DUIHUAN_ITEM, result);
	}
	@EasyMapping(mapping = ClientCmdType.EXIT_JINGJI_WATCH)
	public void exitWatch(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		jingjiService.exitFight(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.ENTER_SAFE_SUCCESS_START_FIGHT)
	public void startFight(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		jingjiService.fight(userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_JINGJI_DUIHUAN_INFO)
	public void getDuihuanInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.getJingjiDuihuanInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_JINGJI_DUIHUAN_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_JINGJI_PAIMING_INFO)
	public void getRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer rank = inMsg.getData();
		jingjiService.getJingjiRankInfo(userRoleId,rank);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_JINGJI_TIAOZHAN_REPORT)
	public void getReports(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.getJingjiReports(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_JINGJI_TIAOZHAN_REPORT, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.JINGJI_IS_RECIVE_GIFT)
	public void isReciveGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object result = jingjiService.isReciveGift(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_IS_RECIVE_GIFT, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.JINGJI_STATE_VALUE)
	public void getHuoDongDaTingStateValue(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object result = jingjiService.getHuoDongDaTing(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_STATE_VALUE, result);
	}
	
	/**请求我要变强排名*/
	@EasyMapping(mapping = ClientCmdType.RANK_SOFT)
	public void rankSoft(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int reuslt = jingjiService.rankSoft(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RANK_SOFT, reuslt);
	}

	@EasyMapping(mapping = ClientCmdType.REFRESH_TIAOZHAN)
	public void refreshTiaozhan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = jingjiService.refreshTiaozhan(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.REFRESH_TIAOZHAN, result);
	}

	@EasyMapping(mapping = ClientCmdType.FIGHT_VAL_COMPARE1)
	public void fightValCompare1(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long targetUserId = CovertObjectUtil.obj2long(inMsg.getData());

		Object result = fightPowerService.fightValCompareParent(userRoleId,targetUserId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FIGHT_VAL_COMPARE1, result);
	}

	@EasyMapping(mapping = ClientCmdType.FIGHT_VAL_COMPARE2)
	public void fightValCompare2(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Object result = fightPowerService.fightValCompareChild(CovertObjectUtil.object2int(data[0]),userRoleId,CovertObjectUtil.object2Long(data[1]));
		BusMsgSender.send2One(userRoleId, ClientCmdType.FIGHT_VAL_COMPARE2, result);
	}
	
	
	
	@EasyMapping(mapping = ClientCmdType.FIGHT_VAL_COMPARE3)
	public void fightValCompare3(Message inMsg){
		Long userRoleId = inMsg.getRoleId();

		Object result = fightPowerService.fightValCompareParent(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FIGHT_VAL_COMPARE3, result);
	}

	@EasyMapping(mapping = ClientCmdType.FIGHT_VAL_COMPARE4)
	public void fightValCompare4(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int type = inMsg.getData();
		Object result = fightPowerService.fightValCompareChild(type,userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FIGHT_VAL_COMPARE4, result);
	}
}
