package com.junyou.bus.daomoshouzha.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.daomoshouzha.service.DaoMoShouZhaService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布全民修仙运营活动
 * @description 
 * @author ZHONGDIAN
 * @email 279444454@qq.com
 * @date 2015-5-5 下午4:46:05
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class DaoMoShouZhaAction {

	@Autowired
	private DaoMoShouZhaService daoMoShouZhaService;
	
	/**
	 * 请求追寻
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.DAOMO_ZHUIXUN)
	public void lingquLevelLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer count = (Integer) data[2];
		if(count > 10){
			return;
		}
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = daoMoShouZhaService.zhuixun(userRoleId, version, subId,count,busMsgQueue);
		
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.DAOMO_ZHUIXUN, result);
		}
		busMsgQueue.flush();
	}
	
	
	
}
