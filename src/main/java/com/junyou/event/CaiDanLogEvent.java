package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 幸运彩蛋日志
 * @author LiuYu
 * @date 2015-9-16 下午4:58:56
 */
public class CaiDanLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private String name;
	private String goodsId;//获得的道具id
	private int num;//获得的道具数量
	private int score;//获得的积分
	private int gold;//消耗的元宝数量
	public CaiDanLogEvent(long userRoleId,String name, String goodsId, int num,int score,int gold) {
		super(LogPrintHandle.CAIDAN_LOG);
		this.userRoleId = userRoleId;
		this.name = name;
		this.goodsId = goodsId;
		this.num = num;
		this.score = score;
		this.gold = gold;
	}

	public long getUserRoleId() {
		return userRoleId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public int getNum() {
		return num;
	}
	public int getScore() {
		return score;
	}
	public int getGold() {
		return gold;
	}
	public String getName() {
		return name;
	}
	
	
	
}