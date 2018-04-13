package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.cmd.InnerCmdType;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;

/**
 * 多人副本队伍管理
 * @author jy
 *
 */
public class MoreFubenTeamManage {
	// key:副本ID value：List<MoreFubenTeam>
	private static ConcurrentMap<Integer,List<MoreFubenTeam>> teamFubenMap = new ConcurrentHashMap<>();
	// key：队伍ID value：MoreFubenTeam
	private static ConcurrentMap<Integer,MoreFubenTeam> teamMap = new ConcurrentHashMap<>();
	// key:玩家ID value：队伍ID
	private static ConcurrentMap<Long,Integer> teamIdMap = new ConcurrentHashMap<>();
	// key:玩家ID value：成员
	private static ConcurrentMap<Long,MoreFubenTeamMember> teamMembers = new ConcurrentHashMap<>();
	// key:玩家ID value：applyEnterData
	private static ConcurrentMap<Long,Object[]> enterDatas = new ConcurrentHashMap<>();
	// key：队伍ID MoreFubenSourceTeam(原服用 )
	private static ConcurrentMap<Integer,MoreFubenSourceTeam> sourceTeamMap = new ConcurrentHashMap<>();
	
	public static void addMoreFubenTeamMap(int fubenId,MoreFubenTeam team){
		List<MoreFubenTeam> teamList = teamFubenMap.get(fubenId);
		if(teamList == null){
			teamList = new ArrayList<>();
			teamFubenMap.put(fubenId, teamList);
		}
		teamList.add(team);
	}
	
	public static void addTeamMap(int teamId,MoreFubenTeam team){
		teamMap.put(teamId, team);
	}
	
	public static void addTeamIdMap(Long userRoleId,MoreFubenTeamMember member){
		teamIdMap.put(userRoleId, member.getTeam().getTeamId());
		teamMembers.put(userRoleId, member);
	}
	
	/**
	 * 通过队伍ID获取多人副本队伍
	 * @param teamId
	 * @return
	 */
	public static MoreFubenTeam getTeamByTeamID(int teamId){
		return teamMap.get(teamId);
	}
	
	/**
	 * 是否存在
	 * @param fubenId
	 * @param userRoleId
	 * @param isCreate
	 * @return
	 */
	public static boolean isExist(int fubenId,Long userRoleId){
		List<MoreFubenTeam> teamList = teamFubenMap.get(fubenId);
		if(teamList != null && !teamList.isEmpty()){
			for(MoreFubenTeam team : teamList){
				if(team.getMember(userRoleId).getRoleId() == userRoleId){
					return true;
				}
			}
		}
		return false;
	}

	public static ConcurrentMap<Integer, List<MoreFubenTeam>> getTeamFubenMap() {
		return teamFubenMap;
	}

	public static ConcurrentMap<Integer, MoreFubenTeam> getTeamMap() {
		return teamMap;
	}

	public static ConcurrentMap<Long, Integer> getTeamIdMap() {
		return teamIdMap;
	}
	
	public static Integer getTeamId(Long userRoleId){
		return teamIdMap.get(userRoleId);
	}
	
	public static void removeTeam(Long userRoleId,boolean notice){
		teamIdMap.remove(userRoleId);
		MoreFubenTeamMember member = teamMembers.remove(userRoleId);
		if(notice && member != null){
			//通知原服退出副本状态
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.MORE_FUBEN_LEAVE_TEAM, userRoleId);
		}
	}
	
	public static MoreFubenTeamMember getTeamMember(Long userRoleId){
		return teamMembers.get(userRoleId);
	}
	
	public static void memberReadyEnter(Long userRoleId,Object[] data){
		enterDatas.put(userRoleId, data);
	}
	
	public static Object[] getEnterData(Long userRoleId){
		return enterDatas.remove(userRoleId);
	}
	
	/*TODO:以下方法为原服使用*/
	public static void enterTeam(Long userRoleId,Integer teamId,Integer fubenId){
		teamIdMap.put(userRoleId, teamId);
		MoreFubenSourceTeam team = sourceTeamMap.get(teamId);
		if(team == null){
			team = new MoreFubenSourceTeam(teamId, fubenId);
			sourceTeamMap.put(teamId, team);
		}
		team.addRole(userRoleId);
	}
	public static Object[] getYaoQingMsg(Long userRoleId){
		Integer teamId = teamIdMap.get(userRoleId);
		if(teamId == null){
			return null;
		}
		MoreFubenSourceTeam team = sourceTeamMap.get(teamId);
		if(team == null){
			return null;
		}
		return team.getMsg();
	}
	public static boolean leaveTeam(Long userRoleId){
		Integer teamId = teamIdMap.remove(userRoleId);
		if(teamId != null){
			MoreFubenSourceTeam team = sourceTeamMap.get(teamId);
			if(team != null){
				team.removeRole(userRoleId);
				if(team.isCanRemove()){
					sourceTeamMap.remove(teamId);
				}
			}
			return true;
		}
		return false;
	}
}
