package com.junyou.stage.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.service.TeamService;

@Service
public class TeamExportService {
	@Autowired
	private TeamService teamService;
	/**
	 * 离线业务
	 * @param role
	 */
	public void offlineHandle(Long userRoleId){
		teamService.offlineHandle(userRoleId);
	}
	/**
	 * 获取同队伍成员id列表
	 * @param userRoleId
	 * @return（如果没队伍，则返回null）
	 */
	public Long[] getTeamMemberIds(Long userRoleId){
		return teamService.getTeamMemberIds(userRoleId);
	}
	/**
	 * 切换场景处理
	 * @param role
	 */
	public void changeStageHandle(IRole role,IStage stage){
		teamService.changeStageHandle(role, stage);
	}
}
