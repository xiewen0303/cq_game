package com.junyou.bus.kuafuarena1v1.util;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.datetime.GameSystemTime;

public class KuafuMatch {

	public KuafuMatch(Long matchId) {
		super();
		this.matchId = matchId;
		startTime = GameSystemTime.getSystemMillTime();
		start = false;
	}

	private Long matchId;
	private boolean start;
	private Map<Long, KuafuMatchMember> members = new HashMap<Long, KuafuMatchMember>();
	private Long startTime;

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public Map<Long, KuafuMatchMember> getMembers() {
		return members;
	}

	public void setMembers(Map<Long, KuafuMatchMember> members) {
		this.members = members;
	}

	public void addMember(KuafuMatchMember member) {
		members.put(member.getRoleId(), member);
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public KuafuMatchMember getMember(Long userRoleId) {
		return members.get(userRoleId);
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isAllOffline() {
		for (KuafuMatchMember e : getMembers().values()) {
			if (e.isOnline()) {
				return false;
			}
		}
		return true;
	}

	public boolean isAllOnline() {
		for (KuafuMatchMember e : getMembers().values()) {
			if (!e.isOnline()) {
				return false;
			}
		}
		return true;
	}

}
