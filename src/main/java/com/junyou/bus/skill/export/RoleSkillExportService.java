package com.junyou.bus.skill.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.skill.entity.RoleSkillGewei;
import com.junyou.bus.skill.service.RoleSkillService;

@Service
public class RoleSkillExportService {
	@Autowired
	private RoleSkillService roleSkillService;
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public List<RoleSkill> initRoleSkill(Long userRoleId) {
		return roleSkillService.initRoleSkill(userRoleId);
	}
	public List<RoleSkillGewei> initRoleSkillGeweis(Long userRoleId) {
		return roleSkillService.initRoleSkillGeweis(userRoleId);
	}
	/**
	 * 查询所有技能
	 * @param userRoleId
	 * @return
	 */
	public List<RoleSkill> loadAllRoleSkill(Long userRoleId) {
		return roleSkillService.loadAllRoleSkill(userRoleId);
	}
	/**
	 * 查询指定技能
	 * @param userRoleId
	 * @param skillId
	 * @return
	 */
	public RoleSkill loadRoleSkill(Long userRoleId,String skillId) {
		return roleSkillService.getSkill(userRoleId,skillId);
	}
	/**
	 * 查询指定类型技能
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public List<RoleSkill> loadRoleSkill(Long userRoleId,Integer type) {
		return roleSkillService.getSkillsByType(userRoleId,type);
	}
	
	/**
	 * 上线检测是否有该学的技能未学习
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		roleSkillService.onlineHandle(userRoleId);
	}
	
	/**
	 * 切换技能
	 * @param userRoleId
	 * @param yaoshen	切换后是否是妖神状态
	 */
	public void changeSkill(Long userRoleId,boolean yaoshen){
		roleSkillService.changeSkill(userRoleId, yaoshen);
	}
	/**
	 * 初始化妖神技能
	 * @param userRoleId
	 */
	public void initYaoShenSkills(Long userRoleId){
		roleSkillService.initYaoShenSkills(userRoleId);
	}
	/**
	 * 同步技能入库
	 * @param roleSkill
	 */
	public void saveSkill(RoleSkill roleSkill){
		roleSkillService.saveSkill(roleSkill);
	}
	/**
	 * 转职转换技能
	 * @param userRoleId
	 * @param job
	 */
	public void zhuanZhiJiNeng(Long userRoleId,int job){
		roleSkillService.zhuanZhiJiNeng(userRoleId, job);
	}

	/**
	 * 判断糖宝被动技能是否已装备
	 */
	public boolean isTbSkillZhuangbei(Long userRoleId, String skillId){
	    return roleSkillService.isTbSkillZhuangbei(userRoleId, skillId);
	}
}
