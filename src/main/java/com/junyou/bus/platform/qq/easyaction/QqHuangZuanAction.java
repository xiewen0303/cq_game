package com.junyou.bus.platform.qq.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.service.QqHuangZuanGiftService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqHuangZuanAction {
	@Autowired
	private QqHuangZuanGiftService qqHuangZuanGigtService;
	@EasyMapping(mapping = ClientCmdType.GET_QQ_GIFT_STATUS)
	public void getGiftStatus(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer ret = qqHuangZuanGigtService.getGiftStatus(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_QQ_GIFT_STATUS, ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_QQ_XINSHOU_GIFT)
	public void getXinshouGift(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqHuangZuanGigtService.getXinShouGift(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_QQ_XINSHOU_GIFT, ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_QQ_DENGJI_GIFT)
	public void getChengZhangGift(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer n = inMsg.getData();
		Object[] ret = qqHuangZuanGigtService.getChengZhangGift(userRoleId,n);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_QQ_DENGJI_GIFT,
					ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_QQ_MEIRI_GIFT_STATUS)
	public void getMeiRiGiftStatus(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Boolean ret = qqHuangZuanGigtService.getMeiriGiftStatus(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_QQ_MEIRI_GIFT_STATUS,
					ret);
		}
	}


	@EasyMapping(mapping = ClientCmdType.GET_QQ_MEIRI_GIFT)
	public void getMeiriGift(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqHuangZuanGigtService.getMeiriGift(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_QQ_MEIRI_GIFT, ret);
		}
	}
}
