package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.WenquanStageService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.WENQUAN_MODULE, groupName = EasyGroup.STAGE)
public class WenquanStageAction {

	@Autowired
	private WenquanStageService wenquanStageService;
	
	@EasyMapping(mapping = ClientCmdType.ENTER_WENQUAN)
	public void enterWenquan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = wenquanStageService.enterWenquan(userRoleId);
		if (ret != null) {
			StageMsgSender.send2One(userRoleId, ClientCmdType.ENTER_WENQUAN,
					ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.EXIT_WENQUAN)
	public void exitWenquan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = wenquanStageService.exitWenquan(userRoleId);
		if (ret != null) {
			StageMsgSender.send2One(userRoleId, ClientCmdType.EXIT_WENQUAN,
					ret);
		}
	}

	/**
	 * 温泉战活动开始
	 */
	@EasyMapping(mapping = InnerCmdType.WENQUAN_START)
	public void wenquanStart(Message inMsg) {
		Object[] data = inMsg.getData();
		Integer hdId = (Integer) data[0];
		wenquanStageService.activeStart(hdId);
	}
	
	
	/**
	 * 温泉加经验
	 */
	@EasyMapping(mapping = InnerCmdType.WENQUAN_ADD_EXP)
	public void wenquanAddExp(Message inMsg) {
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		wenquanStageService.addExpAndZhenqi(stageId,userRoleId);
	}
	
	/**
	 * 进入高倍区
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.WENQUAN_GO_HIGH_AREA)
	public void gotoHighArea(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		wenquanStageService.gotoHighArea(userRoleId, stageId);
	}

}
