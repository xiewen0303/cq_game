package com.junyou.bus.platform.sogou.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.sogou.service.SogouService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.PLATFORM_SOGOU)
public class SogouAction {

	@Autowired
	private SogouService sogouService;
	/**
	 * 礼包领取情况
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SOGOU_GIFT_STATE)
	public void getGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int  result = sogouService.getStateByRoleId(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SOGOU_GIFT_STATE, result);
	}
	/**
	 * 大厅0
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SOGOU_GIFT_DATING)
	public void getDatingGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = sogouService.getReward(userRoleId, PlatformPublicConfigConstants.SOGOU_WANCLIENT_MINI);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SOGOU_GIFT_DATING,result );
		}
	}
	/**
	 * 皮肤1
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SOGOU_GIFT_PIFU)
	public void getPifuGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = sogouService.getReward(userRoleId,PlatformPublicConfigConstants.SOGOU_WANCLIENT_SKIN);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SOGOU_GIFT_PIFU, result);
		}
	}
	
	/**
	 * 超级会员请求信息
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SUPER_QQ_STATE)
	public void get37WanGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = sogouService.getSuperVipInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SUPER_QQ_STATE, result);
	}
	 */
	/**
	 * 超级会员活动是否关闭
	 
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SUPER_QQ_CLOSE)
	public void isCloseActivity(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = sogouService.isCloseActivity();
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SUPER_QQ_CLOSE, result);
	}
	*/
	
}
