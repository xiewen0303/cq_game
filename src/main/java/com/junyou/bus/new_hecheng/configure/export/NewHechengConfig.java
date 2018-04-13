package com.junyou.bus.new_hecheng.configure.export;


/**
 * 
 * @description:新合成表 
 *
 *	@author ChuBin
 *
 * @date 2016-12-26
 */
public class NewHechengConfig {
    
	private Integer id;
	private String fromItem;
	private String toItem;
	private Float clientGailv;//显示概率; 实际合成概率=合成物品数量 * 标准概率
	private Float realGaiLv;//真实概率
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFromItem() {
		return fromItem;
	}
	public void setFromItem(String fromItem) {
		this.fromItem = fromItem;
	}
	public String getToItem() {
		return toItem;
	}
	public void setToItem(String toItem) {
		this.toItem = toItem;
	}
	public Float getClientGailv() {
		return clientGailv;
	}
	public void setClientGailv(Float clientGailv) {
		this.clientGailv = clientGailv;
	}
	public Float getRealGaiLv() {
		return realGaiLv;
	}
	public void setRealGaiLv(Float realGaiLv) {
		this.realGaiLv = realGaiLv;
	}
}
