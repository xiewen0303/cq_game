package com.junyou.bus.xunbao.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xunbao.service.RefbXunBaoService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
/**
 * 热发布寻宝
 * @author zhongdian 
 * @email  zhongdian@junyougame.com
 * @date 2015-10-10 下午4:09:54
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RefbXunBaoAction {
	
	@Autowired
	private RefbXunBaoService refbXunBaoService;
	
	@EasyMapping(mapping = ClientCmdType.REFABU_XUNBAO)
	public void xunBao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer xunbaoId = (Integer) data[1];
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result = refbXunBaoService.xunbao(userRoleId, subId, xunbaoId, busMsgQueue);
		
		BusMsgSender.send2One(userRoleId,ClientCmdType.REFABU_XUNBAO,result);
		
		busMsgQueue.flush();
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_XUNBAO_REWARDS)
	public void getQFXBRewards(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer configId = (Integer) data[1];
		Object[] result = refbXunBaoService.getQFXBRewards(userRoleId, subId, configId);
		
		BusMsgSender.send2One(userRoleId,ClientCmdType.GET_XUNBAO_REWARDS,result);
	}
}
