package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 混沌战场日志
 * @author LiuYu
 * @date 2015-9-9 上午9:47:17
 */
public class ChaosLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private String name;
	private int cengId;//当前层
	private int rank;//当前排名
	private int score;//混沌积分
	
	public ChaosLogEvent(Long userRoleId,String name, int cengId,int rank,int score) {
			super(LogPrintHandle.CHAOS_LOG);
			this.userRoleId = userRoleId;
			this.name = name;
			this.cengId = cengId;
			this.rank = rank;
			this.score = score;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public String getName() {
		return name;
	}

	public int getCengId() {
		return cengId;
	}

	public int getRank() {
		return rank;
	}

	public int getScore() {
		return score;
	} 
	

}