package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.cmd.InnerCmdType;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;

/**
 * 多人副本队伍管理
 * 
 * @author jy
 * 
 */
public class MaiguFubenTeamManage {
	// key:副本ID value：List<MoreFubenTeam>
	private static ConcurrentMap<Integer, List<MaiguFubenTeam>> teamFubenMap = new ConcurrentHashMap<>();
	// key：队伍ID value：MoreFubenTeam
	private static ConcurrentMap<Integer, MaiguFubenTeam> teamMap = new ConcurrentHashMap<>();
	// key:玩家ID value：队伍ID
	private static ConcurrentMap<Long, Integer> teamIdMap = new ConcurrentHashMap<>();
	// key:玩家ID value：成员
	private static ConcurrentMap<Long, MaiguFubenTeamMember> teamMembers = new ConcurrentHashMap<>();
	// key:玩家ID value：applyEnterData
	private static ConcurrentMap<Long, Object[]> enterDatas = new ConcurrentHashMap<>();
	// key：队伍ID MoreFubenSourceTeam(原服用 )
	private static ConcurrentMap<Integer, MaiguFubenSourceTeam> sourceTeamMap = new ConcurrentHashMap<>();

	public static void addMaiguFubenTeamMap(int fubenId, MaiguFubenTeam team) {
		List<MaiguFubenTeam> teamList = teamFubenMap.get(fubenId);
		if (teamList == null) {
			teamList = new ArrayList<>();
			teamFubenMap.put(fubenId, teamList);
		}
		teamList.add(team);
	}

	public static void addTeamMap(int teamId, MaiguFubenTeam team) {
		teamMap.put(teamId, team);
	}

	public static void addTeamIdMap(Long userRoleId, MaiguFubenTeamMember member) {
		teamIdMap.put(userRoleId, member.getTeam().getTeamId());
		teamMembers.put(userRoleId, member);
	}

	/**
	 * 通过队伍ID获取多人副本队伍
	 * 
	 * @param teamId
	 * @return
	 */
	public static MaiguFubenTeam getTeamByTeamID(int teamId) {
		return teamMap.get(teamId);
	}

	/**
	 * 是否存在
	 * 
	 * @param fubenId
	 * @param userRoleId
	 * @param isCreate
	 * @return
	 */
	public static boolean isExist(int fubenId, Long userRoleId) {
		List<MaiguFubenTeam> teamList = teamFubenMap.get(fubenId);
		if (teamList != null && !teamList.isEmpty()) {
			for (MaiguFubenTeam team : teamList) {
				if (team.getMember(userRoleId).getRoleId() == userRoleId) {
					return true;
				}
			}
		}
		return false;
	}

	public static ConcurrentMap<Integer, List<MaiguFubenTeam>> getTeamFubenMap() {
		return teamFubenMap;
	}

	public static ConcurrentMap<Integer, MaiguFubenTeam> getTeamMap() {
		return teamMap;
	}

	public static ConcurrentMap<Long, Integer> getTeamIdMap() {
		return teamIdMap;
	}

	public static Integer getTeamId(Long userRoleId) {
		return teamIdMap.get(userRoleId);
	}

	public static void removeTeam(Long userRoleId, boolean notice) {
		teamIdMap.remove(userRoleId);
		MaiguFubenTeamMember member = teamMembers.remove(userRoleId);
		if (notice && member != null) {
			// 通知原服退出副本状态
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
					InnerCmdType.MAIGU_LEAVE_TEAM, userRoleId);
		}
	}

	public static MaiguFubenTeamMember getTeamMember(Long userRoleId) {
		return teamMembers.get(userRoleId);
	}

	public static void memberReadyEnter(Long userRoleId, Object[] data) {
		enterDatas.put(userRoleId, data);
	}

	public static Object[] getEnterData(Long userRoleId) {
		return enterDatas.remove(userRoleId);
	}

	/* TODO:以下方法为原服使用 */
	public static void enterTeam(Long userRoleId, Integer teamId,
			Integer fubenId) {
		teamIdMap.put(userRoleId, teamId);
		MaiguFubenSourceTeam team = sourceTeamMap.get(teamId);
		if (team == null) {
			team = new MaiguFubenSourceTeam(teamId, fubenId);
			sourceTeamMap.put(teamId, team);
		}
		team.addRole(userRoleId);
	}

	public static Object[] getYaoQingMsg(Long userRoleId) {
		Integer teamId = teamIdMap.get(userRoleId);
		if (teamId == null) {
			return null;
		}
		MaiguFubenSourceTeam team = sourceTeamMap.get(teamId);
		if (team == null) {
			return null;
		}
		return team.getMsg();
	}

	public static boolean leaveTeam(Long userRoleId) {
		Integer teamId = teamIdMap.remove(userRoleId);
		if (teamId != null) {
			MaiguFubenSourceTeam team = sourceTeamMap.get(teamId);
			if (team != null) {
				team.removeRole(userRoleId);
				if (team.isCanRemove()) {
					sourceTeamMap.remove(teamId);
				}
			}
			return true;
		}
		return false;
	}
}
