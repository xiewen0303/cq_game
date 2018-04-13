package com.junyou.bus.zhuanpan.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zhuanpan.server.ZhuanPanService;
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
public class ZhuanPanAction {

	@Autowired
	private ZhuanPanService zhuanPanService;
	
	/**
	 * 请求转
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ZP_ZHUAN)
	public void buy(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Boolean is = (Boolean) data[2];
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = zhuanPanService.zhuan(userRoleId, version, subId, busMsgQueue,is);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.ZP_ZHUAN, result);
		}
		busMsgQueue.flush();
	}
	
	/**
	 * 请求兑换
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ZP_DUIHUAN)
	public void shuaxin(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer configId = (Integer) data[1];
		Integer version = (Integer) data[2];
		
		Object[] result = zhuanPanService.duihuan(userRoleId, version, subId, configId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.ZP_DUIHUAN, result);
		}
	}
	
	
	
}
