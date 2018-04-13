package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 玄铁兑换日志
 * @author LiuYu
 * @date 2015-3-30 下午5:17:30
 */
public class XuanTieDuihuanLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public XuanTieDuihuanLogEvent(long userRoleId, String name,int xtz, String goodsId, int num) {
		super(LogPrintHandle.XUANTIE_DUIHUAN);
		this.userRoleId = userRoleId;
		this.name = name;
		this.xtz = xtz;
		this.goodsId = goodsId;
		this.num = num;
	}

	private long userRoleId;
	private String name;
	private int xtz;
	private String goodsId;
	private int num;


	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public int getRongyu() {
		return xtz;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public int getNum() {
		return num;
	}
	
	
}