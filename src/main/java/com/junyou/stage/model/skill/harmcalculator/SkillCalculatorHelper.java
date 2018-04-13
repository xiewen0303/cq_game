package com.junyou.stage.model.skill.harmcalculator;

import com.junyou.stage.model.core.element.IFighter;

public class SkillCalculatorHelper {

	public static float getASkillType(IFighter fighter,String gongjiType){
		
		
		return fighter.getFightAttribute().getAttack();
		
	}
	
	public static float getBSkillType(IFighter fighter,String gongjiType){
		
		
		return fighter.getFightAttribute().getAttack();
		
	}
	
	
	public static float getCSkillType(IFighter fighter,String gongjiType){
		
		
		return fighter.getFightAttribute().getDefense();
		
	}
	
	public static float getDSkillType(IFighter fighter,String gongjiType){
		
		return fighter.getFightAttribute().getDefense();
		
	}
}
