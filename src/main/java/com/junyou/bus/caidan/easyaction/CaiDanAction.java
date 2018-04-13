package com.junyou.bus.caidan.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.caidan.service.RoleCaidanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-9-16 下午6:16:25
 */
@Controller
@EasyWorker(moduleName = GameModType.CAIDAN)
public class CaiDanAction {
	@Autowired
	private RoleCaidanService roleCaidanService;
	
	@EasyMapping(mapping = ClientCmdType.GET_CAIDAN_CONFIG_INFO)
	public void getCaiDanInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = CovertObjectUtil.object2int(data[0]);
		Integer version = CovertObjectUtil.object2int(data[1]);
		Object[] result = roleCaidanService.getInfo(userRoleId, version, subId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CAIDAN_CONFIG_INFO, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_CAIDAN_DUIHUAN_CONFIG)
	public void getDuihuanConfig(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer subId = inMsg.getData();
		Object[] result = roleCaidanService.getCaidanDuihuanConfig(subId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CAIDAN_DUIHUAN_CONFIG, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_CAIDAN_ZADAN_LOGS)
	public void getZaDanLogs(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer subId = inMsg.getData();
		Object[] result = roleCaidanService.getAllCaidanLog(subId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CAIDAN_ZADAN_LOGS, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.CAIDAN_RESET)
	public void caidanReset(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer subId = inMsg.getData();
		Object[] result = roleCaidanService.resetCaidan(userRoleId, subId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CAIDAN_RESET, result);
		
	}
	
	@EasyMapping(mapping = ClientCmdType.CAIDAN_ZADAN_ONE)
	public void zadanOne(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = CovertObjectUtil.object2int(data[0]);
		Integer version = CovertObjectUtil.object2int(data[1]);
		Integer id = CovertObjectUtil.object2int(data[2]);
		Object[] result = roleCaidanService.zaOneEgg(userRoleId, subId, version, id);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CAIDAN_ZADAN_ONE, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.CAIDAN_ZADAN_ALL)
	public void zadanAll(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = CovertObjectUtil.object2int(data[0]);
		Integer version = CovertObjectUtil.object2int(data[1]);
		Object[] result = roleCaidanService.zaAllEggs(userRoleId, subId, version);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CAIDAN_ZADAN_ALL, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.CAIDAN_DUIHUAN)
	public void duihuan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = CovertObjectUtil.object2int(data[0]);
		Integer version = CovertObjectUtil.object2int(data[1]);
		String itemId = CovertObjectUtil.object2String(data[2]);
		Object[] result = roleCaidanService.duihuanItem(userRoleId, subId, version, itemId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CAIDAN_DUIHUAN, result);
		}
	}
}
