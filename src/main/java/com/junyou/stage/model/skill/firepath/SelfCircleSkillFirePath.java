package com.junyou.stage.model.skill.firepath;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillFirePath;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;
import com.junyou.stage.utils.OffUtils;

/**
 * @description 自己为圆心的指定半径内
 * @author ShiJie Chi
 * @date 2012-6-12 下午4:18:58 
 */
public class SelfCircleSkillFirePath implements ISkillFirePath {

	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,Object[] helpParams, IStage stage,ConfirmDraft draft) {
		
		Point circlePoint = attacker.getPosition();
		Point targetPoint = target.getPosition();
		
		SkillConfig skillConfig = skill.getSkillConfig();
		float radius = skillConfig.getPathRadius();
		
		if(OffUtils.inScope(circlePoint, targetPoint,PathFinderType.AExtend, (int)radius)){
			return true;
		}
		
		return false;
		
	}

	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {
		
		Point position = attacker.getPosition();
		
		return new Object[]{position.getX(),position.getY()};
	}

}
