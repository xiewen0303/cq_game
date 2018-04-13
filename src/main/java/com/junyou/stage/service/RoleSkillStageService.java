/**
 * 
 */
package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-29下午3:25:30
 */
@Service
public class RoleSkillStageService{
	
	/**
	 * 增加角色技能
	 * @param stageId
	 * @param userRoleId
	 * @param skillId
	 */
	public void addSkill(String stageId,Long userRoleId,String skillId,Long zplus,int level){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		if(zplus > 0){
			Map<String,Long> skillZplusMap = new HashMap<>();
			skillZplusMap.put(EffectType.zplus.name(), zplus);
			role.getFightAttribute().setBaseAttribute(BaseAttributeType.SKILL, skillZplusMap);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
		if(stage.isCanChangeSkill()){
			ISkill skill = SkillManager.getManager().getSkill(skillId,level);
			if(skill != null){
				if(role.isYaoshen() && !SkillFireType.YAOSHEN.equals(skill.getSkillConfig().getSkillFireType())){
					return;
				}else if(!role.isYaoshen() && !SkillFireType.NORMAL.equals(skill.getSkillConfig().getSkillFireType()) && !SkillFireType.NUQI.equals(skill.getSkillConfig().getSkillFireType())){
					return;
				}
				role.addSkill(skill);
				Set<String> set = role.getNoticeSkills();
				if(set != null){
					set.add(skillId);
				}
			}
		}
		
	}
	
	/**
	 * 删除角色技能
	 * @param stageId
	 * @param userRoleId
	 * @param skillId
	 */
	public void deleteSkill(String stageId,Long userRoleId,List<String> skillIds){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		for (int i = 0; i <skillIds.size(); i++) {
			role.removeSkill(skillIds.get(i));
		}
		
	}
	
	/**
	 * 增加角色被动技能
	 * @param stageId
	 * @param userRoleId
	 * @param skillId
	 */
	public void addBeiDongSkill(String stageId,Long userRoleId,Map<String,Long> attribute){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		if(attribute != null && attribute.size() > 0){
			role.getFightAttribute().setBaseAttribute(BaseAttributeType.BEIDONG_SKILL, attribute);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
	}
	/**
	 * 增加糖宝被动技能
	 * @param stageId
	 * @param userRoleId
	 * @param skillId
	 */
	public void addTangBaoSkill(String stageId,Long userRoleId,Map<String,Long> attribute){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		if(attribute != null && attribute.size() > 0){
		    role.getFightAttribute().setBaseAttribute(BaseAttributeType.TB_BEIDONG_SKILL, attribute);
		    role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		    Pet pet = role.getPet();
		    if(pet != null){
		        pet.getFightAttribute().setBaseAttribute(BaseAttributeType.TB_BEIDONG_SKILL, attribute);
		        pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		        StageMsgSender.send2One(role.getId(), ClientCmdType.TANGBAO_MAX_HP, pet.getFightAttribute().getMaxHp());
		    }
		}
	}
	
	/**
	 * 增加角色门派技能
	 * @param stageId
	 * @param userRoleId
	 * @param skillId
	 */
	public void addGuildBeiDongSkill(String stageId,Long userRoleId,Map<String,Long> attribute){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		if(attribute != null && attribute.size() > 0){
			role.getFightAttribute().setBaseAttribute(BaseAttributeType.GUILD_SKILL, attribute);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
	}
	
	/**
	 * 增加角色技能战力变化
	 * @param stageId
	 * @param userRoleId
	 * @param skillId
	 */
	public void skillZplusChange(String stageId,Long userRoleId,Long zplus){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		if(zplus != null){
			Map<String,Long> attribute = new HashMap<>();
			attribute.put(EffectType.zplus.name(), zplus);
			role.getFightAttribute().setBaseAttribute(BaseAttributeType.SKILL, attribute);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
	}
	
	/**
	 * 切换技能
	 * @param stageId
	 * @param userRoleId
	 * @param zplus
	 */
	public void roleChangeSkills(String stageId,Long userRoleId,Object[] skills){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null || !stage.isCanChangeSkill()){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		//需要保留的技能
		List<ISkill> safeSkills = new ArrayList<>();
		//清除旧技能
		for (ISkill skill : role.getSkills()) {
			if(SkillFireType.isNoChangeClearSkill(skill.getSkillConfig().getSkillFireType())){
				safeSkills.add(skill);
			}
		}
		role.clearSkills();
		for (ISkill iSkill : safeSkills) {
			role.addSkill(iSkill);
		}
		for (Object object : skills) {
			String skillId = (String)object;
			ISkill skill = SkillManager.getManager().getSkill(skillId);
			if(skill == null){
				continue;
			}
			role.addSkill(skill);
		}
	}
}
