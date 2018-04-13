package com.junyou.stage.model.skill.firepath;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillFirePath;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;
import com.junyou.stage.utils.OffUtils;

public class DefaultSkillFirePath implements ISkillFirePath {

	@Override
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParamsm, IStage stage,ConfirmDraft draft) {
		
		SkillConfig skillConfig = skill.getSkillConfig();
		int range = skillConfig.getRange();
		
		return OffUtils.inScope(target.getPosition(), attacker.getPosition(), PathFinderType.AExtend, range);
	}

	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {
		// TODO Auto-generated method stub
		return null;
	}

}
