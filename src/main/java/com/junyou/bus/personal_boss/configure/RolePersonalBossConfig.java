package com.junyou.bus.personal_boss.configure;

import java.util.Map;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;

public class RolePersonalBossConfig extends AbsFubenConfig {
	private String monsterid;
	private int cishu;
	private Map<String, Integer> item;
	private Map<String, Integer> jlitem;
	private int level;
	private int map;
	private int x;
	private int y;
	private int tztime;
	
	public String getMonsterid() {
		return monsterid;
	}
	public void setMonsterid(String monsterid) {
		this.monsterid = monsterid;
	}
	public int getCishu() {
		return cishu;
	}
	public void setCishu(int cishu) {
		this.cishu = cishu;
	}
	public Map<String, Integer> getItem() {
		return item;
	}
	public void setItem(Map<String, Integer> item) {
		this.item = item;
	}
	public Map<String, Integer> getJlitem() {
		return jlitem;
	}
	public void setJlitem(Map<String, Integer> jlitem) {
		this.jlitem = jlitem;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getTztime() {
		return tztime;
	}
	
	public void setTztime(int tztime) {
		this.tztime = tztime;
	}
	@Override
	public int getMapType() {
		return MapType.PERSONAL_BOSS;
	}
	@Override
	public int getFubenType() {
		return 0;
	}
	@Override
	public short getExitCmd() {
		return ClientCmdType.PERSONAL_BOSS_EXIT;
	}
	@Override
	public boolean isAutoProduct() {
		return false;
	}
	
	
}
