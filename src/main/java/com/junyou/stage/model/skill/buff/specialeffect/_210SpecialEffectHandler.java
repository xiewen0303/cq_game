package com.junyou.stage.model.skill.buff.specialeffect;

import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.skill.buff.Buff;

/**
 * 周期性给周围敌人增加指定技能ID伤害(技能ID)
 *@author  DaoZheng Yuan
 *@created 2012-11-14上午11:42:55
 */
public class _210SpecialEffectHandler extends SpecialEffectHandlerAdapter {



	@Override
	public void triggerHandle(IBuff buff) {
		IStage stage = buff.getOwner().getStage();
		
		if(stage != null){
//			String stageId = stage.getId();
//			
//			Object[] result = new Object[]{stageId,buff.getAttackerId(),buff.getAttacker().getElementType(),buff.getSpecialEffectValue()};
			//释放技能buff关联的技能
//			StageMsgSender.send2StageInner(buff.getAttackerId(), stageId, StageCommands.INNER_SKILL_FIRE,result);
		}
		
		//通知
//		StageMsgSender.send2One(buff.getOwner().getId(), StageCommands.TRIGGER_BUFF, buff.getTrigerMsg());
	}
	

	@Override
	public void overHandle(Buff buff) {
		
		//通知
//		StageMsgSender.send2One(buff.getOwner().getId(), StageCommands.REMOVE_BUFF, new Object[]{buff.getId(),buff.getBuffId()});
	}



}
