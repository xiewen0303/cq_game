package com.junyou.bus.shenmo.util;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.datetime.GameSystemTime;

public class KuafuMatch4v4 {

	public KuafuMatch4v4(Long matchId) {
		super();
		this.matchId = matchId;
		startTime = GameSystemTime.getSystemMillTime();
		start = false;
	}

	private Long matchId;
	private boolean start;
	private Map<Long, KuafuMatch4v4Member> members = new HashMap<Long, KuafuMatch4v4Member>();
	private Long startTime;

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public Map<Long, KuafuMatch4v4Member> getMembers() {
		return members;
	}

	public void setMembers(Map<Long, KuafuMatch4v4Member> members) {
		this.members = members;
	}

	public void addMember(KuafuMatch4v4Member member) {
		members.put(member.getRoleId(), member);
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public KuafuMatch4v4Member getMember(Long userRoleId) {
		return members.get(userRoleId);
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isAllOffline() {
		for (KuafuMatch4v4Member e : getMembers().values()) {
			if (e.isOnline()) {
				return false;
			}
		}
		return true;
	}

	public boolean isAllOnline() {
		for (KuafuMatch4v4Member e : getMembers().values()) {
			if (!e.isOnline()) {
				return false;
			}
		}
		return true;
	}

}
