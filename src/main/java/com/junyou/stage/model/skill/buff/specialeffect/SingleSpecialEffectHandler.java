package com.junyou.stage.model.skill.buff.specialeffect;

import com.junyou.cmd.InnerCmdType;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 周期性释放单体技能
 *@author  DaoZheng Yuan
 *@created 2012-11-14上午11:42:55
 */
public class SingleSpecialEffectHandler extends SpecialEffectHandlerAdapter {



	@Override
	public void triggerHandle(IBuff buff) {
		IStage stage = buff.getOwner().getStage();
		
		if(stage != null){
			String stageId = stage.getId();
			Object[] result = new Object[]{stageId,buff.getOwner().getId(),buff.getAttackerId(),buff.getAttacker().getElementType(),buff.getSpecialEffectValue(),buff.getOwner().getElementType()};
			//释放技能buff关联的技能
			StageMsgSender.send2StageInner(buff.getAttackerId(), stageId, InnerCmdType.INNER_BUFF_SKILL_FIRE,result);
		}
	}
	


}
