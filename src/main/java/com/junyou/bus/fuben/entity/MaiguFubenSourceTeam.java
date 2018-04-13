package com.junyou.bus.fuben.entity;

import java.util.HashSet;
import java.util.Set;

import com.junyou.constants.GameConstants;
import com.junyou.utils.datetime.GameSystemTime;
/**
 * 多人副本队伍(原服)
 * @author LiuYu
 * @date 2015-7-7 上午10:58:20
 */
public class MaiguFubenSourceTeam {
	// 队伍ID
	private int teamId;
	// 成员列表list
	private Set<Long> roleIdSet = new HashSet<>();
	// 所属副本ID
	private int belongFubenId;
	private Object[] msg;
	private long nextTime;
	
	public MaiguFubenSourceTeam(Integer teamId,Integer fubenId) {
		this.teamId = teamId;
		belongFubenId = fubenId;
	}

	public int getTeamId() {
		return teamId;
	}

	public Set<Long> getRoleIdSet() {
		return roleIdSet;
	}

	public int getBelongFubenId() {
		return belongFubenId;
	}
	
	public void addRole(Long userRoleId){
		roleIdSet.add(userRoleId);
	}
	public void removeRole(Long userRoleId){
		roleIdSet.remove(userRoleId);
	}
	public boolean isCanRemove(){
		return roleIdSet.size() < 1;
	}

	public Object[] getMsg() {
		if(GameSystemTime.getSystemMillTime() < nextTime){
			return null;
		}
		nextTime = GameSystemTime.getSystemMillTime() + GameConstants.MAIGU_FUBEN_YAOQING_INTERVAL;
		if(msg == null){
			msg = new Object[]{null,belongFubenId,teamId};
		}
		return msg;
	}
		
	
}
