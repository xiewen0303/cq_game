package com.junyou.stage.hundun.configure.export;


/**
 * @description 混沌战场规则表 
 *
 * @author LiNing
 * @date 2015-07-25 13:43:23
 */
public class HunDunWarGuiZeBiaoConfig {

	private Integer id;
	private Integer minneed;
	private int time;
	private long second;
	private String pujiang;
	private Integer mapId;
	private Integer data1;
	private int number;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMinneed() {
		return minneed;
	}

	public void setMinneed(Integer minneed) {
		this.minneed = minneed;
	}
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	public long getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second * 1000;
	}


	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	public Integer getData1() {
		return data1;
	}

	public void setData1(Integer data1) {
		this.data1 = data1;
	}
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPujiang() {
		return pujiang;
	}

	public void setPujiang(String pujiang) {
		this.pujiang = pujiang;
	}
}
