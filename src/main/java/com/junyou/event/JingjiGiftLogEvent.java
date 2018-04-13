package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 竞技场奖励日志
 * @author LiuYu
 * @date 2015-3-30 下午4:56:16
 */
public class JingjiGiftLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	

	public JingjiGiftLogEvent(long userRoleId, String name, int rank,int exp, int rongyu) {
		super(LogPrintHandle.JINGJI_GIFT);
		this.userRoleId = userRoleId;
		this.name = name;
		this.rank = rank;
		this.exp = exp;
		this.rongyu = rongyu;
	}

	private long userRoleId;
	private String name;
	private int rank;
	private int exp;
	private int rongyu;

	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public int getRank() {
		return rank;
	}
	public int getExp() {
		return exp;
	}
	public int getRongyu() {
		return rongyu;
	}
	
	
	
}