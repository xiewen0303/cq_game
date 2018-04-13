package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.configure.export.impl.TeamConfig;
import com.junyou.stage.configure.export.impl.TeamConfigExportService;
import com.junyou.stage.entity.Team;
import com.junyou.stage.entity.TeamMember;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.OtherRolesSearchFilter;
import com.junyou.stage.model.core.stage.OtherTeamLeaderSearchFilter;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgQueue;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.number.LongUtils;
import com.kernel.gen.id.IdFactory;

@Service
public class TeamService {
	@Autowired
	private TeamConfigExportService teamConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	private ConcurrentMap<Long, TeamMember> members = new ConcurrentHashMap<>();
	
	private TeamMember createTeamMember(IRole role,IStage stage){
		TeamMember member = new TeamMember();
		member.setLine(stage.getLineNo());
		member.setMapId(stage.getMapId());
		member.setStageId(stage.getId());
		role.setTeamMember(member);
		members.put(role.getId(), member);
		return member;
	}
	private Team createTeam(TeamMember leader){
		leader.setLeader(true);
		Team team = new Team(leader);
		team.setTeamId(IdFactory.getInstance().getTeamId());
		leader.setTeam(team);
		return team;
	}
	
	/**
	 * 创建队伍
	 * @param userRoleId
	 * @param stageId
	 */
	public Object[] createTeam(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		if(stage.isCopy()){
			return AppErrorCode.TEAM_ERROR_IN_FUBEN;//副本中无法组队
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember leader = role.getTeamMember();
		if(leader != null && leader.getTeam() != null){
			return AppErrorCode.TEAM_HAS_TEAM;//已有队伍
		}
		if(leader == null){
			leader = createTeamMember(role, stage);
		}
		createTeam(leader);
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.ZUDUI, 1});
		return new Object[]{1,leader.getMemberVo()};
	}
	/**
	 * 获取周围玩家
	 * @param userRoleId
	 * @param stageId
	 * @return
	 */
	public Object[] getArroundPlayer(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		OtherRolesSearchFilter filter = new OtherRolesSearchFilter(userRoleId);
		Collection<IStageElement> roles = stage.getSurroundElements(role.getPosition(), ElementType.ROLE, filter);
		if(roles != null && roles.size() > 0){
			Object[] roleVos = new Object[roles.size()];
			int i = 0;
			for (IStageElement iStageElement : roles) {
				IRole otherRole = (IRole)iStageElement;
				TeamMember teamMember = otherRole.getTeamMember();
				if(teamMember == null){
					teamMember = createTeamMember(otherRole, stage);
				}
				roleVos[i++] = teamMember.getMemberVo();
			}
			return new Object[]{1,roleVos};
		}
		return AppErrorCode.OK;
	}
	/**
	 * 获取周围队伍
	 * @param userRoleId
	 * @param stageId
	 * @return
	 */
	public Object[] getArroundTeam(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		OtherTeamLeaderSearchFilter filter = new OtherTeamLeaderSearchFilter(userRoleId);
		Collection<IStageElement> roles = stage.getSurroundElements(role.getPosition(), ElementType.ROLE, filter);
		if(roles != null && roles.size() > 0){
			Object[] teamVos = new Object[roles.size()];
			int i = 0;
			for (IStageElement iStageElement : roles) {
				IRole otherRole = (IRole)iStageElement;
				teamVos[i++] = otherRole.getTeamMember().getTeam().getTeamVo();
			}
			return new Object[]{1,teamVos};
		}
		return AppErrorCode.OK;
	}
	/**
	 * 邀请玩家加入队伍
	 * @param userRoleId
	 * @param stageId
	 * @param targetRoleId
	 * @param stageMsgQueue
	 * @return
	 */
	public Object[] yaoqing(Long userRoleId,String stageId,Long targetRoleId,StageMsgQueue stageMsgQueue){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		if(stage.isCopy()){
			return AppErrorCode.TEAM_ERROR_IN_FUBEN;//副本中无法组队
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember leader = role.getTeamMember();
		if(leader == null){
			leader = createTeamMember(role, stage);
		}
		Team team = leader.getTeam();
		if(team == null){
			team = createTeam(leader);
			stageMsgQueue.addMsg(userRoleId, ClientCmdType.CREATE_TEAM, new Object[]{1,leader.getMemberVo()});
		}
		if(!leader.isLeader()){
			return AppErrorCode.TEAM_IS_NOT_LEADER;//不是队长
		}
		if(team.isFull()){
			return AppErrorCode.TEAM_IS_FULL;//队伍已满
		}
		String targetStageId = publicRoleStateExportService.getRolePublicStageId(targetRoleId);
		if(targetStageId == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标玩家不在线
		}
		IStage targetStage = StageManager.getStage(targetStageId);
		if(targetStage == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//场景不存在
		}
		IRole targetRole = targetStage.getElement(targetRoleId, ElementType.ROLE);
		if(targetRole == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标角色不存在
		}
		TeamMember member = targetRole.getTeamMember();
		if(member == null){
			member = createTeamMember(targetRole, targetStage);
		}
		if(member.getTeam() != null){
			return AppErrorCode.TEAM_ROLE_HAS_TEAM;//目标已有队伍
		}else if(member.isAutoAgreeYaoqing()){
			//自动接收邀请
			if(team.addMembers(member)){
				stageMsgQueue.addMsg(targetRoleId, ClientCmdType.TEAM_AGREE_YAPQING, new Object[]{1,team.getMemberVos()});
			}else{
				return AppErrorCode.TEAM_IS_FULL;//队伍已满	
			}
			//计算buff属性
			enterStageHandle(member);
			synTeamInfo(team,userRoleId);
		}else{
			stageMsgQueue.addMsg(targetRoleId, ClientCmdType.TEAM_RECIVE_YAOQING, new Object[]{role.getId(),role.getName(),role.getLevel()});
		}
		return AppErrorCode.OK;
	}
	/**
	 * 设置自动同意
	 * @param userRoleId
	 * @param stageId
	 * @param type	1：自动同意邀请，2：自动同意申请
	 * @param auto
	 */
	public void setAutoAgree(Long userRoleId,String stageId,int type,boolean auto){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		TeamMember member = role.getTeamMember();
		if(member == null){
			member = createTeamMember(role, stage);
		}
		if(type == GameConstants.TEAM_AUTO_YAOQING){
			member.setAutoAgreeYaoqing(auto);
		}else{
			member.setAutoAgreeApply(auto);
		}
	}
	/**
	 * 同意玩家邀请
	 * @param userRoleId
	 * @param stageId
	 * @param targetRoleId
	 * @return
	 */
	public Object[] agreeYaoqing(Long userRoleId,String stageId,Long targetRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		if(stage.isCopy()){
			return AppErrorCode.TEAM_ERROR_IN_FUBEN;//副本中无法组队
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember member = role.getTeamMember();
		if(member != null && member.getTeam() != null){
			return AppErrorCode.TEAM_HAS_TEAM;//已有队伍
		}
		String targetStageId = publicRoleStateExportService.getRolePublicStageId(targetRoleId);
		if(targetStageId == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标玩家不在线
		}
		IStage targetStage = StageManager.getStage(targetStageId);
		if(targetStage == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//场景不存在
		}
		IRole targetRole = targetStage.getElement(targetRoleId, ElementType.ROLE);
		if(targetRole == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标角色不存在
		}
		TeamMember leader = targetRole.getTeamMember();
		if(leader == null || leader.getTeam() == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		if(!leader.isLeader()){
			return AppErrorCode.TEAM_ROLE_IS_NOT_LEADER;//权限不足
		}
		Team team = leader.getTeam();
		if(team.isFull()){
			return AppErrorCode.TEAM_IS_FULL;//队伍已满
		}
		if(member == null){
			member = createTeamMember(role, stage);
		}
		if(!team.addMembers(member)){
			return AppErrorCode.TEAM_IS_FULL;//队伍已满
		}
		//计算buff属性
		enterStageHandle(member);
		synTeamInfo(team,userRoleId);
		return null;
	}
	/**
	 * 我的队伍
	 * @param userRoleId
	 * @param stageId
	 * @return
	 */
	public Object[] myTeamInfo(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember member = role.getTeamMember();
		if(member == null || member.getTeam() == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		return member.getTeam().getMemberVos();
	}
	/**
	 * 同步队伍信息
	 * @param team
	 */
	private void synTeamInfo(Team team,Long userRoleId){
		Long[] roleIds = team.getRoleIdList();
		if(roleIds.length > 0){
			StageMsgSender.send2Many(roleIds, ClientCmdType.TEAM_MY_TEAM_INFO, team.getMemberVos());
			try {
				
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
			//修炼任务
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.ZUDUI, 1});
		}
	}
	/**
	 * 离开队伍
	 * @param userRoleId
	 * @param stageId
	 * @return
	 */
	public Object[] leaveTeam(Long userRoleId,String stageId,Object targetRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember member = role.getTeamMember();
		if(member == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		Team team = member.getTeam();
		if(team == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		IRole leaveRole = role;
		if(targetRoleId == null){
			//主动离开队伍
			leaveStageHandle(member,team);
			team.removeMember(userRoleId);
			StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_LEAVE_TEAM, AppErrorCode.TEAM_LEAVE_TEAM);
		}else{
			//踢出队伍
			if(!member.isLeader()){
				return AppErrorCode.TEAM_IS_NOT_LEADER;//不是队长
			}
			long leaveRoleId = LongUtils.obj2long(targetRoleId);
			TeamMember targertMember = team.removeMember(leaveRoleId);
			if(targertMember == null){
				return AppErrorCode.TEAM_NOT_SAME_TEAM;//不在一个队伍
			}
			leaveRole = targertMember.getRole();
			if(targertMember != null){
				leaveStageHandle(targertMember, team);
			}
			StageMsgSender.send2One(leaveRoleId, ClientCmdType.TEAM_LEAVE_TEAM, AppErrorCode.TEAM_BE_KICK_TEAM);
		}
		calTeamBuff(leaveRole, 0);
		synTeamInfo(team,userRoleId);
		return null;
	}
	/**
	 * 离开场景处理
	 * @param member
	 * @param team
	 */
	private void leaveStageHandle(TeamMember member,Team team){
		List<IRole> noticeRoleIds = new ArrayList<>();
		for (TeamMember teamMember : team.getMembers()) {
			if(teamMember == member || !teamMember.getStageId().equals(member.getStageId())){
				continue;
			}
			noticeRoleIds.add(teamMember.getRole());
		}
		int count = noticeRoleIds.size();
		if(count > 0){
			//队伍属性变更
			for (IRole role : noticeRoleIds) {
				calTeamBuff(role, count);
			}
		}
	}
	/**
	 * 进入场景处理
	 * @param member
	 */
	private void enterStageHandle(TeamMember member){
		Team team = member.getTeam();
		if(team != null){
			List<IRole> noticeRoleIds = new ArrayList<>();
			noticeRoleIds.add(member.getRole());
			for (TeamMember teamMember : team.getMembers()) {
				if(teamMember == member || !teamMember.getStageId().equals(member.getStageId())){
					continue;
				}
				noticeRoleIds.add(teamMember.getRole());
			}
			
			//队伍属性变更
			int count = noticeRoleIds.size();
			for (IRole role : noticeRoleIds) {
				calTeamBuff(role, count);
			}
		}
	}
	/**
	 * 重新计算组队BUFF
	 * @param role
	 * @param count
	 */
	private void calTeamBuff(IRole role,int count){
		TeamConfig config = teamConfigExportService.loadByCount(count);
		if(config == null || config.getAttribute() == null){
			role.getFightAttribute().clearBaseAttribute(BaseAttributeType.TEAM, true);
		}else{
			role.getFightAttribute().setBaseAttribute(BaseAttributeType.TEAM, config.getAttribute());
		}
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	/**
	 * 转让队长
	 * @param userRoleId
	 * @param stageId
	 * @param targetRoleId
	 * @return
	 */
	public Object[] changeLeader(Long userRoleId,String stageId,Long targetRoleId){
		if(userRoleId.equals(targetRoleId)){
			return AppErrorCode.TEAM_CHANGE_LEADER_SELF;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember leader = role.getTeamMember();
		if(leader == null || !leader.isLeader()){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		Team team = leader.getTeam();
		if(team == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		if(!team.changeLeader(targetRoleId)){
			return AppErrorCode.TEAM_NOT_SAME_TEAM;//不在一个队伍中
		}
		synTeamInfo(team,userRoleId);
		return AppErrorCode.OK;
	}
	/**
	 * 申请入队
	 * @param userRoleId
	 * @param stageId
	 * @param targetRoleId
	 * @return
	 */
	public Object[] applyTeam(Long userRoleId,String stageId,Long targetRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember member = role.getTeamMember();
		if(member != null && member.getTeam() != null){
			return AppErrorCode.TEAM_ROLE_HAS_TEAM;//已有队伍
		}
		String targetStageId = publicRoleStateExportService.getRolePublicStageId(targetRoleId);
		if(targetStageId == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标玩家不在线
		}
		IStage targetStage = StageManager.getStage(targetStageId);
		if(targetStage == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//场景不存在
		}
		IRole targetRole = targetStage.getElement(targetRoleId, ElementType.ROLE);
		if(targetRole == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标角色不存在
		}
		TeamMember leader = targetRole.getTeamMember();
		if(leader == null || !leader.isLeader()){
			return AppErrorCode.TEAM_ROLE_IS_NOT_LEADER;//权限不足
		}
		if(leader.isAutoAgreeApply()){
			Team team = leader.getTeam();
			if(team.isFull()){
				return AppErrorCode.TEAM_IS_FULL;//队伍已满
			}
			if(member == null){
				member = createTeamMember(role, stage);
			}
			if(!team.addMembers(member)){
				return AppErrorCode.TEAM_IS_FULL;//队伍已满
			}
			//计算buff属性
			enterStageHandle(member);
			synTeamInfo(team,userRoleId);
			return null;//返回null，不通知前端
		}else{
			StageMsgSender.send2One(targetRoleId, ClientCmdType.TEAM_LEADER_RECIVE, new Object[]{role.getId(),role.getName(),role.getLevel()});
		}
		return AppErrorCode.OK;
	}
	/**
	 * 同意组队
	 * @param userRoleId
	 * @param stageId
	 * @param targetRoleId
	 * @return
	 */
	public Object[] agreeApply(Long userRoleId,String stageId,Long targetRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.TEAM_STAGE_IS_NULL;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.TEAM_ROLE_IS_NULL;//角色不存在
		}
		TeamMember leader = role.getTeamMember();
		if(leader == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		Team team = leader.getTeam();
		if(team == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;//队伍不存在
		}
		if(!leader.isLeader()){
			return AppErrorCode.TEAM_IS_NOT_LEADER;//不是队长
		}
		if(team.isFull()){
			return AppErrorCode.TEAM_IS_FULL;//队伍已满
		}
		String targetStageId = publicRoleStateExportService.getRolePublicStageId(targetRoleId);
		if(targetStageId == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标玩家不在线
		}
		IStage targetStage = StageManager.getStage(targetStageId);
		if(targetStage == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//场景不存在
		}
		IRole targetRole = targetStage.getElement(targetRoleId, ElementType.ROLE);
		if(targetRole == null){
			return AppErrorCode.TEAM_ROLE_IS_OFFLINE;//目标角色不存在
		}
		TeamMember member = targetRole.getTeamMember();
		if(member != null && member.getTeam() != null){
			return AppErrorCode.TEAM_ROLE_HAS_TEAM;//已有队伍
		}
		if(member == null){
			member = createTeamMember(targetRole, targetStage);
		}
		if(!team.addMembers(member)){
			return AppErrorCode.TEAM_IS_FULL;//队伍已满
		}
		//计算buff属性
		enterStageHandle(member);
		synTeamInfo(team,userRoleId);
		return AppErrorCode.OK;
	}
	/**
	 * 获取同队伍成员id列表
	 * @param userRoleId
	 * @return（如果没队伍，则返回null）
	 */
	public Long[] getTeamMemberIds(Long userRoleId){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(stageId == null){
			return null;//玩家不在线
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;//场景不存在
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return null;//角色不存在
		}
		TeamMember member = role.getTeamMember();
		if(member == null || member.getTeam() == null){
			return null;//队伍不存在
		}
		return member.getTeam().getRoleIdList();
	}
	/**
	 * 离线事件
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		TeamMember member = members.get(userRoleId);
		if(member != null){
			if(member.getTeam() != null){
				Team team = member.getTeam();
				leaveStageHandle(member,team);
				team.removeMember(userRoleId);
				synTeamInfo(team,userRoleId);
			}
			members.remove(userRoleId);
		}
	}
	/**
	 * 切换场景处理
	 * @param role
	 */
	public void changeStageHandle(IRole role,IStage stage){
		TeamMember member = members.get(role.getId());
		if(member != null){
			Team team = member.getTeam();
			if(team != null){
				leaveStageHandle(member,team);
			}
			
			role.setTeamMember(member);
			member.setLine(stage.getLineNo());
			member.setMapId(stage.getMapId());
			member.setStageId(stage.getId());
			
			if(team != null){
				enterStageHandle(member);
			}
		}
	}
}
