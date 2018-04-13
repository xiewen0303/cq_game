package com.junyou.stage.model.element.biaoche;

import com.junyou.stage.model.core.attribute.BaseFightAttribute;
import com.junyou.stage.model.core.element.IFighter;

public class BiaoCheFightAttribute  extends BaseFightAttribute{

	private IFighter fighter;
	
	public BiaoCheFightAttribute(IFighter fighter) {
		super(fighter);
		
		this.fighter = fighter;
	}

	
	@Override
	public void setCurHp(long curHp) {
		super.setCurHp(curHp);
	}
}
