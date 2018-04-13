package com.junyou.public_.guild.manager;

import java.util.Comparator;

import com.junyou.public_.guild.entity.Guild;

public class GuildComparator implements Comparator<Guild>{

	@Override
	public int compare(Guild o1, Guild o2) {
		if(!o1.getLevel().equals(o2.getLevel())){
			return o2.getLevel() - o1.getLevel();
		}
		int level1 = o1.getLeader().getLevel();
		int level2 = o2.getLeader().getLevel();
		if(level1 != level2){
			return level2 - level1;
		}
		int size1 = o1.getSize();
		int size2 = o2.getSize();
		if(size1 != size2){
			return size2 - size1;
		}
		return 0;
	}

}
