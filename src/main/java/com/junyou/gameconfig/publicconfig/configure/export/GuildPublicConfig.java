package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.ArrayList;
import java.util.List;


/**
 * PK相关配置
 *@author  LiNing
 *@created 2013-10-24上午6:22:26
 */
public class GuildPublicConfig extends AdapterPublicConfig{
	//创建门派所需VIP等级
	private int vipLevel;
	//创建/加入门派所需等级
	private int level;
	//开服几天后可修改公告
	private int day;
	/**银两捐献基数*/
	private int moneyRate;
	/**银两捐献帮贡*/
	private int moneyGong;
	/**帮派获得资金*/
	private int moneyGuildGet;
	/**道具捐献获得帮贡*/
	private int itemGong;
	/**绑元捐献获得帮贡*/
	private int bGoldGong;
	/**绑元捐献获得资金*/
	private int bGoldGuildGet;
	/**元宝捐献获得帮贡*/
	private int goldGong;
	/**元宝捐献获得资金*/
	private int goldGuildGet;
	/**弹劾道具*/
	private String impeachItemId;
	/**掌门连续未登录天数*/
	private int leaderNoLoginDay;
	/**成功挑战炼狱BOSS属性减少百分比**/
	private int lianyuBossAttrReduce;
	/** 炼狱BOSS刷新的坐标点**/
	private List<Integer[]> lianyuBossBirth = new ArrayList<>();
	/** 炼狱BOSS开启等级**/
	private int  lianyuBossOpen;
	/** 炼狱BOSS地图id**/
	private int  lianyuBossMapId;
	public int getLianyuBossMapId() {
		return lianyuBossMapId;
	}
	public void setLianyuBossMapId(int lianyuBossMapId) {
		this.lianyuBossMapId = lianyuBossMapId;
	}
	public List<Integer[]> getLianyuBossBirth() {
		return lianyuBossBirth;
	}
	public void setLianyuBossBirth(Integer[] position) {
		this.lianyuBossBirth.add(position);
	}
	 
	public int getLianyuBossOpen() {
		return lianyuBossOpen;
	}
	
	public void setLianyuBossOpen(int lianyuBossOpen) {
		this.lianyuBossOpen = lianyuBossOpen;
	}
	
	public int getLianyuBossAttrReduce() {
		return lianyuBossAttrReduce;
	}
	public void setLianyuBossAttrReduce(int lianyuBossAttrReduce) {
		this.lianyuBossAttrReduce = lianyuBossAttrReduce;
	}
	public String getImpeachItemId() {
		return impeachItemId;
	}
	public void setImpeachItemId(String impeachItemId) {
		this.impeachItemId = impeachItemId;
	}
	public int getLeaderNoLoginDay() {
		return leaderNoLoginDay;
	}
	public void setLeaderNoLoginDay(int leaderNoLoginDay) {
		this.leaderNoLoginDay = leaderNoLoginDay;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMoneyRate() {
		return moneyRate;
	}
	public void setMoneyRate(int moneyRate) {
		this.moneyRate = moneyRate;
	}
	public int getMoneyGong() {
		return moneyGong;
	}
	public void setMoneyGong(int moneyGong) {
		this.moneyGong = moneyGong;
	}
	public int getMoneyGuildGet() {
		return moneyGuildGet;
	}
	public void setMoneyGuildGet(int moneyGuildGet) {
		this.moneyGuildGet = moneyGuildGet;
	}
	public int getItemGong() {
		return itemGong;
	}
	public void setItemGong(int itemGong) {
		this.itemGong = itemGong;
	}
	public int getbGoldGong() {
		return bGoldGong;
	}
	public void setbGoldGong(int bGoldGong) {
		this.bGoldGong = bGoldGong;
	}
	public int getbGoldGuildGet() {
		return bGoldGuildGet;
	}
	public void setbGoldGuildGet(int bGoldGuildGet) {
		this.bGoldGuildGet = bGoldGuildGet;
	}
	public int getGoldGong() {
		return goldGong;
	}
	public void setGoldGong(int goldGong) {
		this.goldGong = goldGong;
	}
	public int getGoldGuildGet() {
		return goldGuildGet;
	}
	public void setGoldGuildGet(int goldGuildGet) {
		this.goldGuildGet = goldGuildGet;
	}
	
	
}
