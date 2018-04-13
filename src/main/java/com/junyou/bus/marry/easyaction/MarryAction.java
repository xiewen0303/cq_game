package com.junyou.bus.marry.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.marry.service.MarryService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-8-13 下午4:46:20
 */
@Controller
@EasyWorker(moduleName = GameModType.MARRY, groupName = EasyGroup.BUS_CACHE)
public class MarryAction {

	@Autowired
	private MarryService marryService;
	
	@EasyMapping(mapping = ClientCmdType.GET_SELF_MARRY_INFO)
	public void getSelfMarryInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.getSelfMarryInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_SELF_MARRY_INFO, result);
	}

	@EasyMapping(mapping = ClientCmdType.APPLY_DINGHUN)
	public void applyDinghun(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.dinghun(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_DINGHUN, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.APPLY_MARRY)
	public void applyMarry(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer xinwu = inMsg.getData();
		Object[] result = marryService.marry(userRoleId,xinwu);
		BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_MARRY, result);
	}

	@EasyMapping(mapping = ClientCmdType.CANCEL_DINGHUN)
	public void cancelDinghun(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.cancelDing(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CANCEL_DINGHUN, result);
		
	}

	@EasyMapping(mapping = ClientCmdType.ADD_YUANFEN)
	public void addYuanfen(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer auto = inMsg.getData();
		Object[] result = marryService.addYuanfen(userRoleId, auto == GameConstants.BOOLEAN_TRUE_TO_INT);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ADD_YUANFEN, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.ADD_QINMIDU)
	public void addQinmidu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer auto = inMsg.getData();
		Object[] result = marryService.addQinmidu(userRoleId, auto == GameConstants.BOOLEAN_TRUE_TO_INT);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ADD_QINMIDU, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_LONGFENG_INFO)
	public void getLongfengInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.getLongfengInfo(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_LONGFENG_INFO, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.AGREE_DINGHUN)
	public void agreeDinghun(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.answerDinghun(userRoleId,true);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.AGREE_DINGHUN, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.AGREE_MARRY)
	public void agreeMarry(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.answerMarry(userRoleId,true);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.AGREE_MARRY, result);
		}
		
	}

	@EasyMapping(mapping = ClientCmdType.CHANGE_MARRY_XINWU)
	public void changeXinwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer xinwu = inMsg.getData();
		Object[] result = marryService.changeXinWu(userRoleId, xinwu);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHANGE_MARRY_XINWU, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_DING_INFO)
	public void getDinghunInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.getDinghunInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_DING_INFO, result);
	}

	@EasyMapping(mapping = ClientCmdType.GET_MARRY_INFO)
	public void getMarryInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.getMarryInfo(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_MARRY_INFO, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.SAVE_TODAY_XINQING)
	public void saveTodayXinqing(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String xinqing = inMsg.getData();
		Object[] result = marryService.changeTodayXinqing(userRoleId,xinqing);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SAVE_TODAY_XINQING, result);
	}

	@EasyMapping(mapping = ClientCmdType.REFUSE_DINGHUN)
	public void refuseDinghun(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.answerDinghun(userRoleId, false);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.REFUSE_DINGHUN, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.REFUSE_MARRY)
	public void refuseMarry(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.answerMarry(userRoleId, false);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.REFUSE_MARRY, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.OPEN_CLOSE_DINGHUN_PANEL)
	public void openCloseDinghunPanel(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer open = inMsg.getData();
		Object[] result = marryService.openCloseDinghunPanel(userRoleId,open == GameConstants.BOOLEAN_TRUE_TO_INT);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.OPEN_CLOSE_DINGHUN_PANEL, result);
		}
	}

	@EasyMapping(mapping = ClientCmdType.OPEN_CLOSE_CHANGE_XINWU_PANEL)
	public void openCloseXinwuPanel(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer open = inMsg.getData();
		Object[] result = marryService.openCloseXinwuPanel(userRoleId,open == GameConstants.BOOLEAN_TRUE_TO_INT);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.OPEN_CLOSE_CHANGE_XINWU_PANEL, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.APPLY_DIVORCE)
	public void applyDivorce(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer type = inMsg.getData();
		Object[] result = marryService.divorce(userRoleId, type);
		BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_DIVORCE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.CANCEL_DIVORCE)
	public void cancelDivorce(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.cancelDivorce(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CANCEL_DIVORCE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.AGREE_DIVORCE)
	public void agreeDivorce(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = marryService.answerDivorce(userRoleId, true);
		BusMsgSender.send2One(userRoleId, ClientCmdType.AGREE_DIVORCE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.REFUSE_DIVORCE)
	public void refuseDivorce(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		 marryService.answerDivorce(userRoleId, false);
		BusMsgSender.send2One(userRoleId, ClientCmdType.REFUSE_DIVORCE, null);
	}
	
	@EasyMapping(mapping = InnerCmdType.MARRY_SCHEDULE_ADD)
	public void marryAddSchedule(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		marryService.addYuanfenOrLove(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.MARRY_SCHEDULE_DIVOCRE)
	public void divorceSchedule(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		marryService.successDivorce(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.MODIFY_NAME_EVENT_4)
	public void peiouChangeName(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String name = inMsg.getData();
		marryService.peiouChangeName(userRoleId, name);
	}
}
