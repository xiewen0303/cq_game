package com.junyou.stage.model.fight;

import org.springframework.stereotype.Component;

import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;

/**
 * 跨服混沌战场战斗结果
 * @author DaoZheng Yuan
 * 2014-2-24 下午3:01:59
 */
@Component
public class KfFightHundunResult {



	public static void fightResultHandle(IFighter fighter,IFighter target,IHarm harm){
		
		try {
//			IStage stage = fighter.getStage();
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
}
