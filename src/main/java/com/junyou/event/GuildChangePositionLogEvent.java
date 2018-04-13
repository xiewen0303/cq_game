package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 门派职位变更日志
 * @author lxn
 * @date 2015-6-5
 */
public class GuildChangePositionLogEvent extends AbsGameLogEvent {
 
	private static final long serialVersionUID = 1L;
	private long guildId;
	private String guildName;
	private long userRoleId; 
	private int  oldPosition; //旧职位
	private int  newPosition; //新职位
	private int  category; //0普通成员职位变更，1会长转让
	
	private long leaderUserRoleId; //category=0表示会长，category= 1表示上一任会长
	
	public GuildChangePositionLogEvent(int category,long userRoleId,long  guild,String guildName,int oldPosition, int newPosition,long leaderUserRoleId) {
		 super(LogPrintHandle.GUILD_POSITION_CHANGE_LOG);
		 this.guildId = guild;
		 this.guildName = guildName;
		 this.userRoleId = userRoleId;
		 this.oldPosition = oldPosition;
		 this.newPosition = newPosition;
		 this.category = category;
		 this.leaderUserRoleId = leaderUserRoleId;
	}
   
	
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public long getLeaderUserRoleId() {
		return leaderUserRoleId;
	}
	public void setLeaderUserRoleId(long leaderUserRoleId) {
		this.leaderUserRoleId = leaderUserRoleId;
	}
	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public int getOldPosition() {
		return oldPosition;
	}

	public void setOldPosition(int oldPosition) {
		this.oldPosition = oldPosition;
	}

	public int getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}

	 

}