package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.common.ObjectUtil;

public class ShouhuFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig{
	private int level;
	private int exp;
	private int money;
	private int zhenqi;
	private String itemId;//首次通关奖励物品
	private String monsterId;//本波怪物id
	private Integer monsterCount;//本波怪物数量
	private int needMoney;//扫到消耗银两
	

	@Override
	public int getMapType() {
		return MapType.SAVE_FUBEN_MAP;
	}

	@Override
	public short getExitCmd() {
		return 0;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getZhenqi() {
		return zhenqi;
	}

	public void setZhenqi(int zhenqi) {
		this.zhenqi = zhenqi;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}

	public Integer getMonsterCount() {
		return monsterCount;
	}

	public void setMonsterCount(Integer monsterCount) {
		this.monsterCount = monsterCount;
	}

	public int getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}

	@Override
	public String getConfigName() {
		return "ShouhuFubenConfig--" + getId();
	}

	@Override
	public List<Map<String, Integer>> getCheckMap() {
		if(!ObjectUtil.strIsEmpty(itemId)){
			List<Map<String, Integer>> list = new ArrayList<>();
			Map<String, Integer> map = new HashMap<>();
			map.put(itemId, 1);
			list.add(map);
			return list;
		}
		return null;
	}

	@Override
	public boolean isAutoProduct() {
		return false;
	}@Override
	public int getFubenType() {
		return GameConstants.FUBEN_TYPE_SHOUHU;
	}
}
