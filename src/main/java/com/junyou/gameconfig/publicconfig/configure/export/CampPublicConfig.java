package com.junyou.gameconfig.publicconfig.configure.export;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 阵营战
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-10 下午12:16:42
 */
public class CampPublicConfig extends AdapterPublicConfig{
	private int time;//阵营战场每秒获得经验
	private int[] camp;//阵营
	private int jifen1;// 击杀单个玩家获得积分
	private int jifen2;// 单次攻击神像获得积分
	private int jifen3;// 摧毁神像获得积分
	private int harm;//固定伤害值（每次攻击雕像伤害为1点）
	private int paiming = 10;//活动结束后的总排名人数（前10名）
	private ReadOnlyMap<Integer, int[]> points;//阵营所对应的坐标
	/**
	 * 获取阵营所对应的坐标
	 * @return
	 */
	public int[] getPoints(Integer camp) {
		if(points == null || points.size() <= 0){
			return null;
		}
		return points.get(camp);
	}
	
	public ReadOnlyMap<Integer, int[]> getPoint(){
		return points;
	}

	public void setPoints(ReadOnlyMap<Integer, int[]> points) {
		this.points = points;
	}

	/**
	 * 获取击杀雕像的固定伤害值
	 * @return
	 */
	public int getHarm() {
		return harm;
	}

	public void setHarm(int harm) {
		this.harm = harm;
	}

	/**
	 * 获取活动结束后的总排名人数
	 * @return
	 */
	public int getPaiming() {
		return paiming;
	}

	public void setPaiming(int paiming) {
		if(paiming < this.paiming){
			this.paiming = paiming;
		}
	}

	/**
	 * 获取击杀单个玩家获得积分
	 * @return
	 */
	public int getJifen1() {
		return jifen1;
	}

	public void setJifen1(int jifen1) {
		this.jifen1 = jifen1;
	}

	/**
	 * 单次攻击神像获得积分
	 * @return
	 */
	public int getJifen2() {
		return jifen2;
	}

	public void setJifen2(int jifen2) {
		this.jifen2 = jifen2;
	}

	/**
	 * 摧毁神像获得积分
	 * @return
	 */
	public int getJifen3() {
		return jifen3;
	}

	public void setJifen3(int jifen3) {
		this.jifen3 = jifen3;
	}

	/**
	 * 获取阵营每秒获得经验(单位：秒）
	 * @return
	 */
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * 获取所有阵营
	 * @return
	 */
	public int[] getCamp() {
		return camp;
	}

	public void setCamp(int[] camp) {
		this.camp = camp;
	}
}