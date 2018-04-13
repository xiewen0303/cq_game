package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 新开服排名活动日志
 * @author LiuYu
 * @date 2015-3-30 下午5:17:30
 */
public class KaiFuPaiMingHDLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public KaiFuPaiMingHDLogEvent(long userRoleId, String name,int mingci, String emailGood, int stype) {
		super(LogPrintHandle.KAIFU_ACTITY);
		this.userRoleId = userRoleId;
		this.name = name;
		this.mingci = mingci;
		this.emailGood = emailGood;
		this.stype = stype;
	}

	private long userRoleId;
	private String name;
	private int mingci;
	private String emailGood;
	private int stype;


	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public int getMingci() {
		return mingci;
	}
	public String getEmailGood() {
		return emailGood;
	}
	public int getStype() {
		return stype;
	}
	
	
}