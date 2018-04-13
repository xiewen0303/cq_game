package com.junyou.bus.wuqi.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.service.WuQiService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 武器
 * @author  作者：wind
 * @version 创建时间：2017-5-31 上午11:59:30
 */
@Controller
@EasyWorker(moduleName = GameModType.WU_QI,groupName=EasyGroup.BUS_CACHE)
public class WuQiAction {
	
	@Autowired
	private WuQiService wuQiService;
	
	@EasyMapping(mapping = ClientCmdType.WUQI_SHOW)
	public void wuqiShow(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=wuQiService.wuqiShow(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_SHOW, result);
	}

	@EasyMapping(mapping = ClientCmdType.WUQI_JJ_UP)
	public void wuqiJjUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result = wuQiService.wuQiSj(userRoleId,busMsgQueue,isAutoGm);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_JJ_UP, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.WUQI_AUTO_UP)
	public void wuqiAutoUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=wuQiService.autoWuQiSj(userRoleId,busMsgQueue,isAutoGm);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_AUTO_UP, result);
	}
	
	//新圣剑外显更新
	@EasyMapping(mapping = ClientCmdType.WUQI_UPDATE_SHOW,kuafuType=EasyKuafuType.KF2S_HANDLE_TYPE)
	public void wuqiUpdateShow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int showId = inMsg.getData();
		
		Object[] result = wuQiService.wuqiUpdateShow(userRoleId,showId,true);
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_UPDATE_SHOW, result);
	} 
	
//	TODO wind
//	//新圣剑上下状态切换持久化
//	@EasyMapping(mapping = InnerCmdType.WUQI_BUS_STATE,kuafuType=EasyKuafuType.KF2S_HANDLE_TYPE)
//	public void zuoqiBusState(Message inMsg) {
//		Long userRoleId = inMsg.getRoleId();
//		int state = inMsg.getData(); 
//		
//		wuQiService.zuoqiBusState(userRoleId,state);
//	}

	/**
	 * 更新新圣剑战斗力
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_WUQI_ZPLUS_CHANGE,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void wuqiZplusChange(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long zplus = inMsg.getData();
		
		wuQiService.updateWuQiZplus(userRoleId, zplus);
	}
	
	/**
	 * 获取新圣剑的属性，战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.WUQI_ATTR_CHANGE)
	public void getWuqiAttrAndZplus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Map<String ,Long> zuoQiAttrs = wuQiService.getWuqiAttr(userRoleId);
		if(zuoQiAttrs != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_ATTR_CHANGE, zuoQiAttrs);
		}
	}
}
