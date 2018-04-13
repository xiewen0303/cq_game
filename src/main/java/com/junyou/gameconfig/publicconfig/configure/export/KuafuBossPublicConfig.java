package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class KuafuBossPublicConfig extends AdapterPublicConfig {
	private int open;// 跨服boss开启等级
	private String bossid;// 怪物id
	private int mapid;// 跨服boss活动地图id
	private int[] zuobiao;// boss刷新坐标
	private Map<String, Integer> lastattack;// 最后一击奖励（道具id）
	private int bossdeathgg;// boss击杀公告code码
	private int exptime;// 每次获得经验时间（秒） 10
	private long jiangexp;// 次获得的经验值 100000
	private int maxpople;// 每个跨服场景承载人数上限
	private List<int[]> zuobiao1 = new ArrayList<int[]>();// 进入及复活坐标，随机取1个
	private int fuhuotime;// 复活时间（秒）
	private int attacktime;//XX时间内击杀boss的下次成长（分）

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public String getBossid() {
		return bossid;
	}

	public void setBossid(String bossid) {
		this.bossid = bossid;
	}

	public int getMapid() {
		return mapid;
	}

	public void setMapid(int mapid) {
		this.mapid = mapid;
	}

	public int[] getZuobiao() {
		return zuobiao;
	}

	public void setZuobiao(int[] zuobiao) {
		this.zuobiao = zuobiao;
	}

	public Map<String, Integer> getLastattack() {
		return lastattack;
	}

	public void setLastattack(Map<String, Integer> lastattack) {
		this.lastattack = lastattack;
	}

	public int getBossdeathgg() {
		return bossdeathgg;
	}

	public void setBossdeathgg(int bossdeathgg) {
		this.bossdeathgg = bossdeathgg;
	}

	public int getExptime() {
		return exptime;
	}

	public void setExptime(int exptime) {
		this.exptime = exptime;
	}

	public long getJiangexp() {
		return jiangexp;
	}

	public void setJiangexp(long jiangexp) {
		this.jiangexp = jiangexp;
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

	public int getAttacktime() {
		return attacktime;
	}

	public void setAttacktime(int attacktime) {
		this.attacktime = attacktime;
	}

}
