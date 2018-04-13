package com.junyou.public_.rank.vo;

public interface IRankVo {
	
	Long  getUserRoleId();
	
	Integer getRank();
	
	Integer getVipLevel();
	
	String getName();
	
	String getUserGuildName();
	
	Integer getConfigId();
	
	/**每种排行对应字段*/
	String getUncertain();
	
	Object[] getOutData();
}
