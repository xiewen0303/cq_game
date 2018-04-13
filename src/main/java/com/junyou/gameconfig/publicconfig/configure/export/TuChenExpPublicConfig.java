package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.Map;


/**
 * 土城高经验配置
 *@author  LiNing
 *@created 2013-10-24上午6:22:26
 */
public class TuChenExpPublicConfig extends AdapterPublicConfig{
	
	/**
	 * 是否开放
	 */
	private boolean isOpen;
	
	private String mapId;
	
	private int openDay;
	
	private int addExpTime;
	
	private int sumExpXs;
	
	private int wXs;
	
	private float hXs;
	
	private int minLevel;
	
	private Map<String,String> points;
	
	private int startHour;
	private int startMinute;
	
	private int endHour;
	private int endMinute;
	
	
	
	/**
	 * 是否开放
	 * @return
	 */
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getMapId() {
		return mapId;
	}

	public int getOpenDay() {
		return openDay;
	}

	public int getAddExpTime() {
		return addExpTime;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public void setOpenDay(int openDay) {
		this.openDay = openDay;
	}

	public void setAddExpTime(int addExpTime) {
		this.addExpTime = addExpTime;
	}

	public Map<String, String> getPoints() {
		return points;
	}

	public void setPoints(Map<String, String> points) {
		this.points = points;
	}

	
	public float gethXs() {
		return hXs;
	}

	public void sethXs(float hXs) {
		this.hXs = hXs;
	}

	public int getSumExpXs() {
		return sumExpXs;
	}

	public int getwXs() {
		return wXs;
	}

	public int getStartHour() {
		return startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public int getEndHour() {
		return endHour;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public void setSumExpXs(int sumExpXs) {
		this.sumExpXs = sumExpXs;
	}

	public void setwXs(int wXs) {
		this.wXs = wXs;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	
	
	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	/**
	 * 是否包含指定坐标
	 * @param x
	 * @param y
	 * @return true:包含
	 */
	public boolean isContainsPoint(int x,int y){
		String tmp = x + "-" + y;
		return points.containsKey(tmp);
	}
	
	/**
	 * 在时间内
	 * @return true:在时间内
	 */
	public boolean isInTime(){
		if(startHour == 0 && endHour == 0){
			return false;
		}
		
//		Long c = GameSystemTime.getSystemMillTime();
//		Long s = DateUtils.getCalcTimeByValue(startHour, startMinute, 0);
//		Long e = DateUtils.getCalcTimeByValue(endHour, endMinute, 0);
//		
//		return  c >= s && c <= e;
		
		return false;
	}
	
}
