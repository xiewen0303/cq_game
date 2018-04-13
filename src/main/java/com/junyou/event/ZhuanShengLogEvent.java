package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 坐骑升阶
 * 
 * @author wind
 * 
 */
public class ZhuanShengLogEvent extends AbsGameLogEvent {
 

	private static final long serialVersionUID = 1L;
	
	private Long userRoleId;
	private String name;
	private Integer zsLevel;//转生后登记
	private String itemId1;//转生消耗道具大类id
	private Integer itemCount;//消耗道具数量
	
	
	
	public ZhuanShengLogEvent(Long userRoleId, String name,Integer zsLevel,String itemId1,Integer itemCount) {
		super(LogPrintHandle.ZHUANSHENG);
		this.userRoleId = userRoleId;
		this.name = name;
		this.zsLevel = zsLevel;
		this.itemId1 = itemId1;
		this.itemCount = itemCount;
	}
	
	public Long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getName() {
		return name;
	}

	public Integer getZsLevel() {
		return zsLevel;
	}

	public String getItemId1() {
		return itemId1;
	}

	public Integer getItemCount() {
		return itemCount;
	}
	
}