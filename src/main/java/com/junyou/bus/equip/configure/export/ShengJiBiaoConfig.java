package com.junyou.bus.equip.configure.export;


/**
 * 
 * @description 装备升阶表 
 *
 * @author ZHONGDIAN
 * @date 2015-06-01 17:26:39
 */
public class ShengJiBiaoConfig {

	private Integer successrate;

	private Integer num;

	private Integer zblevel;

	private String prop;

	private Integer money;

	private Integer mincs;

	private Integer gold;
	
	private Integer bgold;
	

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getBgold() {
		return bgold;
	}

	public void setBgold(Integer bgold) {
		this.bgold = bgold;
	}

	public Integer getSuccessrate() {
		return successrate;
	}

	public void setSuccessrate(Integer successrate) {
		this.successrate = successrate;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getZblevel() {
		return zblevel;
	}

	public void setZblevel(Integer zblevel) {
		this.zblevel = zblevel;
	}
	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}
	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}
	public Integer getMincs() {
		return mincs;
	}

	public void setMincs(Integer mincs) {
		this.mincs = mincs;
	}

	public ShengJiBiaoConfig copy(){
		return null;
	}


}
