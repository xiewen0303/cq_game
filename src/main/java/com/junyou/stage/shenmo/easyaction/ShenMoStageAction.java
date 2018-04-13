package com.junyou.stage.shenmo.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.shenmo.service.ShenMoStageService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-9-25 下午2:57:32
 */
@Controller
@EasyWorker(moduleName = GameModType.SHENMO,groupName = EasyGroup.STAGE)
public class ShenMoStageAction {
	
	@Autowired
	private ShenMoStageService shenMoStageService;
	
	@EasyMapping(mapping = InnerCmdType.SHENMO_ADD_SHUIJING_SCORE)
	public void addShuiJingScore(Message inMsg) {
		Long elementId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] data = inMsg.getData();
		int score = CovertObjectUtil.object2int(data[0]);
		int team = CovertObjectUtil.object2int(data[1]);
		shenMoStageService.addShuiJingScore(elementId, stageId, score, team);
	}
	
	@EasyMapping(mapping = InnerCmdType.SHENMO_ACTIVE_STOP)
	public void activeOver(Message inMsg) {
		String stageId = inMsg.getStageId();
		shenMoStageService.gameOver(stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.SHENMO_SCHEDULE_NOTICE_SCORE)
	public void noticeScore(Message inMsg) {
		String stageId = inMsg.getStageId();
		shenMoStageService.noticeScore(stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.SHENMO_KICK_ALL)
	public void kickAll(Message inMsg) {
		String stageId = inMsg.getStageId();
		shenMoStageService.kickAll(stageId);
	}
}
