package com.junyou.bus.kuafu_qunxianyan.easyaction;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.kuafu_boss.constants.KuafubossConstants;
import com.junyou.bus.kuafu_qunxianyan.service.KuafuQunXianYanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_QUNXIANYAN, groupName = EasyGroup.BUS_CACHE)
public class KuafuQunXianYanAction {

	@Autowired
	private KuafuQunXianYanService kuafuQunXianYanService;

	@EasyMapping(mapping = ClientCmdType.ENTER_KUAFU_QUNXIANYAN)
	public void enterKuafuBoss(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuQunXianYanService.enterKuafuBoss(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_QUNXIANYAN,ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.EXIT_KUAFU_QUNXIANYAN)
	public void exitKuafuBoss(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuQunXianYanService.exitKuafuBoss(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.EXIT_KUAFU_QUNXIANYAN,
					ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.KUAFU_QUNXIANYAN_FUHUO)
	public void kuafuBossFuhuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = kuafuQunXianYanService.kuafuBossFuhuo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_QUNXIANYAN_FUHUO,
					ret);
		}
	}
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_LEAVE_FB, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void leaveFb(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		 kuafuQunXianYanService.leaveFb(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_ENTER_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterFail(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer reason = inMsg.getData();
		if (reason.intValue() == KuafubossConstants.ENTER_FAIL_REASON_1) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_QUNXIANYAN,AppErrorCode.KUAFU_ENTER_FAIL);
		} else if (reason.intValue() == KuafubossConstants.ENTER_FAIL_REASON_2) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_QUNXIANYAN,AppErrorCode.KUAFU_BOSS_ACTIVE_FULL);
		}
	}
	/***
	 *采集物品 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_CJ_ITEM, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void caijiGood(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		String item = (String) obj[0];
		Integer count = (Integer) obj[1];
		
		kuafuQunXianYanService.caiJiGood(userRoleId, item, count);
	}
	/***
	 *发奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_JIESUAN_EMAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void fajiang(Message inMsg) {
		Object[] obj = inMsg.getData();
		Long userRoleId = (Long) obj[0];
		Integer rank = (Integer) obj[1];
		kuafuQunXianYanService.emailJiangli(userRoleId, rank);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_ENTER_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterXiaoheiwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuQunXianYanService.enterXiaoheiwu(userRoleId);
	}
	/**
	 * 请求排行榜
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_QUNXIANYAN_RANK)
	public void getRank(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuQunXianYanService.getRank(userRoleId);
	}
	/**
	 * 请求排行榜
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.KUAFU_QUNXIANYAN_LINGQU)
	public void lingqu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer rank = inMsg.getData();
		//kuafuQunXianYanService.lingQuJiangLi(userRoleId,rank);
	}
}
