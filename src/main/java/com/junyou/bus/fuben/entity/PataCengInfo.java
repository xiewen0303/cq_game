package com.junyou.bus.fuben.entity;
/**
 * @author LiuYu
 * 2015-6-10 下午8:54:19
 */
public class PataCengInfo {
	private int cengId;
	private String lastRole;//最短时间通关人
	private int lastTime;//通关时间
	public int getCengId() {
		return cengId;
	}
	public void setCengId(int cengId) {
		this.cengId = cengId;
	}
	public String getLastRole() {
		return lastRole;
	}
	public void setLastRole(String lastRole) {
		this.lastRole = lastRole;
	}
	public int getLastTime() {
		return lastTime;
	}
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	
}
