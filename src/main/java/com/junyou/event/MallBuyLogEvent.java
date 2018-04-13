package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 商城购买日志事件
 * @author DaoZheng Yuan
 * 2015年6月15日 上午11:51:58
 */
public class MallBuyLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	private long userRoleId;
	private String roleName;
	private String goodsId;//物品配置id
	private int goodsCount;//物品数量
	private int moneyType;//货币类型
	private long price;//购买价格
	
	
	public MallBuyLogEvent(long userRoleId,String roleName,String goodsId, int goodsCount,int moneyType,long price) {
		super(LogPrintHandle.MAIL_BUY);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		
		this.goodsId = goodsId;
		this.goodsCount = goodsCount;
		this.moneyType = moneyType;
		this.price = price;
	}


	public long getUserRoleId() {
		return userRoleId;
	}


	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public String getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}


	public int getGoodsCount() {
		return goodsCount;
	}


	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}


	public int getMoneyType() {
		return moneyType;
	}


	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}


	public long getPrice() {
		return price;
	}


	public void setPrice(long price) {
		this.price = price;
	}

	
}