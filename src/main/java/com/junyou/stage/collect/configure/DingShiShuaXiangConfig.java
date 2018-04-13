package com.junyou.stage.collect.configure;

import java.util.List;

/** 定时刷箱配置 */
public class DingShiShuaXiangConfig {
	/**资源ID*/	
	private int id1; 
	/** 活动名称 */
	private String name;
	/**排序*/
	private int id;
	/** 活动类型 */
	private int type;
	/** 是否多线刷箱 */
	private int line;
	/**采集奖励*/ 
	private String jiangItem;
	
/*	*//** 所属活动ID *//*
	private int huodong;*/
	
/*	*//** 宝箱开始刷新时间 *//*
	private int[] time1;
	*//** 回收时间 *//*
	private int[] time2;*/ 
	
	
	/** 每次刷新宝箱显示公告 */
	private int gonggao;
	/** 刷新间隔 (MS)*/
	private long jiange;
	/** 宝箱采集时长 (MS)*/
	private long time3;
	/** 刷新宝箱数量 */
	private int count;
	/** 刷箱地图 */
	private int map;
	/** 广播寻路坐标 */
	private Integer[] xunlu;
	/** 刷新坐标集 */
	private List<Integer[]> zuobiaoList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

/*	public int getHuodong() {
		return huodong;
	}

	public void setHuodong(int huodong) {
		this.huodong = huodong;
	}*/



/*	public int[] getTime1() {
		return time1;
	}

	public void setTime1(int[] time1) {
		this.time1 = time1;
	}

	public int[] getTime2() {
		return time2;
	}

	public void setTime2(int[] time2) {
		this.time2 = time2;
	}*/

	public int getGonggao() {
		return gonggao;
	}

	public void setGonggao(int gonggao) {
		this.gonggao = gonggao;
	}

	public long getJiange() {
		return jiange;
	}

	public void setJiange(long jiange) {
		this.jiange = jiange;
	}

	public long getTime3() {
		return time3;
	}

	public void setTime3(long time3) {
		this.time3 = time3;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public Integer[] getXunlu() {
		return xunlu;
	}

	public void setXunlu(Integer[] xunlu) {
		this.xunlu = xunlu;
	}

	public List<Integer[]> getZuobiaoList() {
		return zuobiaoList;
	}

	public void setZuobiaoList(List<Integer[]> zuobiaoList) {
		this.zuobiaoList = zuobiaoList;
	}

	public String getJiangItem() {
		return jiangItem;
	}

	public void setJiangItem(String jiangItem) {
		this.jiangItem = jiangItem;
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}



}
