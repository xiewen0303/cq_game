package com.junyou.bus.qiling.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.qiling.service.QiLingService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 器灵
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.QI_LING,groupName=EasyGroup.BUS_CACHE)
public class QiLingAction {
	
	@Autowired
	private QiLingService qiLingService;
	
	@EasyMapping(mapping = ClientCmdType.QILING_SHOW)
	public void qiLingShow(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=qiLingService.qiLingShow(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_SHOW, result);
	}

	@EasyMapping(mapping = ClientCmdType.QILING_JJ_UP)
	public void qiLingJjUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=qiLingService.qiLingSj(userRoleId,busMsgQueue,isAutoGm);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_JJ_UP, result);
	}
	@EasyMapping(mapping = ClientCmdType.QILING_JJ_UP_AUTO)
	public void qiLingJjUpAuto(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=qiLingService.qiLingSj(userRoleId,busMsgQueue,isAutoGm);
		busMsgQueue.flush();
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_JJ_UP_AUTO, result);
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
	@EasyMapping(mapping = ClientCmdType.QILING_UPDATE_SHOW,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void qiLingUpdateShow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int showId = inMsg.getData();
		
		Object[] result = qiLingService.qiLingUpdateShow(userRoleId,showId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_UPDATE_SHOW, result);
	}
	
	/**
	 * 器灵战斗力变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_ZPLUS_QILING,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void qiLingZplusChange(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long zplus = inMsg.getData(); 
		
		qiLingService.updateQiLingZplus(userRoleId, zplus);
	} 
	/**
	 * 获取器灵的属性，战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.QILING_ATTR_CHANGE)
	public void getQiLingAttrAndZplus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Map<String ,Long> qiLingAttrs = qiLingService.getQiLingAttr(userRoleId);
		if(qiLingAttrs != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_ATTR_CHANGE, qiLingAttrs);
		}
	}
}
