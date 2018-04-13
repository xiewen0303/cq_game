package com.junyou.bus.jewel.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.jewel.service.JewelService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

/**
 * 宝石
 * @author lxn
 *
 */
@Component
@EasyWorker(moduleName = GameModType.JEWEL)
public class JewelAction {
     @Autowired
     private JewelService jewelService;

	@EasyMapping(mapping = ClientCmdType.JEWEL_INIT)
	public void init(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = jewelService.initPanelData(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JEWEL_INIT, result);
	}
	@EasyMapping(mapping = ClientCmdType.JEWEL_UP)
	public void up(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int gwId  = (Integer)data[0];
		int kId  = (Integer)data[1];
		long guid=LongUtils.obj2long(data[2]);
		Object[]  result = jewelService.jewelUp(userRoleId, gwId, kId, guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JEWEL_UP, result);
	}
	@EasyMapping(mapping = ClientCmdType.JEWEL_DOWN)
	public void down(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int gwId  = (Integer)data[0];
		int kId  = (Integer)data[1];
		Object[]  result = jewelService.jewelDown(userRoleId, gwId, kId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JEWEL_DOWN, result);
	}
	@EasyMapping(mapping = ClientCmdType.JEWEL_BURROW)
	public void burrow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int gwId  = (Integer)data[0];
		int kId  = (Integer)data[1];
		Object[]  result = jewelService.jewelBurrow(userRoleId, gwId, kId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JEWEL_BURROW, result);
	}
	@EasyMapping(mapping = ClientCmdType.JEWEL_COMPOUND)
	public void compound(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		long guid=LongUtils.obj2long(data[0]);
		int num  = (Integer)data[1];
		Object[]  result = jewelService.jewelCompound(userRoleId, guid, num);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JEWEL_COMPOUND, result);
	}
}
