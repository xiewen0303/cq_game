package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 幸运转盘
 * @author wind
 */
public class HuiYanShiJinLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; //玩家角色ID
	private int consumeGold; //消耗的元宝
	private String roleName; //玩家名称
	private int level;//参与的活动等级
	private int count;//全服第多少次
	private int subId;//子活动ID
	private int hGlod;//获得的元宝
	 
	
	public HuiYanShiJinLogEvent(Long userRoleId, int consumeGold,int hGlod, String roleName, 
			int level, int count,int subId) {
			super(LogPrintHandle.REFABU_HUIYANSHIJN);
			this.userRoleId = userRoleId;
			this.consumeGold = consumeGold;
			this.hGlod = hGlod;
			this.roleName = roleName;
			this.level = level;
			this.count = count;
			this.subId = subId;
	} 

	public Long getUserRoleId() {
		return userRoleId;
	}

	public int getConsumeGold() {
		return consumeGold;
	}

	public String getRoleName() {
		return roleName;
	}

	public int getLevel() {
		return level;
	}

	public int getSubId() {
		return subId;
	}

	public int gethGlod() {
		return hGlod;
	}

	public int getCount() {
		return count;
	}


}