package com.junyou.bus.activityboss.manage;

import java.io.Serializable;
 
/**
 * 击杀boss信息
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-5-4 下午9:08:17
 */
public class BossKillInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3021832719108215637L;
	
	private int line;  //线
	private int state; //(1,被击杀	2:被回收	0:没有被击杀)
	private String roleName;//角色名称
	private long endTime; //击杀时间
	
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
