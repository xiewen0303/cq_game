package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 
 * @description:套装象位升星日志 
 *
 *	@author ChuBin
 *
 * @date 2016-12-16
 */
public class EquipXiangWeiLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; 
	private Integer buwei;//部位
	private String goodsId;//升阶时使用的装备物品Id
	private JSONArray consumeItem;// 消耗物品信息
	private int consumeBgold;// 消耗绑定元宝数
	private int consumeGold;// 消耗元宝数
	private int beginLevel; // 强化之前等级
	private int endLevel; // 强化之后等级
	private int beginStar;// 强化前星数
	private int endStar;// 强化后星数

	public EquipXiangWeiLogEvent(Long userRoleId, Integer buwei, String goodsId, JSONArray consumeItem, int consumeBgold,
			int consumeGold, int beginLevel, int endLevel, int beginStar, int endStar) {
		super(LogPrintHandle.TAOZHUANG_XIANGWEI_LOG);
		this.userRoleId = userRoleId;
		this.buwei = buwei;
		this.goodsId = goodsId;
		this.consumeItem = consumeItem;
		this.consumeBgold = consumeBgold;
		this.consumeGold = consumeGold;
		this.beginLevel = beginLevel;
		this.endLevel = endLevel;
		this.beginStar = beginStar;
		this.endStar = endStar;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getBuwei() {
		return buwei;
	}

	public void setBuwei(Integer buwei) {
		this.buwei = buwei;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public JSONArray getConsumeItem() {
		return consumeItem;
	}

	public void setConsumeItem(JSONArray consumeItem) {
		this.consumeItem = consumeItem;
	}

	public int getConsumeBgold() {
		return consumeBgold;
	}

	public void setConsumeBgold(int consumeBgold) {
		this.consumeBgold = consumeBgold;
	}

	public int getConsumeGold() {
		return consumeGold;
	}

	public void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
	}

	public int getBeginLevel() {
		return beginLevel;
	}

	public void setBeginLevel(int beginLevel) {
		this.beginLevel = beginLevel;
	}

	public int getEndLevel() {
		return endLevel;
	}

	public void setEndLevel(int endLevel) {
		this.endLevel = endLevel;
	}

	public int getBeginStar() {
		return beginStar;
	}

	public void setBeginStar(int beginStar) {
		this.beginStar = beginStar;
	}

	public int getEndStar() {
		return endStar;
	}

	public void setEndStar(int endStar) {
		this.endStar = endStar;
	}
}