package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 幸运彩蛋日志
 * @author LiuYu
 * @date 2015-9-16 下午4:58:56
 */
public class GongxunDuihuanLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private String name;
	private String goodsId;//获得的道具id
	private int num;//获得的道具数量
	private long gongxun;//消耗的贡献
	public GongxunDuihuanLogEvent(long userRoleId,String name, String goodsId, int num,long gongxun) {
		super(LogPrintHandle.KUAFU_JINGJI_DUIHUAN_LOG);
		this.userRoleId = userRoleId;
		this.name = name;
		this.goodsId = goodsId;
		this.num = num;
		this.gongxun = gongxun;
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
	public long getGongxun() {
		return gongxun;
	}
	public String getName() {
		return name;
	}
	
	
	
}