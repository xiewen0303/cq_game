package com.junyou.stage.model.skill.firepath;

import com.junyou.gameconfig.constants.PathFinderType;
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
 * 已施法者为圆心
 * @author DaoZheng Yuan
 * 2013-10-31 下午8:26:15
 */
public class AttackerCircleSkillFirePath implements ISkillFirePath {

		
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParam, IStage stage, ConfirmDraft draft) {
		
		Integer x = (Integer)helpParam[0];
		Integer y = (Integer)helpParam[1];
		Point circlePoint = new Point(x, y);
		
		SkillConfig skillConfig = skill.getSkillConfig();
		float radius = skillConfig.getPathRadius();
		if(OffUtils.inScope(circlePoint, target.getPosition(),PathFinderType.AExtend, (int)radius)){
			return true;
		}
		
		return false;
	}

	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {
		return new Object[]{attacker.getPosition().getX(), attacker.getPosition().getY()};
	}

}
