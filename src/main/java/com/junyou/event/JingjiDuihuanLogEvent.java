package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 竞技场兑换日志
 * @author LiuYu
 * @date 2015-3-30 下午5:17:30
 */
public class JingjiDuihuanLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public JingjiDuihuanLogEvent(long userRoleId, String name,int rongyu, String goodsId, int num) {
		super(LogPrintHandle.JINGJI_DUIHUAN);
		this.userRoleId = userRoleId;
		this.name = name;
		this.rongyu = rongyu;
		this.goodsId = goodsId;
		this.num = num;
	}

	private long userRoleId;
	private String name;
	private int rongyu;
	private String goodsId;
	private int num;


	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public int getRongyu() {
		return rongyu;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public int getNum() {
		return num;
	}
	
	
}