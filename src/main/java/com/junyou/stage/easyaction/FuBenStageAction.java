package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.FubenStageService;
import com.kernel.pool.executor.Message;

/**
 * 副本场景
 * @author LiuYu
 * @date 2015-3-13 下午6:35:25
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE)
public class FuBenStageAction {
	@Autowired
	private FubenStageService fubenStageService;
	
	@EasyMapping(mapping = InnerCmdType.S_EXIT_FUBEN)
	public void exitFuben(Message inMsg){
		String stageId = inMsg.getStageId();
		
		fubenStageService.exitFuben(stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.S_EXIT_TAFANG_FUBEN)
	public void exitTaFangFuben(Message inMsg){
		String stageId = inMsg.getStageId();
		
		fubenStageService.exitTaFangFuben(stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.S_FUBEN_OVER)
	public void challengeOver(Message inMsg){
		String stageId = inMsg.getStageId();
		
		fubenStageService.challengeOver(stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.SHOUHU_FUBEN_FINISH)
	public void shouhuFinish(Message inMsg){
		String stageId = inMsg.getStageId();
		fubenStageService.shouhuFinish(stageId);
	}
	@EasyMapping(mapping = InnerCmdType.LIANYU_BOSS_FINISH_2_STAGE)
	public void guildLianyuBossFinish(Message inMsg){
		String stageId = inMsg.getStageId();
		fubenStageService.lianyuBossFinish(stageId);
	}
	
	 
	@EasyMapping(mapping = InnerCmdType.S_JIANZHONG_FUBEN_OVER)
	public void jianzhongChallengeOver(Message inMsg){
		String stageId = inMsg.getStageId();
		
		fubenStageService.jianzhongFubenTimeOver(stageId);
	} 
	@EasyMapping(mapping = InnerCmdType.S_FORCE_EXIT_FUBEN)
	public void forceExitJianzhongFuben(Message inMsg){
		String stageId = inMsg.getStageId();
		fubenStageService.forceExitJianzhongFuben(stageId);
	}
	
}
