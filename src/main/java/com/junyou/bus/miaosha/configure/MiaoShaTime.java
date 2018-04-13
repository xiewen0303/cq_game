package com.junyou.bus.miaosha.configure;


/**
 * @author LiuYu
 * 2016-3-4 下午5:02:06
 */
public class MiaoShaTime implements Comparable<MiaoShaTime>{
	private long startTime;//今日开始时间净值
	private long endTime;//今日结束时间净值
	private int id;
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int compareTo(MiaoShaTime o) {
		if(o.getStartTime() > getStartTime()){
			return 1;
		}
		return -1;
	}
	
}
