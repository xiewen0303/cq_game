package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 组队公会配置
 * @author DaoZheng Yuan
 * 2013-11-4 下午5:14:36
 */
public class TeamPublicConfig extends AdapterPublicConfig{

	//组队最大人数
	private int maxTeamCount;
	
	private Map<Integer,Float> teamXs;
	
	
	public int getMaxTeamCount() {
		return maxTeamCount;
	}

	public void setMaxTeamCount(int maxTeamCount) {
		this.maxTeamCount = maxTeamCount;
	}

	public Float getTeamXs(int count) {
		Float value = teamXs.get(count);
		if(value == null){
			return 0f;
		}
		return value;
	}
	

	public void setTeamXs(Integer count,Float xs) {
		if(teamXs == null){
			teamXs = new HashMap<Integer, Float>();
		}
		teamXs.put(count, xs);
	}


}
