package com.junyou.public_.guild.util;

import com.junyou.constants.GameConstants;

public class GuildUtil {
	/**
	 * 是否是管理层
	 * @param postion
	 * @return
	 */
	public static boolean isLeaderOrViceLeader(int postion){
		return postion == GameConstants.GUILD_LEADER_POSTION || postion == GameConstants.GUILD_VIC_LEADER_POSTION;
	}
}
