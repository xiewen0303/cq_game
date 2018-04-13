package com.junyou.bus.wuxing.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 五行配置表 
 *
 * @author ZHONGDIAN
 * @date 2016-04-11 15:28:13
 */
public class HuanShiMoShenJiChuBiaoConfig extends AbsVersion  {

	private Integer id;
	private Integer type;
	private Integer level;
	private String item;
	private Integer count;
	private Integer money;
	private Integer gold;
	private Integer bgold;
	private Integer zfzmin;
	private Integer zfzmax;
	private Integer pro;
	private Integer zfzmin3;
	private Integer zfzmin2;
	private boolean zfztime;
	private boolean ggopen;
	private float cztime;


	private Map<String,Long> attrs;
	private Map<String,Long> futiAttrs;
	
	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public Map<String, Long> getFutiAttrs() {
		return futiAttrs;
	}

	public void setFutiAttrs(Map<String, Long> futiAttrs) {
		this.futiAttrs = futiAttrs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

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

	public Integer getZfzmin() {
		return zfzmin;
	}

	public void setZfzmin(Integer zfzmin) {
		this.zfzmin = zfzmin;
	}

	public Integer getZfzmax() {
		return zfzmax;
	}

	public void setZfzmax(Integer zfzmax) {
		this.zfzmax = zfzmax;
	}

	public Integer getPro() {
		return pro;
	}

	public void setPro(Integer pro) {
		this.pro = pro;
	}

	public Integer getZfzmin3() {
		return zfzmin3;
	}

	public void setZfzmin3(Integer zfzmin3) {
		this.zfzmin3 = zfzmin3;
	}
	public Integer getZfzmin2() {
		return zfzmin2;
	}

	public void setZfzmin2(Integer zfzmin2) {
		this.zfzmin2 = zfzmin2;
	}

	public boolean isZfztime() {
		return zfztime;
	}

	public void setZfztime(boolean zfztime) {
		this.zfztime = zfztime;
	}

	public boolean isGgopen() {
		return ggopen;
	}

	public void setGgopen(boolean ggopen) {
		this.ggopen = ggopen;
	}

	public float getCztime() {
		return cztime;
	}

	public void setCztime(float cztime) {
		this.cztime = cztime;
	}
	public HuanShiMoShenJiChuBiaoConfig copy(){
		return null;
	}


}
