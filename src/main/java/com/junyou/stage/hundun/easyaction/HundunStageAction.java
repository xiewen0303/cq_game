package com.junyou.stage.hundun.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.hundun.service.HundunStageService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-9-7 下午4:52:43
 */
@Controller
@EasyWorker(moduleName = GameModType.HUNDUN, groupName = EasyGroup.STAGE)
public class HundunStageAction {

	@Autowired
	private HundunStageService hundunStageService;
	
	@EasyMapping(mapping = ClientCmdType.CHAOS_ENTER_NEXT)
	public void enterNext(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = hundunStageService.enterNext(userRoleId, stageId);
		if(result != null){
			StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_ENTER_NEXT, result);
		}
	}
	@EasyMapping(mapping = InnerCmdType.DINGSHI_HUNDUN_CHECK)
	public void chaosWarCheck(Message inMsg) {
		String stageId = inMsg.getStageId();
		hundunStageService.chaosWarCheck(stageId);
	}
	
//	@EasyMapping(mapping = ClientCmdType.CHAOS_ASK_SELF_RANK)
//	public void getSelfRank(Message inMsg) {
//		Long userRoleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId();
//		int rank = hundunStageService.getSelfRank(userRoleId, stageId);
//		StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_ASK_SELF_RANK, rank);
//	}
	
	@EasyMapping(mapping = ClientCmdType.CHAOS_CUR_CENG_RANK_INFO)
	public void getCurRank(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = hundunStageService.getCurRank(stageId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_CUR_CENG_RANK_INFO, result);
		int rank = hundunStageService.getSelfRank(userRoleId, stageId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_ASK_SELF_RANK, rank);
	}
	@EasyMapping(mapping = ClientCmdType.CHAOS_PUTONG_FUHUO)
	public void chaosPutongFuhuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		hundunStageService.putongRevive(stageId, userRoleId);
	}
}
