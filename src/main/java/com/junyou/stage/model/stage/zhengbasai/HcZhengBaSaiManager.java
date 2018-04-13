package com.junyou.stage.model.stage.zhengbasai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.HcZBSPublicConfig;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.datetime.GameSystemTime;

public class HcZhengBaSaiManager {
	private static HcZhengBaSaiManager manager = new HcZhengBaSaiManager();

	public static HcZhengBaSaiManager getManager() {
		return manager;
	}

	private long zhanglingTime=0; //旗子刚被占领的时间点
	public long getZhanglingTime() {
		return zhanglingTime;
	}
	public void setZhanglingTime(long zhanglingTime) {
		this.zhanglingTime = zhanglingTime;
	}
	private Map<Long, Integer> roleBgMap = new HashMap<Long, Integer>();

	public Integer getRoleBg(Long userRoleId) {
		Integer banggong = roleBgMap.get(userRoleId);
		if (banggong == null) {
			return 0;
		}
		return banggong;
	}

	public Map<Long, Integer> getRoleBgMap() {
		return roleBgMap;
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

	private List<HcZhengBaSaiStage> activeStages = new ArrayList<>();

	public void addStage(HcZhengBaSaiStage stage) {
		activeStages.add(stage);
		StageManager.addStageCopy(stage);
	}

	public List<HcZhengBaSaiStage> getStages() {
		return activeStages;
	}

	public void stop() {
		for (HcZhengBaSaiStage stage : activeStages) {
			stage.stop();
		}
	}

	/**
	 * 所有参加过的活动的有帮派的roleId
	 */
	private List<Long> roleIds = new ArrayList<Long>();

	public List<Long> getRoleIds() {
		return new ArrayList<Long>(roleIds);
	}

	private Map<Long, List<HcZhengBaSaiRoleOnline>> roleOnlines = new HashMap<Long, List<HcZhengBaSaiRoleOnline>>();
	private HcZBSPublicConfig publicConfig;

	public void setPublicConfig(HcZBSPublicConfig publicConfig) {
		this.publicConfig = publicConfig;
	}
	public HcZBSPublicConfig getPublicConfig() {
		return publicConfig;
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
			List<HcZhengBaSaiRoleOnline> onlineList = roleOnlines.get(roleId);
			if (onlineList == null) {
				onlineList = new ArrayList<HcZhengBaSaiRoleOnline>();
				HcZhengBaSaiRoleOnline roleOnline = new HcZhengBaSaiRoleOnline();
				roleOnline.setRole(role);
				roleOnline.setEnterTime(GameSystemTime.getSystemMillTime());
				onlineList.add(roleOnline);
			} else {
				for (HcZhengBaSaiRoleOnline e : onlineList) {
					if (e.getLeaveTime() == 0L) {
						e.setLeaveTime(GameSystemTime.getSystemMillTime());
					}
				}
				HcZhengBaSaiRoleOnline roleOnline = new HcZhengBaSaiRoleOnline();
				roleOnline.setRole(role);
				roleOnline.setEnterTime(GameSystemTime.getSystemMillTime());
				onlineList.add(roleOnline);
			}
			roleOnlines.put(roleId, onlineList);
			// 开启加经验，真气定时器，没有帮派的人没有此奖励
			role.startHcZhengBaSaiAddExpSchedule(publicConfig.getDelay());
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
			List<HcZhengBaSaiRoleOnline> onlineList = roleOnlines.get(roleId);
			if (onlineList != null) {
				for (HcZhengBaSaiRoleOnline e : onlineList) {
					if (e.getLeaveTime() == 0L) {
						e.setLeaveTime(GameSystemTime.getSystemMillTime());
					}
				}
			}
			// 取消加经验，真气定时器
			role.cancelHcZhengBaSaiAddExpSchedule();
		}
	}

	/**
	 * 获取玩家此次领地战活动中的累计在线时间(单位：秒)
	 * 
	 * @param userRoleId
	 * @return
	 */
	public long getRoleTotalOnlineTime(Long userRoleId) {
		List<HcZhengBaSaiRoleOnline> onlineList = roleOnlines.get(userRoleId);
		if (onlineList == null || onlineList.size() <= 0) {
			return 0;
		}
		long totalOnlineTime = 0L;
		long firstEnterTime = onlineList.get(0).getEnterTime();
		long lastLeaveTime = onlineList.get(onlineList.size() - 1).getLeaveTime();
		if (lastLeaveTime == 0) {
			lastLeaveTime = GameSystemTime.getSystemMillTime();
		}
		for (HcZhengBaSaiRoleOnline e : onlineList) {
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

	private List<Long> GuildLeaderIds = new ArrayList<Long>();

	public void addGuildLeaderId(Long userRoleId) {
		GuildLeaderIds.add(userRoleId);
	}

	public List<Long> getGuildLeaderIds() {
		return GuildLeaderIds;
	}

	public void clear() {
		roleBgMap.clear();
		winGuildIdList.clear();
		stageIdList.clear();
		roleIds.clear();
		roleOnlines.clear();
		publicConfig = null;
		GuildLeaderIds.clear();
		zhanglingTime = 0;
	}
	
}
