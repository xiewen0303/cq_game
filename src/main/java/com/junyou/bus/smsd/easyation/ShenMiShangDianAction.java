package com.junyou.bus.smsd.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.smsd.server.ShenMiShangDianService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布神秘商店运营活动
 * @description 
 * @author ZHONGDIAN
 * @date 2015-5-5 下午4:46:05
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class ShenMiShangDianAction {

	@Autowired
	private ShenMiShangDianService shenMiShangDianService;
	
	/**
	 * 请求购买
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_BUY_SMSD)
	public void buy(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer geWeiId = (Integer) data[2];
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = shenMiShangDianService.buy(userRoleId, version, subId, geWeiId, busMsgQueue);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_BUY_SMSD, result);
		}
		busMsgQueue.flush();
	}
	
	/**
	 * 请求刷新
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_SX_SMSD)
	public void shuaxin(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		
		Object[] result = shenMiShangDianService.shuaxin(userRoleId, version, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_SX_SMSD, result);
		}
	}
	/**
	 * 请求检测刷新
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_JCSX_SMSD)
	public void jcShuaxin(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		
		Object[] result = shenMiShangDianService.jcShuaxin(userRoleId, version, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_JCSX_SMSD, result);
		}
	}
	
	
	
}
