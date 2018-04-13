package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;


public class DayFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig{
	private int level;
	private int mapId;
	private int count;
	private ReadOnlyMap<String, Integer> first;
	private ReadOnlyMap<String, Integer> dayFirst;
	private ReadOnlyMap<String, Integer> prop;
	private int money;
	private int exp;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ReadOnlyMap<String, Integer> getFirst() {
		return first;
	}
	public void setFirst(Map<String, Integer> first) {
		this.first = new ReadOnlyMap<>(first);
	}
	public ReadOnlyMap<String, Integer> getDayFirst() {
		return dayFirst;
	}
	public void setDayFirst(Map<String, Integer> dayFirst) {
		this.dayFirst = new ReadOnlyMap<>(dayFirst);
	}
	public ReadOnlyMap<String, Integer> getProp() {
		return prop;
	}
	public void setProp(Map<String, Integer> prop) {
		this.prop = new ReadOnlyMap<>(prop);
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	@Override
	public short getExitCmd() {
		return InnerCmdType.B_EXIT_FUBEN;
	}
	@Override
	public int getMapType() {
		return MapType.FUBEN_MAP;
	}
	@Override
	public String getConfigName() {
		return "DayFubenConfig--" + getId();
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = null;
		if(first != null && first.size() > 0){
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(first);
		}
		if(dayFirst != null && dayFirst.size() > 0){
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(dayFirst);
		}
		if(prop != null && prop.size() > 0){
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(prop);
		}
		return list;
	}
	@Override
	public boolean isAutoProduct() {
		return true;
	}
	@Override
	public int getFubenType() {
		return GameConstants.FUBEN_TYPE_DAY;
	}
	

}
