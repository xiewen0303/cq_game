package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 宝石/道具合成系统日志
 * 
 * @date 2015-6-5
 */
public class JewelLogEvent extends AbsGameLogEvent {
	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private int category;// 0卸载宝石，1装上宝石，2道具合成,3,(新)道具合成
	private String goodId; // 宝石id
	// 宝石合成
	private String newGoodId; // 合成的新宝石goodId
	private int newNum; // 合成宝石生成的数量
	private String consumeGoodId;// 消耗的宝石goodId
	private int consumeNum; // 合成宝石消耗的宝石数量

	public JewelLogEvent(long userRoleId, int category, String goodId) {
		super(LogPrintHandle.JEWEL_LOG);
		this.category = category;
		this.userRoleId = userRoleId;
		this.goodId = goodId;
	}

	// 宝石合成
	public JewelLogEvent(long userRoleId, int category, String newGoodId, int newNum, String consumeGoodId, int consumeNum) {
		super(LogPrintHandle.JEWEL_LOG);
		this.category = category;
		this.userRoleId = userRoleId;
		this.newGoodId = newGoodId;
		this.newNum = newNum;
		this.consumeGoodId = consumeGoodId;
		this.consumeNum = consumeNum;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public String getNewGoodId() {
		return newGoodId;
	}

	public void setNewGoodId(String newGoodId) {
		this.newGoodId = newGoodId;
	}

	public int getNewNum() {
		return newNum;
	}

	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}

	public String getConsumeGoodId() {
		return consumeGoodId;
	}

	public void setConsumeGoodId(String consumeGoodId) {
		this.consumeGoodId = consumeGoodId;
	}

	public int getConsumeNum() {
		return consumeNum;
	}

	public void setConsumeNum(int consumeNum) {
		this.consumeNum = consumeNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}