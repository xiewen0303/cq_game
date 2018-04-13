package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 技能招唤配置表
 *
 * @author DaoZhen Yuan
 * @date 2013-11-12 16:50:31
 */
public class ZhaoHuanBiaoConfig extends AbsVersion{

	private Integer level;

	private Map<Integer,String> monsterIds;
	
	
	public Map<Integer, String> getMonsterIds() {
		return monsterIds;
	}

	public void setMonsterIds(Integer type, String monsterId) {
		if(monsterIds == null){
			monsterIds = new HashMap<Integer, String>();
		}
		
		monsterIds.put(type, monsterId);
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

}
