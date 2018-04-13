package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 幸运彩蛋日志
 * @author LiuYu
 * @date 2015-9-16 下午4:58:56
 */
public class GuildDuihuanLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private String name;
	private String goodsId;//获得的道具id
	private int num;//获得的道具数量
	private long gongxian;//消耗的贡献
	public GuildDuihuanLogEvent(long userRoleId,String name, String goodsId, int num,long gongxian) {
		super(LogPrintHandle.GUILD_DUIHUAN_LOG);
		this.userRoleId = userRoleId;
		this.name = name;
		this.goodsId = goodsId;
		this.num = num;
		this.gongxian = gongxian;
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
	public long getGongxian() {
		return gongxian;
	}
	public String getName() {
		return name;
	}
	
	
	
}