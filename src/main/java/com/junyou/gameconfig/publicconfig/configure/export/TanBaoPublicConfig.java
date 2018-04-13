package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * 探宝相关
 * @author LiuYu
 * @date 2015-6-20 下午5:22:24
 */
public class TanBaoPublicConfig extends AdapterPublicConfig{
	private int addExpTime;
	private int fuhuoTime;
	private String emailCode;
	private int minJf;
	private int maxJf;
	private float drop;
	/**
	 * 加经验时间
	 * @return
	 */
	public int getAddExpTime() {
		return addExpTime;
	}
	public void setAddExpTime(int addExpTime) {
		this.addExpTime = addExpTime * 1000;
	}
	/**
	 * 复活时间
	 * @return
	 */
	public int getFuhuoTime() {
		return fuhuoTime;
	}
	public void setFuhuoTime(int fuhuoTime) {
		this.fuhuoTime = fuhuoTime * 1000;
	}
	public String getEmailCode() {
		return emailCode;
	}
	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
	public int getMinJf() {
		return minJf;
	}
	public void setMinJf(int minJf) {
		this.minJf = minJf;
	}
	public int getMaxJf() {
		return maxJf;
	}
	public void setMaxJf(int maxJf) {
		this.maxJf = maxJf;
	}
	public float getDrop() {
		return drop;
	}
	public void setDrop(float drop) {
		this.drop = drop;
	}
	
	
}
