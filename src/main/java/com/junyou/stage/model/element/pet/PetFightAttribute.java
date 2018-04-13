package com.junyou.stage.model.element.pet;

import com.junyou.stage.model.core.attribute.BaseFightAttribute;
import com.junyou.stage.model.core.element.IFighter;

public class PetFightAttribute  extends BaseFightAttribute{

	private IFighter fighter;
	
	public PetFightAttribute(IFighter fighter) {
		super(fighter);
		
		this.fighter = fighter;
	}

	
	@Override
	public void setCurHp(long curHp) {
		super.setCurHp(curHp);
		
		Pet pet = (Pet) fighter;
		PetVo petVo = pet.getPetVo();
		if(petVo != null){
			petVo.setCurHp(curHp);
		}
	}
}
