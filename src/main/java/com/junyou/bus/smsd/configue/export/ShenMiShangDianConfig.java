package com.junyou.bus.smsd.configue.export;


/**
 * 
 * @description 热发布活动公共配置表 
 *
 * @author ZHONGDIAN
 * @date 2014-04-23 15:22:05
 */
public class ShenMiShangDianConfig {

	private Integer id;

	private Integer old;

	private Integer now;

	private String item;

	private Integer count;
	
	private Integer guangbo;

	private Integer littleid;

	private Integer moneytype;

	private Float odds;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOld() {
		return old;
	}

	public void setOld(Integer old) {
		this.old = old;
	}
	public Integer getNow() {
		return now;
	}

	public void setNow(Integer now) {
		this.now = now;
	}
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	public Integer getGuangbo() {
		return guangbo;
	}

	public void setGuangbo(Integer guangbo) {
		this.guangbo = guangbo;
	}
	public Integer getLittleid() {
		return littleid;
	}

	public void setLittleid(Integer littleid) {
		this.littleid = littleid;
	}
	public Integer getMoneytype() {
		return moneytype;
	}

	public void setMoneytype(Integer moneytype) {
		this.moneytype = moneytype;
	}
	public Float getOdds() {
		return odds;
	}

	public void setOdds(Float odds) {
		this.odds = odds;
	}

	
	public ShenMiShangDianConfig copy(){
		return null;
	}
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}



}
