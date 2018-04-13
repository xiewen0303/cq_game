package com.junyou.bus.platform.qq.confiure.export;


/**
 * 
 * @description 腾讯折扣商店配置表 
 *
 * @author ZHONGDIAN
 * @date 2015-12-16 14:57:22
 */
public class LanZuanZheKouShopConfig {

	private Integer id;

	private Integer needgold;

	private Integer count;

	private Integer showgold;

	private String itemid;

	private Integer type;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNeedgold() {
		return needgold;
	}

	public void setNeedgold(Integer needgold) {
		this.needgold = needgold;
	}
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getShowgold() {
		return showgold;
	}

	public void setShowgold(Integer showgold) {
		this.showgold = showgold;
	}
	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	
	public LanZuanZheKouShopConfig copy(){
		return null;
	}


}
