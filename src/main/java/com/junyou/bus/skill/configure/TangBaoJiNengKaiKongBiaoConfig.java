package com.junyou.bus.skill.configure;


/**
 * 
 * @description 糖宝技能格位表 
 *
 * @author ZHONGDIAN
 * @date 2016-06-28 10:24:09
 */
public class TangBaoJiNengKaiKongBiaoConfig {

	private Integer id;

	private Integer num;

	private String needitem;

	private Integer needid;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public String getNeeditem() {
		return needitem;
	}

	public void setNeeditem(String needitem) {
		this.needitem = needitem;
	}
	public Integer getNeedid() {
		return needid;
	}

	public void setNeedid(Integer needid) {
		this.needid = needid;
	}

	
	public TangBaoJiNengKaiKongBiaoConfig copy(){
		return null;
	}


}
