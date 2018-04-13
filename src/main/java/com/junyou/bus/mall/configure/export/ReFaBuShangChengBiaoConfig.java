package com.junyou.bus.mall.configure.export;


/**
 * 
 * @description 商城 
 *
 * @author HanChun
 * @date 2015-03-17 14:29:30
 */
public class ReFaBuShangChengBiaoConfig {

	private Integer id;

	private Integer maxlevel;

	private Integer price;

	private Integer order;

	private String sellid;

	private Integer modelid;

	private Integer showprice;

	private Integer day;

	private Integer moneytype;

	private Integer minlevel;
	
	private String superScript;
	
	public String getSuperScript() {
		return superScript;
	}
	public void setSuperScript(String superScript) {
		this.superScript = superScript;
	}
	
	private Integer count;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMaxlevel() {
		return maxlevel;
	}

	public void setMaxlevel(Integer maxlevel) {
		this.maxlevel = maxlevel;
	}
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getSellid() {
		return sellid;
	}

	public void setSellid(String sellid) {
		this.sellid = sellid;
	}
	public Integer getModelid() {
		return modelid;
	}

	public void setModelid(Integer modelid) {
		this.modelid = modelid;
	}
	public Integer getShowprice() {
		return showprice;
	}

	public void setShowprice(Integer showprice) {
		this.showprice = showprice;
	}
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getMoneytype() {
		return moneytype;
	}

	public void setMoneytype(Integer moneytype) {
		this.moneytype = moneytype;
	}
	public Integer getMinlevel() {
		return minlevel;
	}

	public void setMinlevel(Integer minlevel) {
		this.minlevel = minlevel;
	}

	public ReFaBuShangChengBiaoConfig copy(){
		return null;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}


}
