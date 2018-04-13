package com.junyou.public_.guild.manager;

import java.util.Comparator;

import com.junyou.public_.guild.entity.GuildMember;

/**
 * 弹劾门主的新门主筛选规则
 * @author lxn
 * 转让给门派贡献高的一方 ，若派贡献相同，则转让给最先进入门派的一方					
 *
 */
public class SortMemberComparator implements Comparator<GuildMember> {
	@Override
	public int compare(GuildMember m1, GuildMember m2) {
		if (m1.getTotalGongxian().longValue() > m2.getTotalGongxian().longValue()) {
			return -1;//负数表示m1在前面
		} else if (m1.getTotalGongxian().longValue() == m2.getTotalGongxian().longValue()) {
			if (m1.getEnterTime() < m2.getEnterTime()) {
				return -1;
			}
		}
		return 1;
	};

}
