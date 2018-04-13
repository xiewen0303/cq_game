package com.junyou.bus.lianyuboss.configure;

import java.util.Map;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;

public class LianyuBossConfig extends AbsFubenConfig{
	private int configId;
	private Map<String, Integer> rewards;
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public Map<String, Integer> getRewards() {
		return rewards;
	}
	public void setRewards(Map<String, Integer> rewards) {
		this.rewards = rewards;
	}
	 
	@Override
	public int getMapType() {
		return MapType.GUILD_LIANYU_BOSS;
	}
	@Override
	public int getFubenType() {
		return GameConstants.FUBEN_GUILD_LIANYU;
	}
	@Override
	public short getExitCmd() {
		return ClientCmdType.LIANYU_BOSS_EXIT;
	}
	@Override
	public boolean isAutoProduct() {
		return false;
	}
	

}
