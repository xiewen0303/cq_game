package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 消耗数值日志
 * @date 2015-1-8 下午2:47:00 
 */
public class ComsumeNumLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	private int sort;//小类编号
	private Long userRoleId;//角色Id
	private String userName;//角色名称
	private int consuType;//消耗类型
	private long consuVal;//消耗数量
	private Long consuReYb;//消耗充值元宝（只有元宝才有，其他为null）
	private Long consuNoReYb;//消耗充值元宝中非充值元宝（只有元宝才有，其他为null）
	private long beforeVal;//消耗前值
	private long afterVal;//消耗后值
	private int beizhu;//备注
	private int level;//玩家当前等级
	
	/**
	 * 消耗数值日志
	 * @param sort 小类编号
	 * @param userRoleId 角色Id
	 * @param userName 角色名称
	 * @param consuType 消耗类型
	 * @param consuVal 消耗数量
	 * @param consuReYb 消耗充值元宝（只有元宝才有，其他为null）
	 * @param consuNoReYb 消耗充值元宝中非充值元宝（只有元宝才有，其他为null）
	 * @param beforeVal 消耗前值
	 * @param afterVal 消耗后值
	 * @param beizhu 备注
	 */
	public ComsumeNumLogEvent(int sort,Long userRoleId, String userName, int consuType,long consuVal, Long consuReYb, Long consuNoReYb, long beforeVal, long afterVal, int beizhu,int level) {
		super(LogPrintHandle.CONSUME_NUM_LOG);
		
		this.sort = sort;
		this.userRoleId = userRoleId;
		this.userName = userName;
		this.consuType = consuType;
		this.consuVal = consuVal;
		this.beforeVal = beforeVal;
		this.afterVal = afterVal;
		this.beizhu = beizhu;
		
		if(consuReYb != null){
			this.consuReYb = consuReYb;
		}
		
		if(consuNoReYb != null){
			this.consuNoReYb = consuNoReYb;
		}
		
		this.level = level;
	}
	public int getSort() {
		return sort;
	}

	public int getBeizhu() {
		return beizhu;
	}
	public Long getUserRoleId() {
		return userRoleId;
	}

	public String getUserName() {
		return userName;
	}

	public int getConsuType() {
		return consuType;
	}

	public long getConsuVal() {
		return consuVal;
	}

	public Long getConsuReYb() {
		return consuReYb;
	}

	public Long getConsuNoReYb() {
		return consuNoReYb;
	}

	public long getBeforeVal() {
		return beforeVal;
	}

	public long getAfterVal() {
		return afterVal;
	}
	
	public int getLevel() {
		return level;
	}
	
}