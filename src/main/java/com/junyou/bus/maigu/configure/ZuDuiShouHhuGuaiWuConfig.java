package com.junyou.bus.maigu.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class ZuDuiShouHhuGuaiWuConfig extends AbsVersion {
	private int boshu;
	private List<Map<String, Integer>> monsters = new ArrayList<Map<String, Integer>>();

	public int getBoshu() {
		return boshu;
	}

	public void setBoshu(int boshu) {
		this.boshu = boshu;
	}

	public List<Map<String, Integer>> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<Map<String, Integer>> monsters) {
		this.monsters = monsters;
	}

}
