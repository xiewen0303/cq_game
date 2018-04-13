package com.junyou.bus.xiuxian.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiuxian.service.RfbXiuXianService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 修仙礼包
 * @author DaoZheng Yuan
 * 2015年6月7日 下午2:20:20
 */
@Controller
@EasyWorker(moduleName = GameModType.XIUXIAN)
public class XiuXianEasyAction {

	@Autowired
	private RfbXiuXianService rfbXiuXianService;
	
	/**
	 * 10180 请求买并领取礼包
	 */
	@EasyMapping(mapping = ClientCmdType.BUY_XIUXIAN_LIBAO)
	public void buyXiuXianLiBao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer buyId = (Integer) data[2];
		
		Object result = rfbXiuXianService.buyXiuXianLiBao(userRoleId, subId, version, buyId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.BUY_XIUXIAN_LIBAO, result);
		}
	}
}
