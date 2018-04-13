package com.junyou.bus.equip.configure.export;


/**
 * 
 * @description 玄铁兑换表 
 *
 * @author ZHONGDIAN
 * @date 2015-06-01 10:18:23
 */
public class XuanTieDuiHuanConfig{

	private Integer maxcount;

	private Integer needlevel;

	private Integer order;

	private Integer needjob;

	private Integer needxt;

	private String itemid;


	public Integer getMaxcount() {
		return maxcount;
	}

	public void setMaxcount(Integer maxcount) {
		this.maxcount = maxcount;
	}
	public Integer getNeedlevel() {
		return needlevel;
	}

	public void setNeedlevel(Integer needlevel) {
		this.needlevel = needlevel;
	}
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	public Integer getNeedjob() {
		return needjob;
	}

	public void setNeedjob(Integer needjob) {
		this.needjob = needjob;
	}
	public Integer getNeedxt() {
		return needxt;
	}

	public void setNeedxt(Integer needxt) {
		this.needxt = needxt;
	}
	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public XuanTieDuiHuanConfig copy(){
		return null;
	}


}
