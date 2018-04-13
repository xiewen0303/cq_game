package com.junyou.gameconfig.goods.configure.export;

import java.util.List;

import com.junyou.constants.GameConstants;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 定时活动表
 * @author LiuYu
 * @date 2015-4-22 下午7:43:30
 */
public class DingShiActiveConfig {
	private int id;//活动id
	private String name;//活动名称
	private int type;//活动类型
	private String data1;
	private String data2;
	private String data3;
	private int mapId;//活动寻路地图id
	private int x;//活动寻路地图坐标x
	private int y;//活动寻路地图坐标y
	private int minLevel;//参与活动最低等级
	private boolean tongji;//是否开启活动统计
	private boolean open;//活动是否开放
	private int openDay;//活动在开服几天后开启
	private List<Integer> weeks;//每周几开启活动
	private List<Integer> kfWeeks;//开服一周内每周几开启活动
	private List<Integer> hfWeeks;//合服一周内每周几开启活动
	private String startTime;//活动开始时间字符串
	private int[] startTime1;//活动开始时间:时,分
	private String endTime;//活动结束时间字符串
	private int[] endTime1;//活动结束时间:时,分
	private float expBl;//活动期间经验倍率
	private boolean isdrop;//活动期间是否掉落物品
	private List<Integer> maps;//活动关联地图
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getData1() {
		return data1;
	}
	public void setData1(String data1) {
		this.data1 = data1;
	}
	public String getData2() {
		return data2;
	}
	public void setData2(String data2) {
		this.data2 = data2;
	}
	public String getData3() {
		return data3;
	}
	public void setData3(String data3) {
		this.data3 = data3;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getMinLevel() {
		return minLevel;
	}
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	public boolean isTongji() {
		return tongji;
	}
	public void setTongji(boolean tongji) {
		this.tongji = tongji;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public int getOpenDay() {
		return openDay;
	}
	public void setOpenDay(int openDay) {
		this.openDay = openDay;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public int[] getStartTime1() {
		return startTime1;
	}
	public void setStartTime1(int[] startTime1) {
		this.startTime1 = startTime1;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int[] getEndTime1() {
		return endTime1;
	}
	public void setEndTime1(int[] endTime1) {
		this.endTime1 = endTime1;
	}
	public float getExpBl() {
		return expBl;
	}
	public void setExpBl(float expBl) {
		this.expBl = expBl;
	}
	public boolean isIsdrop() {
		return isdrop;
	}
	public void setIsdrop(boolean isdrop) {
		this.isdrop = isdrop;
	}
	public List<Integer> getMaps() {
		return maps;
	}
	public void setMaps(List<Integer> maps) {
		this.maps = new ReadOnlyList<>(maps);
	}
	public void setWeeks(List<Integer> weeks) {
		this.weeks = new ReadOnlyList<>(weeks);
	}
	public void setKfWeeks(List<Integer> kfWeeks) {
		this.kfWeeks = new ReadOnlyList<>(kfWeeks);
	}
	public void setHfWeeks(List<Integer> hfWeeks) {
		this.hfWeeks = new ReadOnlyList<>(hfWeeks);
	}
	/**
	 * 根据周类型获取星期列表
	 * @param type
	 * @return
	 */
	public List<Integer> getWeeksByType(byte type){
		if(type == GameConstants.DINGSHI_WEEK_TYPE_NOMAL){
			return weeks;
		}else if(type == GameConstants.DINGSHI_WEEK_TYPE_KF){
			return kfWeeks;
		}else{
			return hfWeeks;
		}
	}
	/**
	 * 获取活动结束时间差
	 * @return
	 */
	public Long getCalcEndSecondTime(){
		
		if(startTime1 == null || endTime1 == null){
			return null;
		}
		
		Long currentTime = GameSystemTime.getSystemMillTime();
		
		Long startLong = DatetimeUtil.getCalcTimeKuang(0, startTime1[0], startTime1[1]) - GameConstants.SPRING_DINGSHI_ERRER_TIME;
		Long endLong = DatetimeUtil.getCalcTimeKuang(0, endTime1[0], endTime1[1]);
		if(currentTime < endLong && startLong <= currentTime){
			return endLong - currentTime;
		}
		
		return null;
	}
	
	/**
	 * 获取活动开始时间到现在时间的时间戳
	 * @return 0 则不在活动时间内
	 */
	public long getCalcStartSecondTime(){
		
		int[] star = getStartTime1();
		int[] end = getEndTime1();
		
		if(star == null || end == null){
			return 0;
		}
		
		Long currentTime = GameSystemTime.getSystemMillTime();
		
		Long startLong = DatetimeUtil.getCalcTimeKuang(0, star[0], star[1]);
		Long endLong = DatetimeUtil.getCalcTimeKuang(0, end[0], end[1]);
		if(currentTime < endLong && startLong <= currentTime){
			return currentTime - startLong;
		}
		
		return 0;
	}
	
}
