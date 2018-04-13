/**
 * 
 */
package com.junyou.stage.easyaction;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.RoleSkillStageService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * 角色技能
 * @author LiuYu
 * @date 2015-3-5 上午11:18:52
 */
@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.SKILL_MODULE)
public class RoleSkillStageAction {
	
	@Autowired
	private RoleSkillStageService roleSkillStageService;
	
	@EasyMapping(mapping = InnerCmdType.SKILL_ADD_SKILL,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void addSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] data = (Object[])inMsg.getData();
		String skillId = (String)data[0];
		Long zplus = CovertObjectUtil.object2Long(data[1]);
		int skillLv = 1;
		if(data.length > 2){
			skillLv = CovertObjectUtil.object2int(data[2]);
		}
		
		roleSkillStageService.addSkill(stageId, userRoleId, skillId,zplus,skillLv);
	}
	@EasyMapping(mapping = InnerCmdType.SKILL_DELETE_SKILL,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void deleteSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		List<String> skillIds = inMsg.getData();
		if(skillIds == null || skillIds.size() <= 0){
			return;
		}
		
		roleSkillStageService.deleteSkill(stageId, userRoleId, skillIds);
	}
	
	@EasyMapping(mapping = InnerCmdType.SKILL_ADD_BEIDONG_SKILL,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void addBeiDongSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Map<String,Long> attribute = inMsg.getData();
		
		roleSkillStageService.addBeiDongSkill(stageId, userRoleId,attribute);
	}
	
	@EasyMapping(mapping = InnerCmdType.SKILL_ADD_TANGBAO_SKILL,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void addTangBaoSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Map<String,Long> attribute = inMsg.getData();
		
		roleSkillStageService.addTangBaoSkill(stageId, userRoleId,attribute);
	}
	
	@EasyMapping(mapping = InnerCmdType.SKILL_ADD_GUILD_SKILL,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void addGuildDongSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Map<String,Long> attribute = inMsg.getData();
		
		roleSkillStageService.addGuildBeiDongSkill(stageId, userRoleId,attribute);
	}
	@EasyMapping(mapping = InnerCmdType.SKILL_ZPLUS_CHANGE,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void skillZplusChange(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long zplus = CovertObjectUtil.object2Long(inMsg.getData());
		
		roleSkillStageService.skillZplusChange(stageId, userRoleId, zplus);
	}
	@EasyMapping(mapping = InnerCmdType.ROLE_CHANGE_SKILL,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void roleChangeSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Object[] skillIds = inMsg.getData();
		
		roleSkillStageService.roleChangeSkills(stageId, userRoleId, skillIds);
	}
}
