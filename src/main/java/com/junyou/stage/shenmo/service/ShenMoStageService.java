package com.junyou.stage.shenmo.service;

import org.springframework.stereotype.Service;

import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.shenmo.entity.ShenMoShuiJing;
import com.junyou.stage.shenmo.entity.ShenMoStage;

/**
 * @author LiuYu
 * 2015-9-25 下午3:01:30
 */
@Service
public class ShenMoStageService {
	
	
	public void addShuiJingScore(Long elementId,String stageId,int score,int team) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.SHENMO_FUBEN.equals(iStage.getStageType())){
			return;
		}
		ShenMoStage stage = (ShenMoStage)iStage;
		IStageElement element = stage.getElement(elementId, ElementType.MONSTER);
		if(element == null){
			return;
		}
		ShenMoShuiJing shuijing = (ShenMoShuiJing)element;
		stage.addScore(team, score);
		shuijing.startScoreSchedule();
	}
	
	public void gameOver(String stageId) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.SHENMO_FUBEN.equals(iStage.getStageType())){
			return;
		}
		ShenMoStage stage = (ShenMoStage)iStage;
		stage.gameOver();
	}
	
	public void noticeScore(String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.SHENMO_FUBEN.equals(iStage.getStageType())){
			return;
		}
		ShenMoStage stage = (ShenMoStage)iStage;
		stage.noticeScoreSchedule(false);
	}
	
	public void kickAll(String stageId) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.SHENMO_FUBEN.equals(iStage.getStageType())){
			return;
		}
		ShenMoStage stage = (ShenMoStage)iStage;
		if(stage.isCanRemove()){
			StageManager.removeCopy(stage);
		}else{
			stage.kickAll();
		}
	}
}
