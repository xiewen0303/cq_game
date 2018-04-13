package com.junyou.stage.model.element.pet;

import com.junyou.cmd.InnerCmdType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.element.monster.ai.DefaultAi;
import com.junyou.stage.model.element.monster.ai.IAi;

public class PetAi extends DefaultAi implements IAi{

	public PetAi(IFighter fighter) {
		super(fighter);
	}

	@Override
	protected Short getAiHandleCommand() {
		return InnerCmdType.AI_PET_HANDLE;
	}

}
