package com.junyou.bus.rfbactivity.service;

import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;

public abstract class AbstractActivityService implements IActivityService {

	@Override
	public abstract boolean getChildFlag(long userRoleId, int subId);
	
	public void updateCheck(long userRoleId, int subId){
		//建议每个方法都实现该检测方法
	}
	
	/**
	 * 检查是否还有角标需要领奖
	 */
	@Override
	public void checkIconFlag(long userRoleId, int subId) {
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSon != null){
			boolean flag = getChildFlag(userRoleId, subId);
			//如果没有奖励可领取了，推送客服端关闭对应的提示
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHILD_ICON_FLAG, new Object[]{new int[]{subId,flag?1:0}});
		}
	}

}
