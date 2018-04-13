package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 获得数值日志
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-8 下午2:47:00 
 */
public class GetNumLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	private int sort;//小类编号
	private Long userRoleId;//角色Id
	private String userName;//角色名称
	private int getType;//获得类型
	private long getVal;//获得数量
	private Long getReYb;//获得充值元宝（只有元宝才有，其他为null）
	private Long getNoReYb;//获得充值元宝中非充值元宝（只有元宝才有，其他为null）
	private long beforeVal;//获得前值
	private long afterVal;//获得后值
	private int beizhu;//备注
	
	/**
	 * 获得数值日志
	 * @param sort 小类编号
	 * @param userRoleId 角色Id
	 * @param userName 角色名称
	 * @param getType 获得类型
	 * @param getVal 获得数量
	 * @param getReYb 获得充值元宝（只有元宝才有，其他为null）
	 * @param getNoReYb 获得充值元宝中非充值元宝（只有元宝才有，其他为null）
	 * @param beforeVal 获得前值
	 * @param afterVal 获得后值
	 */
	public GetNumLogEvent(int sort,Long userRoleId, String userName, int getType,long getVal, Long getReYb, Long getNoReYb, long beforeVal, long afterVal, int beizhu) {
		super(LogPrintHandle.GET_NUM_LOG);
		
		this.sort = sort;
		this.userRoleId = userRoleId;
		this.userName = userName;
		this.getType = getType;
		this.getVal = getVal;
		this.beforeVal = beforeVal;
		this.afterVal = afterVal;
		this.beizhu = beizhu;
		
		if(getReYb != null){
			this.getReYb = getReYb;
		}
		
		if(getNoReYb != null){
			this.getNoReYb = getNoReYb;
		}
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

	public int getGetType() {
		return getType;
	}

	public long getGetVal() {
		return getVal;
	}

	public Long getGetReYb() {
		return getReYb;
	}

	public Long getGetNoReYb() {
		return getNoReYb;
	}

	public long getBeforeVal() {
		return beforeVal;
	}

	public long getAfterVal() {
		return afterVal;
	}
}