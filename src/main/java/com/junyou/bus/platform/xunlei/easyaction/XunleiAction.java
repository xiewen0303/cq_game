package com.junyou.bus.platform.xunlei.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.xunlei.service.XunleiService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;


@Component
@EasyWorker(moduleName = GameModType.PLATFORM_XUNLEI)
public class XunleiAction {

	@Autowired
	private XunleiService xunleiService;
	/**
	 * 礼包领取情况
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_XUNLEI_GIFT_STATE)
	public void getGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = xunleiService.getStateByRoleId(userRoleId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_XUNLEI_GIFT_STATE, result);	
		}
	}
	/**
	 *大厅 礼包 
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_XUNLEI_DATING_GIFT)
	public void getTequanGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = xunleiService.getReward(userRoleId,0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_XUNLEI_DATING_GIFT, result);
	}
	/**
	 * 特权礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_XUNLEI_TEQUAN_GIFT)
	public void getDatingGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = xunleiService.getReward(userRoleId,1);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_XUNLEI_TEQUAN_GIFT, result);
		}
	}
	
	/**
	 * 领取VIP礼包
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_XUNLEI_GIFT)
	public void getVipGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer level = (Integer)inMsg.getData();
		Object[]  result = xunleiService.getVipReward(userRoleId,level);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_XUNLEI_GIFT, result);
	}
	/**
	 * 点击迅雷特权超级会员按钮请求信息
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_XUNLEI_SUPER_VIP_INFO)
	public void get37WanGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = xunleiService.getSuperVipInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_XUNLEI_SUPER_VIP_INFO, result);
	}
	/**
	 * 超级会员活动是否关闭
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_XUNLEI_CLOSED)
	public void isCloseActivity(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = xunleiService.isCloseActivity();
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_XUNLEI_CLOSED, result);
	}
}
