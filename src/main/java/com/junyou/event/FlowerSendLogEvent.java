package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 *送花日志
 * @date 2015-6-5
 */
public class FlowerSendLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long sendUserRoleId; //送花人
	private long receiverUserRoleId; //收花人
	private int configId; //鲜花配置表id
	private String goodId;

	public FlowerSendLogEvent(long sendUserRoleId,long receiverUserRoleId, int configId,String goodId ) {
		super(LogPrintHandle.FLOWER_SEND);
		this.sendUserRoleId = sendUserRoleId;
		this.receiverUserRoleId = receiverUserRoleId;
		this.configId  = configId;
		this.goodId  = goodId;
	}
	
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public long getSendUserRoleId() {
		return sendUserRoleId;
	}
	public void setSendUserRoleId(long sendUserRoleId) {
		this.sendUserRoleId = sendUserRoleId;
	}
	public long getReceiverUserRoleId() {
		return receiverUserRoleId;
	}
	public void setReceiverUserRoleId(long receiverUserRoleId) {
		this.receiverUserRoleId = receiverUserRoleId;
	}
}