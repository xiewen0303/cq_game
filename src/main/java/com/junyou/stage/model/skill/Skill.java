/**
 * 
 */
package com.junyou.stage.model.skill;

import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.configure.export.impl.SkillXueXiConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightResultCalculator;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillConsume;
import com.junyou.stage.model.core.skill.ISkillFirePath;
import com.junyou.stage.model.core.skill.ISkillTarget;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.fight.calculator.FightResultCalculatorType;
import com.junyou.stage.model.skill.hatred.ISkillHatred;
import com.junyou.utils.KuafuConfigPropUtil;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-13上午11:36:20
 */
public class Skill implements ISkill {
	
	private String skillId;
	private String skillCategory;
	private Integer level;
	
	private String cd1;
	private String cd2;
	
	//不计算命中:true
	private boolean isNoCalcMZ;
	//无视防御：true
	private boolean isNoDef;
	
	private ISkillHatred skillHatred;
	
	private ISkillFirePath skillFirePath;
	
	private FightResultCalculatorType damageType;
	
	private SkillXueXiConfig skillXueXiConfig;
	
	private SkillConfig skillConfig;
	
	public Skill(SkillConfig _skillConfig, int level) {
		this.skillCategory = _skillConfig.getId();
		this.skillId = SkillManager.getSkillId(skillCategory, level);
		this.level = level;
		this.skillXueXiConfig = _skillConfig.getXuexiConfig(level);
		
		this.skillConfig = _skillConfig;
		this.cd1 = _skillConfig.getCd1();
		this.cd2 = _skillConfig.getCd2();
		this.isNoCalcMZ = _skillConfig.isNoMiss();
		this.isNoDef = _skillConfig.isNoDef();
	}
	
	public boolean isNoCalcMZ() {
		return isNoCalcMZ;
	}

	public boolean isNoDef() {
		return isNoDef;
	}

	/**
	 * 获取伤害类型 
	 * @return
	 */
	public FightResultCalculatorType getDamageType() {
		return damageType;
	}

	public void setDamageType(FightResultCalculatorType damageType) {
		this.damageType = damageType;
	}



	private ISkillConsume skillConsume;
	
	private ISkillTarget skillTarget;
	
	private IFightResultCalculator fightResultCalculator;
	
//	private PublicCdManager publicCdManager;

	public void setSkillConsume(ISkillConsume skillConsume) {
		this.skillConsume = skillConsume;
	}

	public void setSkillTarget(ISkillTarget skillTarget) {
		this.skillTarget = skillTarget;
	}

	public void setFightResultCalculator(
			IFightResultCalculator fightResultCalculator) {
		this.fightResultCalculator = fightResultCalculator;
	}

	@Override
	public ISkillConsume getConsume() {
		return this.skillConsume;
	}

	@Override
	public boolean isCding(PublicCdManager cdManager) {
		String cdType1 = cd1;
		String cdType2 = cd2;
		return cdManager.isCding(cdType1) || cdManager.isCding(cdType2);
	}

	@Override
	public void toCd(IFighter fighter) {
		
		String cdType1 = cd1;
		if(null != cdType1){
			fighter.getPublicCdManager().toCd(cdType1);
		}
		
		String cdType2 = cd2;
		if(null != cdType2){
			fighter.getPublicCdManager().toCd(cdType2);
		}
		
		if(!KuafuConfigPropUtil.isKuafuServer() && ElementType.isRole(fighter.getElementType())){
			IRole role = (IRole)fighter;
			role.addShulian(getCategory());
		}
		
	}

	@Override
	public ISkillTarget getTarget() {
		return this.skillTarget;
	}

	@Override
	public IFightResultCalculator getFightResultCalculator() {
		return this.fightResultCalculator;
	}

	@Override
	public String getId() {
		return this.skillId;
	}

	@Override
	public Integer getLevel() {
		return level;
	}


	@Override
	public Integer getDynamicCd(PublicCdManager cdManager) {
		return Math.max(cdManager.getCd(cd1), cdManager.getCd(cd2));
	}

	@Override
	public ISkillHatred getSkillHatred() {
		return skillHatred;
	}

	public void setSkillHatred(ISkillHatred skillHatred) {
		this.skillHatred = skillHatred;
	}

	@Override
	public ISkillFirePath getSkillFirePath() {
		return skillFirePath;
	}
	
	public void setSkillFirePath(ISkillFirePath skillFirePath){
		this.skillFirePath = skillFirePath;
	}

	@Override
	public String getCategory() {
		return skillCategory;
	}

	@Override
	public SkillXueXiConfig getSkillXueXiConfig() {
		return skillXueXiConfig;
	}

	@Override
	public SkillConfig getSkillConfig() {
		return skillConfig;
	}

	
}
