package com.junyou.bus.equip.configure.export;

import java.util.Map;

/**
 * 
 * @description:套装象位配置 
 *
 *	@author ChuBin
 *
 * @date 2016-12-12
 */
public class TaoZhuangXiangWeiConfig {
	private int buWeiId;// 装备部位Id
	private int jd;// 象位阶段
	private int star;// 星数
	private String consumeId;// 消耗大类
	private String mallId;// 商城大类
	private int gold; // 每个道具消耗元宝
	private int bgold; // 每个道具消耗元宝
	private int needCount;// 升阶消耗道具数量
	private Map<String, Long> attrs;// 属性
	private int needZsLevel;// 升阶需要铸神达到的等级
	
	public int getBuWeiId() {
		return buWeiId;
	}
	public void setBuWeiId(int buWeiId) {
		this.buWeiId = buWeiId;
	}
	public int getJd() {
		return jd;
	}
	public void setJd(int jd) {
		this.jd = jd;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public String getConsumeId() {
		return consumeId;
	}
	public void setConsumeId(String consumeId) {
		this.consumeId = consumeId;
	}
	public String getMallId() {
		return mallId;
	}
	public void setMallId(String mallId) {
		this.mallId = mallId;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getBgold() {
		return bgold;
	}
	public void setBgold(int bgold) {
		this.bgold = bgold;
	}
	public int getNeedCount() {
		return needCount;
	}
	public void setNeedCount(int needCount) {
		this.needCount = needCount;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	public int getNeedZsLevel() {
		return needZsLevel;
	}
	public void setNeedZsLevel(int needZsLevel) {
		this.needZsLevel = needZsLevel;
	}
}
