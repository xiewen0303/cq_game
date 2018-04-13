package com.junyou.public_.guild.manager;

import java.util.Comparator;

import com.junyou.public_.guild.entity.GuildMember;

public class GuildMemberComparator implements Comparator<GuildMember>{

	@Override
	public int compare(GuildMember o1, GuildMember o2) {
		if(o1.getPostion() != o2.getPostion()){
			return o1.getPostion() - o2.getPostion();
		}
		if(o2.isOnline()){
			return o1.isOnline() ? -1 : 1;
		}
		return -1;
	}


}
