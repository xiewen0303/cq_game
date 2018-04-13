package com.junyou.stage.model.stage.territory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.TerritoryPublicConfig;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.datetime.GameSystemTime;

public class TerritoryManager {

	private Map<Long, Integer> roleBgMap = new HashMap<Long, Integer>();

	public Integer getRoleBg(Long userRoleId) {
		Integer banggong = roleBgMap.get(userRoleId);
		if (banggong == null) {
			return 0;
		}
		return banggong;
	}

	public void addRoleBg(Long userRoleId, Integer bg) {
		Integer current = getRoleBg(userRoleId);
		if (current == 0) {
			roleBgMap.put(userRoleId, bg);
		} else {
			roleBgMap.put(userRoleId, bg + current);
		}
	}

	/**
	 * 胜利公会id列表
	 */
	private List<Long> winGuildIdList = new ArrayList<Long>();

	public List<Long> getWinGuildIdList() {
		return winGuildIdList;
	}

	public void addWinGuildId(Long guildId) {
		if (!winGuildIdList.contains(guildId)) {
			winGuildIdList.add(guildId);
		}
	}

	/**
	 * 所有的stageId
	 */
	private List<String> stageIdList = new ArrayList<String>();

	public List<String> getStageIdList() {
		return stageIdList;
	}

	public void addStageId(String stageId) {
		stageIdList.add(stageId);
	}

	/**
	 * 所有参加过的活动的有帮派的roleId
	 */
	private List<Long> roleIds = new ArrayList<Long>();

	public List<Long> getRoleIds() {
		return new ArrayList<Long>(roleIds);
	}

	private Map<Long, List<TerritoryRoleOnline>> roleOnlines = new HashMap<Long, List<TerritoryRoleOnline>>();
	private TerritoryPublicConfig publicConfig;

	public void setPublicConfig(TerritoryPublicConfig publicConfig) {
		this.publicConfig = publicConfig;
	}

	/**
	 * 玩家进入场景
	 * 
	 * @param role
	 */
	public void enter(IRole role) {
		role.setStage(StageManager.getStage(role.getStage().getId()));
		if (role.getBusinessData().getGuildId() != null) {
			Long roleId = role.getId();
			if (!roleIds.contains(roleId)) {
				roleIds.add(roleId);
			}
			List<TerritoryRoleOnline> onlineList = roleOnlines.get(roleId);
			if (onlineList == null) {
				onlineList = new ArrayList<TerritoryRoleOnline>();
				TerritoryRoleOnline roleOnline = new TerritoryRoleOnline();
				roleOnline.setRole(role);
				roleOnline.setEnterTime(GameSystemTime.getSystemMillTime());
				onlineList.add(roleOnline);
			} else {
				for (TerritoryRoleOnline e : onlineList) {
					if (e.getLeaveTime() == 0L) {
						e.setLeaveTime(GameSystemTime.getSystemMillTime());
					}
				}
				TerritoryRoleOnline roleOnline = new TerritoryRoleOnline();
				roleOnline.setRole(role);
				roleOnline.setEnterTime(GameSystemTime.getSystemMillTime());
				onlineList.add(roleOnline);
			}
			roleOnlines.put(roleId, onlineList);
			// 开启加经验，真气定时器，没有帮派的人没有此奖励
			role.startTerritoryAddExpSchedule(publicConfig.getDelay());
		}
	}

	/**
	 * 玩家离开场景
	 * 
	 * @param role
	 */
	public void leave(IRole role) {
		Long roleId = role.getId();
		if (role.getBusinessData().getGuildId() != null) {
			List<TerritoryRoleOnline> onlineList = roleOnlines.get(roleId);
			if (onlineList != null) {
				for (TerritoryRoleOnline e : onlineList) {
					if (e.getLeaveTime() == 0L) {
						e.setLeaveTime(GameSystemTime.getSystemMillTime());
					}
				}
			}
			// 取消加经验，真气定时器
			role.cancelTerritoryAddExpSchedule();
		}
	}

	/**
	 * 获取玩家此次领地战活动中的累计在线时间(单位：秒)
	 * 
	 * @param userRoleId
	 * @return
	 */
	public long getRoleTotalOnlineTime(Long userRoleId) {
		List<TerritoryRoleOnline> onlineList = roleOnlines.get(userRoleId);
		if (onlineList == null || onlineList.size() <= 0) {
			return 0;
		}
		long totalOnlineTime = 0L;
		long firstEnterTime = onlineList.get(0).getEnterTime();
		long lastLeaveTime = onlineList.get(onlineList.size() - 1)
				.getLeaveTime();
		if (lastLeaveTime == 0) {
			lastLeaveTime = GameSystemTime.getSystemMillTime();
		}
		for (TerritoryRoleOnline e : onlineList) {
			long enterTime = e.getEnterTime();
			long leaveTime = e.getLeaveTime();
			if (leaveTime == 0) {
				leaveTime = GameSystemTime.getSystemMillTime();
			}
			totalOnlineTime = totalOnlineTime + leaveTime - enterTime;
		}
		if (totalOnlineTime > (lastLeaveTime - firstEnterTime)) {
			totalOnlineTime = lastLeaveTime - firstEnterTime;
		}
		return totalOnlineTime / 1000L;
	}

	private List<Long> territoryGuildLeaderIds = new ArrayList<Long>();
	public void addTerritoryGuildLeaderId(Long userRoleId){
		territoryGuildLeaderIds.add(userRoleId);
	}

	public List<Long> getTerritoryGuildLeaderIds() {
		return territoryGuildLeaderIds;
	}

	public void clear() {
		roleBgMap.clear();
		winGuildIdList.clear();
		stageIdList.clear();
		roleIds.clear();
		roleOnlines.clear();
		publicConfig = null;
		territoryGuildLeaderIds.clear();
	}
}
