package com.junyou.bus.kuafuluandou.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.kuafuluandou.constants.KuaFuDaLuanDouConstants;
import com.junyou.bus.kuafuluandou.service.KuaFuDaLuanDouService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_DALUANDOU, groupName = EasyGroup.BUS_CACHE)
public class KuafuDaLuanDouAction {

	@Autowired
	private KuaFuDaLuanDouService kuaFuDaLuanDouService;

	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_LUANDOU_BAOMING_INFO)
	public void getBaoMingInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean ret = kuaFuDaLuanDouService.getBaoMingInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_KUAFU_LUANDOU_BAOMING_INFO,ret);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_LUANDOU_HAIXUAN_INFO)
	public void getHaiXuanInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int ret = kuaFuDaLuanDouService.getHaiXuanInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_KUAFU_LUANDOU_HAIXUAN_INFO,ret);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_KUAFU_LUANDOU_BAOMING)
	public void baoMingLuanDou(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuaFuDaLuanDouService.baoMingDaLuanDou(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_KUAFU_LUANDOU_BAOMING,ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.ENTER_KUAFU_LUANDOU)
	public void enterKuafuBoss(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuaFuDaLuanDouService.enterKuafuLuanDou(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_LUANDOU,ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.EXIT_KUAFU_LUANDOU)
	public void exitKuafuDaLuanDou(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuaFuDaLuanDouService.exitKuafuDaLuanDou(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.EXIT_KUAFU_LUANDOU,ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.KUAFU_LUANDOU_FUHUO)
	public void kuafuBossFuhuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuaFuDaLuanDouService.kuafuLuanDouFuhuo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_LUANDOU_FUHUO,ret);
		}
	}
	
	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_LEAVE_FB, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void leaveFb(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuaFuDaLuanDouService.leaveFb(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_ENTER_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterFail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer reason = inMsg.getData();
		if (reason.intValue() == KuaFuDaLuanDouConstants.ENTER_FAIL_REASON_1) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_LUANDOU,AppErrorCode.KUAFU_ENTER_FAIL);
		} else if (reason.intValue() == KuaFuDaLuanDouConstants.ENTER_FAIL_REASON_2) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_LUANDOU,AppErrorCode.KUAFU_DALUANDOU_ACTIVE_FULL);
		}
	}

	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_ENTER_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterXiaoheiwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuaFuDaLuanDouService.enterXiaoheiwu(userRoleId);
	}
	
    @EasyMapping(mapping = ClientCmdType.KUAFU_LUANDOU_GROUP_RANK)
    public void getKuafuDaLuanDouRankData(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] ret = kuaFuDaLuanDouService.getRankData();
        if (ret != null) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_LUANDOU_GROUP_RANK, ret);
        }
    }
	
}
