package com.junyou.bus.mall.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.mall.service.MallService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 商城
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-3-17 下午3:36:31
 */
@Component
@EasyWorker(moduleName = GameModType.MALL_MODULE)
public class MallAction {

	@Autowired
	private MallService mallService;
	
	
	@EasyMapping(mapping = ClientCmdType.GET_MALL_DATA)
	public void shopList(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		String version = inMsg.getData();
		
		Object[] result = mallService.shopList(userRoleId,version);
//		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_MALL_DATA, result);
//		}
	}
	
	@EasyMapping(mapping = ClientCmdType.MALL_BUY)
	public void buyStoreGoods(Message inMsg){

		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer id = (Integer)data[0];
		Integer count = (Integer)data[1];
		Object[] result = mallService.buyStoreGoods(userRoleId, id,count);
		BusMsgSender.send2One(userRoleId, ClientCmdType.MALL_BUY, result);
		
	}
	
}
