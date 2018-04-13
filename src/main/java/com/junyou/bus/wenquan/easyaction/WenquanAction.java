package com.junyou.bus.wenquan.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wenquan.service.WenquanService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.WenquanStageService;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.WENQUAN_MODULE)
public class WenquanAction {
	@Autowired
	private WenquanService wenquanService;
	@Autowired
	private WenquanStageService wenquanStageService;
	@EasyMapping(mapping = ClientCmdType.GET_WENQUAN_INFO)
	public void getWenquanInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = wenquanService.getWenquanInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_WENQUAN_INFO,
					ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_WENQUAN_RANK)
	public void getWenquanRank(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data =  inMsg.getData();
		Integer pageIndex = (Integer)data[0];
		Integer recordsPerPage = (Integer)data[1];
		Object[] ret = wenquanService.getRankList(pageIndex,recordsPerPage);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_WENQUAN_RANK,
					ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.WENQUAN_JU_LIN)
	public void juLin(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer type = inMsg.getData();
		Object[] ret = wenquanService.julin(userRoleId, type);
		if (ret != null) {
			BusMsgSender
					.send2One(userRoleId, ClientCmdType.WENQUAN_JU_LIN, ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.WENQUAN_PLAY_1)
	public void play1(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long targetUserRoleId = LongUtils.obj2long(inMsg.getData());
		Object[] ret = wenquanService.play(userRoleId, targetUserRoleId,0);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.WENQUAN_PLAY_1, ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.WENQUAN_PLAY_2)
	public void play2(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long targetUserRoleId = LongUtils.obj2long(inMsg.getData());
		Object[] ret = wenquanService.play(userRoleId, targetUserRoleId,1);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.WENQUAN_PLAY_2, ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.WENQUAN_RANK_SYN)
	public void rankSyn(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = wenquanService.rankSyn(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.WENQUAN_RANK_SYN, ret);
		}
	}
	
	/**
	 * 温泉战活动开始
	 */
	@EasyMapping(mapping = InnerCmdType.WENQUAN_END)
	public void wenquanEnd(Message inMsg) {
		wenquanStageService.activeEnd();
	}
	/**
	 * 处理玩家改名
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MODIFY_NAME_EVENT_2)
	public void modifyNameHandle(Message inMsg) {
		Object[] data =  inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String afterName = (String) data[2];
		wenquanService.handleUserModifyNameEvent(userRoleId, afterName);
	}
	
}
