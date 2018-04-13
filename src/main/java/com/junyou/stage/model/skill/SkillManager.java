package com.junyou.stage.model.skill;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.utils.common.ObjectUtil;

public class SkillManager {
	private static SkillManager manager = new SkillManager();
	public static SkillManager getManager(){return manager;}
	
	private Map<String,ISkill> skills = new ConcurrentHashMap<>();
	private SkillFactory skillFactory = new SkillFactory();
	
	/**
	 * 根据技能id获取技能
	 * @param skillId
	 * @param level
	 * @return
	 */
	public ISkill getSkill(String skillId,Integer level){
		String id = getSkillId(skillId, level);
		if(skills.containsKey(id)){
			return skills.get(id);
		}
		ISkill skill = skillFactory.create(skillId, level);
		skills.put(id, skill);
		return skill;
	}
	
	/**
	 * 根据拼接后的id获取技能
	 * @param skillIdLevel	"id|level"
	 * @return
	 */
	public ISkill getSkill(String skillIdLevel){
		if (ObjectUtil.strIsEmpty(skillIdLevel)) {
			return null;
		}
		if(skills.containsKey(skillIdLevel)){
			return skills.get(skillIdLevel);
		}
		String[] id = skillIdLevel.split("\\|");
		if(id.length < 2){
			return null;
		}
		ISkill skill = skillFactory.create(id[0], Integer.parseInt(id[1]));
		skills.put(skillIdLevel, skill);
		return skill;
	}
	
	public static String getSkillId(String skillId,Integer level){
		return skillId + "|" + level;
	}
	
}
