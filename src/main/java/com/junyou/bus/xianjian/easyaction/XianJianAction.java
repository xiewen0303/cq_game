package com.junyou.bus.xianjian.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xianjian.service.XianjianService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 仙剑
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.XIAN_JIAN,groupName=EasyGroup.BUS_CACHE)
public class XianJianAction {
	
	@Autowired
	private XianjianService xianJianService;
	
	@EasyMapping(mapping = ClientCmdType.XIANJIAN_SHOW)
	public void xianJianShow(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=xianJianService.xianJianShow(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_SHOW, result);
	}

	@EasyMapping(mapping = ClientCmdType.XIANJIAN_JJ_UP_COMMON)
	public void xianJianJjUpCommon(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=xianJianService.xianJianSj(userRoleId,busMsgQueue,isAutoGm,false);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_JJ_UP_COMMON, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.XIANJIAN_JJ_UP_AUTO)
	public void xianJianJjUpAuto(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=xianJianService.xianJianSj(userRoleId,busMsgQueue,isAutoGm,true);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_JJ_UP_AUTO, result);
	}
	
	//仙剑外显更新
	@EasyMapping(mapping = ClientCmdType.XIANJIAN_UPDATE_SHOW,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void xianJianUpdateShow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int showId = inMsg.getData();
		
		Object[] result = xianJianService.xianJianUpdateShow(userRoleId,showId,true);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_UPDATE_SHOW, result);
	}
	
	/**
	 * 仙剑战斗力变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_ZPLUS_XIANJIAN,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void xianjianZplusChange(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long zplus = inMsg.getData(); 
		
		xianJianService.updateXianJianZplus(userRoleId, zplus);
	} 
	/**
	 * 获取仙剑的属性，战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XIANJIAN_ATTR_CHANGE)
	public void getXianJianAttrAndZplus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Map<String ,Long> xianJianAttrs = xianJianService.getXianJianAttr(userRoleId);
		if(xianJianAttrs != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_ATTR_CHANGE, xianJianAttrs);
		}
	}
	
}
