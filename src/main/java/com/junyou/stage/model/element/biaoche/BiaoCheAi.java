package com.junyou.stage.model.element.biaoche;

import com.junyou.cmd.InnerCmdType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.element.monster.ai.DefaultAi;
import com.junyou.stage.model.element.monster.ai.IAi;

public class BiaoCheAi extends DefaultAi implements IAi{

	public BiaoCheAi(IFighter fighter) {
		super(fighter);
	}

	@Override
	protected Short getAiHandleCommand() {
		//镖车心跳
		return InnerCmdType.S_BIAOCHE_HEART;
	}

}
