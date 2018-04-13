package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 幸运彩蛋兑换日志
 * @author LiuYu
 * @date 2015-9-16 下午4:58:56
 */
public class CaiDanDuihuanLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private String name;
	private String goodsId;//兑换的道具id
	private int score;//兑换道具消耗积分
	public CaiDanDuihuanLogEvent(long userRoleId,String name, String goodsId, int score) {
		super(LogPrintHandle.CAIDAN_DUIHUAN_LOG);
		this.userRoleId = userRoleId;
		this.name = name;
		this.goodsId = goodsId;
		this.score = score;
	}

	public long getUserRoleId() {
		return userRoleId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public int getScore() {
		return score;
	}
	public String getName() {
		return name;
	}
	
	
	
}