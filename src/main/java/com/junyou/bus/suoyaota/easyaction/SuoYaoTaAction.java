package com.junyou.bus.suoyaota.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.suoyaota.service.SuoYaoTaService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-8-4 上午9:34:02
 */
@Controller
@EasyWorker(moduleName = GameModType.SUOYAOTA_MODULE)
public class SuoYaoTaAction {
	@Autowired
	private SuoYaoTaService suoYaoTaService;
	
	@EasyMapping(mapping = ClientCmdType.CANGBAOGE_START_CHOU)
	public void choujiang(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = suoYaoTaService.chou(userRoleId, subId, version,busMsgQueue);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.CANGBAOGE_START_CHOU, result);
			busMsgQueue.flush();
		}
	}
}
