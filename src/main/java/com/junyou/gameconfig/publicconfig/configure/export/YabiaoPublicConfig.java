package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * 押镖配置
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-13 下午5:51:55
 */
public class YabiaoPublicConfig extends AdapterPublicConfig{
	private int maxYbTime;//每日最大押镖次数
	private int maxJbTime;//每日最大劫镖次数
	private int maxTime;//镖车押镖最大时间(s)
	private int maxCell;//离镖车最大的格子数
	private int needLevel;//押镖需要的等级
	private float jbExp;//劫镖获得经验比例
	private int needMoney;//传送至镖车所需铜钱
	private String needId;//押镖刷新道具大类id
	private int defaultBiaoCheId;//默认镖车编号
	private int needgold;//直接护送消耗元宝
	private int needgold1;//没有道具时候，用元宝刷新
	private int needbgold;//没有道具时候，用绑元宝刷新
	private int maxcishu;//刷新刷到橙色的最大次数
	private int shoeId;  //飞鞋Id
	private String  fhnpc; //交镖返回地点

	private int charnewspeed; //接镖后人物的移动速度修改
	
	public String getFhnpc() {
		return fhnpc;
	}
	public void setFhnpc(String fhnpc) {
		this.fhnpc = fhnpc;
	}

	public int getShoeId() {
		return shoeId;
	}
	public void setShoeId(int shoeId) {
		this.shoeId = shoeId;
	}
	
	/**
	 * 获取传送至镖车所需铜钱
	 * @return
	 */
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}
	
	/**
	 * 获取劫镖获得经验比例
	 * @return
	 */
	public float getJbExp() {
		return jbExp;
	}
	public void setJbExp(float jbExp) {
		this.jbExp = jbExp;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	/**
	 * 获取离镖车最大的格子数
	 * @return
	 */
	public int getMaxCell() {
		return maxCell;
	}
	public void setMaxCell(int maxCell) {
		this.maxCell = maxCell;
	}
	public int getMaxYbTime() {
		return maxYbTime;
	}
	public void setMaxYbTime(int maxYbTime) {
		this.maxYbTime = maxYbTime;
	}
	/**
	 * 获取劫镖次数
	 * @return
	 */
	public int getMaxJbTime() {
		return maxJbTime;
	}
	public void setMaxJbTime(int maxJbTime) {
		this.maxJbTime = maxJbTime;
	}
	public int getMaxTime() {
		return maxTime * 1000;
	}
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	public String getNeedId() {
		return needId;
	}
	public void setNeedId(String needId) {
		this.needId = needId;
	}
	public int getDefaultBiaoCheId() {
		return defaultBiaoCheId;
	}
	public void setDefaultBiaoCheId(int defaultBiaoCheId) {
		this.defaultBiaoCheId = defaultBiaoCheId;
	}
	public int getNeedgold() {
		return needgold;
	}
	public void setNeedgold(int needgold) {
		this.needgold = needgold;
	}
	public int getNeedgold1() {
		return needgold1;
	}
	public void setNeedgold1(int needgold1) {
		this.needgold1 = needgold1;
	}
	public int getNeedbgold() {
		return needbgold;
	}
	public void setNeedbgold(int needbgold) {
		this.needbgold = needbgold;
	}
	public int getMaxcishu() {
		return maxcishu;
	}
	public void setMaxcishu(int maxcishu) {
		this.maxcishu = maxcishu;
	}

	public int getCharnewspeed() {
		return charnewspeed;
	}

	public void setCharnewspeed(int charnewspeed) {
		this.charnewspeed = charnewspeed;
	}
}
