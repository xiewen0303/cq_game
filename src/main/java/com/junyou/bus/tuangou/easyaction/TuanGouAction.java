package com.junyou.bus.tuangou.easyaction;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tuangou.service.TuanGouService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 团购
 * @author zhongdian
 * 2015年6月7日 下午2:20:20
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class TuanGouAction {

	@Autowired
	private TuanGouService tuanGouService;
	
	/**
	 *  请求买并领取礼包
	 */
	@EasyMapping(mapping = ClientCmdType.TUANGOU_BUY)
	public void buyQiangGouLiBao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer subId = inMsg.getData();
		
		Object[] result = null;;
		try {
			result = tuanGouService.buy(userRoleId, subId);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TUANGOU_BUY, result);
		}
	}
	/**
	 *  请求全服购买次数
	 */
	@EasyMapping(mapping = ClientCmdType.TUANGOU_GQT_SCOUNT)
	public void getsCount(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer subId = inMsg.getData();
		
		Object[] result = tuanGouService.getScount(userRoleId, subId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TUANGOU_GQT_SCOUNT, result);
		}
	}
}
