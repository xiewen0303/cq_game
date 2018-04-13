package com.junyou.bus.tianyu.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.tianyu.service.TianYuService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 天羽
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.TIAN_YU,groupName=EasyGroup.BUS_CACHE)
public class TianYuAction {
	
	@Autowired
	private TianYuService tianYuService;
	
	@EasyMapping(mapping = ClientCmdType.TIANYU_SHOW)
	public void tianYuShow(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=tianYuService.tianYuShow(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_SHOW, result);
	}

	@EasyMapping(mapping = ClientCmdType.TIANYU_JJ_UP)
	public void tianYuJjUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=tianYuService.tianYuSj(userRoleId,busMsgQueue,isAutoGm,false);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_JJ_UP, result);
	}
	@EasyMapping(mapping = ClientCmdType.TIANYU_JJ_UP_AUTO)
	public void tianYuJjUpAuto(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=tianYuService.tianYuSj(userRoleId,busMsgQueue,isAutoGm,true);
		busMsgQueue.flush();
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_JJ_UP_AUTO, result);
	}
	
//	//下器灵
//	@EasyMapping(mapping = ClientCmdType.CHIBANG_DOWN,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
//	public void chiBangDown(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		
//		chiBangService.chiBangStage(userRoleId,false);
//	}
	
//	//上器灵
//	@EasyMapping(mapping = ClientCmdType.CHIBANG_UP,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
//	public void chiBangUp(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		
//		chiBangService.chiBangStage(userRoleId,true);
//	}
	
	//器灵外显更新
	@EasyMapping(mapping = ClientCmdType.TIANYU_UPDATE_SHOW,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void tianYuUpdateShow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int showId = inMsg.getData();
		
		Object[] result = tianYuService.tianYuUpdateShow(userRoleId,showId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_UPDATE_SHOW, result);
	}
	
	/**
	 * 天羽战斗力变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_ZPLUS_TIANYU,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void tianYuZplusChange(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long zplus = inMsg.getData(); 
		
		tianYuService.updateTianYuZplus(userRoleId, zplus);
	} 
	/**
	 * 获取天羽的属性，战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TIANYU_ATTR_CHANGE)
	public void getTianYuAttrAndZplus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Map<String ,Long> tianYuAttrs = tianYuService.getTianYuAttr(userRoleId);
		if(tianYuAttrs != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_ATTR_CHANGE, tianYuAttrs);
		}
	}
}
