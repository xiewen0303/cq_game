package com.junyou.stage.model.hatred;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatredRule;

public class TaFangMonsterHatredManager extends MonsterHatredManager2 {

	public TaFangMonsterHatredManager(IFighter owner, IHatredRule hatredRule) {
		super(owner, hatredRule);
	}

	@Override
	public void addActiveHatredHandle(IFighter target, int val) {
	}

	@Override
	public void addPassiveHatredHandle(IFighter target, int val) {
	}

}
