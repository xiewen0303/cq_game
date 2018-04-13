package com.junyou.stage.model.skill.hatred;

import com.junyou.stage.configure.export.impl.SkillConfig;


public class DefaultSkillHatred implements ISkillHatred {

	private float n1;
	private float n2;
	
	public DefaultSkillHatred(SkillConfig skillConfig){
		n1 = skillConfig.getHatredN1();
		n2 = skillConfig.getHatredN2();
	}
	
	@Override
	public Integer cal1(long harmVal) {
		return (int)(harmVal * n1);
	}

	
	public Integer cal2(long harmVal) {
		return (int)(harmVal * n2);
	}
}
