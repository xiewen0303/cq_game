package com.junyou.stage.model.element.role;

import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.skill.harm.HarmUtils;
/**
 * 死亡信息
 * @Description 
 * @author zl
 * 2012-8-14 下午4:47:26
 */
public class DeadInfo {
	private boolean killedByRole;
	private boolean killedByBoss;
	
	private Long killerId;
	private String killerName;
	private Object killerSex;
	private String killerGuildName;
	
	private String skillCategory;
	
	public DeadInfo(IHarm harm) {
		
		if(HarmUtils.belong2Role(harm.getAttacker())){
			
			killedByRole = true;
			
			IRole role = HarmUtils.getBelongRole(harm.getAttacker());
			
			killerId = role.getId();
			killerName = role.getName();
//			killerSex = role.getBusinessData().getSex();
			killerGuildName = role.getBusinessData().getGuildName();
			
		}else if(HarmUtils.belong2BossMonster(harm.getAttacker())){
			
			killedByBoss = true;
			
			IMonster monster = HarmUtils.getBelongMonster(harm.getAttacker());
			
			killerId = monster.getId();
			killerName = monster.getMonsterId();
		}else{
			
			killerId = harm.getAttacker().getId();
			killerName = harm.getAttacker().getName();
			
		}
		if(harm.getSkill() != null){
			this.skillCategory = harm.getSkill().getCategory();
		}
		
	}

	public boolean killedByRole() {
		return killedByRole;
	}

	public Long getKillerId() {
		return killerId;
	}

	public String getKillerName() {
		return killerName;
	}

	public boolean killedByBoss() {
		return killedByBoss;
	}

	public Object getKillerSex() {
		return killerSex;
	}

	public Object getKillerGuildName() {
		return killerGuildName;
	}

	public Object getSkillCategory() {
		return skillCategory;
	}
}
