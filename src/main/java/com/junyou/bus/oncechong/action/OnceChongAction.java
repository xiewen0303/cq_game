package com.junyou.bus.oncechong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.oncechong.service.OnceChongService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 单笔充值奖励 
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class OnceChongAction {

	@Autowired
	private OnceChongService onceChongService;
	
	/**
	 * 请求领取
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_ONCECHONG)
	public void award(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = onceChongService.lingqu(userRoleId, version, subId, configId);
		
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_ONCECHONG, result);
		}
	}
}