package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.ArrayList;
import java.util.List;

public class KuafuLuanDouPublicConfig extends AdapterPublicConfig {
	private int stoptime;//活动结束自动退出时间（秒）
	private int maxpople;//每个跨服小组赛承载人数上限
	private String fhbuff;//复活无敌buff，填写效果id
	private int fuhuotime;// 复活时间（秒）
	private int cftime;//退出活动的惩罚时间(秒)
	private int killjf;//击杀获得积分
	private List<int[]> zuobiao1 = new ArrayList<int[]>();// 进入及复活坐标，随机取1个

	
	public int getStoptime() {
		return stoptime;
	}

	public void setStoptime(int stoptime) {
		this.stoptime = stoptime;
	}

	public String getFhbuff() {
		return fhbuff;
	}

	public void setFhbuff(String fhbuff) {
		this.fhbuff = fhbuff;
	}

	public int getCftime() {
		return cftime;
	}

	public void setCftime(int cftime) {
		this.cftime = cftime;
	}

	public int getKilljf() {
		return killjf;
	}

	public void setKilljf(int killjf) {
		this.killjf = killjf;
	}

	public int getMaxpople() {
		return maxpople;
	}

	public void setMaxpople(int maxpople) {
		this.maxpople = maxpople;
	}

	public List<int[]> getZuobiao1() {
		return zuobiao1;
	}

	public void setZuobiao1(List<int[]> zuobiao1) {
		this.zuobiao1 = zuobiao1;
	}

	public int getFuhuotime() {
		return fuhuotime;
	}

	public void setFuhuotime(int fuhuotime) {
		this.fuhuotime = fuhuotime;
	}
	
	public void addZuobiao1(int[] zuobiao){
		zuobiao1.add(zuobiao);
	}

}
