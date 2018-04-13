package com.junyou.bus.email.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.email.service.EmailRelationService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
 
/**
 * 邮件系统
 * @author LiuYu
 * @date 2014-12-31 下午3:21:28
 */
@Component
@EasyWorker(moduleName = GameModType.EMAIL_MODULE)
public class EmailAction {

	@Autowired
	private EmailRelationService emailRelationService;
	
	@EasyMapping(mapping = ClientCmdType.GET_ALL_EMAIL)
	public void getAllEmails(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = emailRelationService.loadAllEmails(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ALL_EMAIL, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.RECIVE_EMAIL)
	public void reciveEmail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long relationId = LongUtils.obj2long(inMsg.getData());
		Object[] result = emailRelationService.reciveEmail(userRoleId,relationId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RECIVE_EMAIL, result);
	}
	@EasyMapping(mapping = ClientCmdType.EMAIL_ONCE_RECIVE)
	public void onceReciveEmail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = emailRelationService.onceReciveEmail(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.EMAIL_ONCE_RECIVE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.READ_EMAIL)
	public void readEmail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long relationId = LongUtils.obj2long(inMsg.getData());
		Object[] result = emailRelationService.readEmail(userRoleId,relationId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.READ_EMAIL, result);
	}

	@EasyMapping(mapping = ClientCmdType.DELETE_EMAIL)
	public void delEmail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] relationIds = inMsg.getData();
		//long relationId = LongUtils.obj2long(inMsg.getData());
		Object[] result = emailRelationService.playerDelEmail(userRoleId,relationIds);
		BusMsgSender.send2One(userRoleId, ClientCmdType.DELETE_EMAIL, result);
	}
	
	
}
