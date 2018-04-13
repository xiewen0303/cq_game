package com.junyou.bus.shoplimit.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.shoplimit.service.ShopLimitService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 限时礼包
 * @author wind
 */
@Controller
@EasyWorker(moduleName = GameModType.XIANSHI_LIBAO,groupName=EasyGroup.BUS_CACHE)
public class ShopLimitAction {
	
	@Autowired
	private ShopLimitService shopLimitService;
	
	@EasyMapping(mapping = ClientCmdType.GET_LIMIT_LIBAO)
	public void getLimitLibao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = shopLimitService.getLimitLibao(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_LIMIT_LIBAO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.BUY_SHOP_LIMIT)
	public void buyShopLimit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int configId = inMsg.getData();
		Object[] result = shopLimitService.buyShopLimit(userRoleId,configId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BUY_SHOP_LIMIT, result);
	}
 }
