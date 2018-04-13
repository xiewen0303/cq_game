package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.List;

/**
 * 野外boss
 * @author LiuYu
 * @date 2015-4-21 下午2:20:59
 */
public class YWBossPublicConfig extends AdapterPublicConfig{
	
	private List<String> lists = null;
 
	
	public List<String> getRankAwards() {
		return lists;
	}
	
	public void setRankAwards(List<String> rankAwards) {
		this.lists = rankAwards;
	}
	
	public String getAwardsByRank(int index){
		index = index > 3? 3:index;
		return lists.get(index);
	}
	
}
