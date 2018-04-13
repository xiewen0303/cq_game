package com.junyou.bus.xiulianzhilu.configure.export;

import com.kernel.data.dao.AbsVersion;

public class XiuLianRenWuConfig extends AbsVersion {
	
	
	private Integer id;
	private Integer day;
	private Integer missionid;
	private Integer missiontype;
	private Integer timeType;
	private Integer data1;
	private String data2;
	private Integer jifen;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getMissionid() {
		return missionid;
	}
	public void setMissionid(Integer missionid) {
		this.missionid = missionid;
	}
	public Integer getMissiontype() {
		return missiontype;
	}
	public void setMissiontype(Integer missiontype) {
		this.missiontype = missiontype;
	}
	public Integer getTimeType() {
		return timeType;
	}
	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}
	public Integer getData1() {
		return data1;
	}
	public void setData1(Integer data1) {
		this.data1 = data1;
	}
	public String getData2() {
		return data2;
	}
	public void setData2(String data2) {
		this.data2 = data2;
	}
	public Integer getJifen() {
		return jifen;
	}
	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}
	
}
