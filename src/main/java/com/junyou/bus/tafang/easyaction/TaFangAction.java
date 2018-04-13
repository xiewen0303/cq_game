package com.junyou.bus.tafang.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tafang.service.RoleTaFangService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-10-10 上午10:13:02
 */
@Controller
@EasyWorker(moduleName = GameModType.TAFANG,groupName = EasyGroup.BUS_CACHE)
public class TaFangAction {
	
	@Autowired
	private RoleTaFangService roleTaFangService;
	
	@EasyMapping(mapping = InnerCmdType.TAFANG_ADD_EXP)
	public void addTaFangExp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String monsterId = inMsg.getData();
		roleTaFangService.addExp(userRoleId, monsterId);
	}
	
	@EasyMapping(mapping = ClientCmdType.TAFANG_ENTER)
	public void enterTaFang(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result =roleTaFangService.enterTaFang(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TAFANG_ENTER, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.TAFANG_GET_INFO)
	public void getInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = roleTaFangService.getTaFangInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAFANG_GET_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.TAFANG_BUILD_TAFANG)
	public void buildTaFang(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] result = roleTaFangService.putTa(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAFANG_BUILD_TAFANG, result);
	}

	@EasyMapping(mapping = ClientCmdType.TAFANG_TAFANG_LEVEL_UP)
	public void taLevelUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer id = CovertObjectUtil.object2int(data[0]);
		Integer type = CovertObjectUtil.object2int(data[1]);
		Object[] result = roleTaFangService.npcLevelUp(userRoleId, id, type);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAFANG_TAFANG_LEVEL_UP, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.TAFANG_RECIVE_EXP)
	public void reciveExp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] result = roleTaFangService.reciveExp(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAFANG_RECIVE_EXP, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.TAFANG_EXIT)
	public void exitTaFang(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		roleTaFangService.exitTaFang(userRoleId);
	}
	
	
}
