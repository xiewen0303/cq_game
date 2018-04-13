package com.junyou.bus.rfbactivity.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.rfbactivity.service.RefabuActivityService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布运营活动
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RefabuActivityAction {

	@Autowired
	private RefabuActivityService refabuActivityService;
	
	/**
	 * 请求当前充值礼包配置信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RFB_GET_ACTIVITY)
	public void getActivity(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = refabuActivityService.getActivity(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.RFB_GET_ACTIVITY, result);
		}
		
		Object[] iconData = refabuActivityService.retIconFlag(userRoleId);
		if(iconData != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHILD_ICON_FLAG, iconData);
		}
	}
	
	/**
	 * 请求拉取子活动列表
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_ZIACTIVITY)
	public void getZiActivity(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer activityId = (Integer)data[0];
		Integer version = (Integer)data[1];
		
		Object[] result = refabuActivityService.getZiActivity(activityId, version);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZIACTIVITY, result);
		}
	}
	/**
	 * 请求拉取指定子活动的数据
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_ZHIDINGZIACTIVITY)
	public void getZhiDingZiActivity(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer)data[0];
		Integer version = (Integer)data[1];
		
		Object[] result = refabuActivityService.getZhiDingZiActivity(userRoleId,subId, version);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, result);
		}
	}
	
}
