package com.junyou.public_.trade;

 

public class TradeParam {
 
	private Object[] selfClientParams;
	private Object[] otherClientParams;
	private String selfName;
	private String otherName;

	private long selfMoney;
	private long otherMoney;
	
	private boolean success;
	
	private Object[] errorCode;

	/**
	 * 是否成功
	 * @return true:成功
	 */
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 获取错误提示参数
	 * @return
	 */
	public Object[] getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Object[] errorCode) {
		this.errorCode = errorCode;
	}

	
	
//	/**
//	 * 获取自己的金钱变化
//	 * @return
//	 */
//	public int[] getSelfMoney() {
//		return selfMoney;
//	}
//
//	public void setSelfMoney(int[] selfMoney) {
//		this.selfMoney = selfMoney;
//	}
//
//	/**
//	 * 获取另一个人的金钱数据
//	 * @return
//	 */
//	public int[] getOtherMoney() {
//		return otherMoney;
//	}
//
//	public void setOtherMoney(int[] otherMoney) {
//		this.otherMoney = otherMoney;
//	}

	public long getSelfMoney() {
		return selfMoney;
	}

	public void setSelfMoney(long selfMoney) {
		this.selfMoney = selfMoney;
	}

	public long getOtherMoney() {
		return otherMoney;
	}

	public void setOtherMoney(long otherMoney) {
		this.otherMoney = otherMoney;
	}

	/**
	 * 获取自己的客户端参数
	 * @return
	 */
	public Object[] getSelfClientParams() {
		return selfClientParams;
	}

	public void setSelfClientParams(Object[] selfClientParams) {
		this.selfClientParams = selfClientParams;
	}

	/**
	 * 获取另一个人的客户端参数
	 * @return
	 */
	public Object[] getOtherClientParams() {
		return otherClientParams;
	}

	public void setOtherClientParams(Object[] otherClientParams) {
		this.otherClientParams = otherClientParams;
	}

	/**
	 * 获取自己的名字
	 * @return
	 */
	public String getSelfName() {
		return selfName;
	}

	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}

	/**
	 * 获取另一人的名字
	 * @return
	 */
	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	

}
