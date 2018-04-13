/**
 * 
 */
package com.junyou.stage.model.core.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.fight.BattleMode;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-6下午5:00:33
 */
public abstract class AbsFighter extends AbsElement implements IFighter {

	private Map<String,ISkill> skillMap = new HashMap<String, ISkill>();
	
	private BattleMode battleMode;
	
	/**
	 * @param id
	 */
	public AbsFighter(Long id,String name) {
		super(id,name);
	}

	@Override
	public ISkill getSkill(String category) {
		return skillMap.get(category);
	}
	
	public List<ISkill> getSkills(){
		return new ArrayList<>(skillMap.values());
	}
	
	public void clearSkills(){
		skillMap.clear();
	}
	
	public void removeSkill(String category){
		skillMap.remove(category);
	}

	/**
	 * @param
	 */
	public void addSkill(ISkill skill) {
		this.skillMap.put(skill.getCategory(), skill);
	}

	@Override
	public void setBattleMode(BattleMode battleMode) {
		this.battleMode = battleMode;
	}

	@Override
	public BattleMode getBattleMode() {
		return battleMode;
	}

	@Override
	public boolean isPve(IFighter target) {
		return ElementType.isMonster(getElementType()) || ElementType.isMonster(target.getElementType());
	}
	
	@Override
	public int getHeartTime() {
		return 300;
	}
	
}
