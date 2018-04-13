package com.junyou.bus.chibang.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.chibang.service.ChiBangService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 翅膀
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.CHI_BANG,groupName=EasyGroup.BUS_CACHE)
public class ChiBangAction {
	
	@Autowired
	private ChiBangService chiBangService;
	
	
	@EasyMapping(mapping = ClientCmdType.CHIBANG_SHOW)
	public void chiBangShow(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=chiBangService.chiBangShow(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_SHOW, result);
	}

	@EasyMapping(mapping = ClientCmdType.CHIBANG_JJ_UP)
	public void chiBangJjUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=chiBangService.chiBangSj(userRoleId,busMsgQueue,isAutoGm,false);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_JJ_UP, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.CHIBANG_AUTO_UP)
	public void chiBangAutoUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=chiBangService.chiBangSj(userRoleId,busMsgQueue,isAutoGm,true);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_AUTO_UP, result);
	}
	
	
	
//	//下翅膀
//	@EasyMapping(mapping = ClientCmdType.CHIBANG_DOWN,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
//	public void chiBangDown(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		
//		chiBangService.chiBangStage(userRoleId,false);
//	}
	
//	//上翅膀
//	@EasyMapping(mapping = ClientCmdType.CHIBANG_UP,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
//	public void chiBangUp(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		
//		chiBangService.chiBangStage(userRoleId,true);
//	}
	
	//翅膀外显更新
	@EasyMapping(mapping = ClientCmdType.CHIBANG_UPDATE_SHOW,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chiBangUpdateShow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int showId = inMsg.getData();
		
		Object[] result = chiBangService.chiBangUpdateShow(userRoleId,showId,true);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_UPDATE_SHOW, result);
	}
	
	/**
	 * 肢膀战斗力变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_ZPLUS_CHIBANG,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void chiBangZplusChange(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long zplus = inMsg.getData(); 
		
		chiBangService.updateChiBangZplus(userRoleId, zplus);
	} 
	/**
	 * 获取翅膀的属性，战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHIBANG_ATTR_CHANGE)
	public void getChiBangAttrAndZplus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Map<String ,Long> chiBangAttrs = chiBangService.getChiBangAttr(userRoleId);
		if(chiBangAttrs != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_ATTR_CHANGE, chiBangAttrs);
		}
	}
}
