package com.junyou.bus.giftcard.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.giftcard.service.GiftCardService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 礼包卡兑换action 
 * @author jy
 *
 */
@Component
@EasyWorker(moduleName = GameModType.GIFT_CARD)
public class GiftCardAction {
	
	@Autowired
	private GiftCardService giftCardService;
	
	/**
	 * 礼包卡兑换
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GIFT_CARD)
	public void getGiftCardReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String cardNo = inMsg.getData();
		int obj = giftCardService.getGiftCardReward(userRoleId, cardNo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GIFT_CARD, obj);
	}
}
