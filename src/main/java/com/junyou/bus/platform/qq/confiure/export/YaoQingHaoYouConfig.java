package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;


/**
 * 
 * @description 腾讯邀请好友 
 *
 * @author ZHONGDIAN
 * @date 2015-12-30 17:23:34
 */
public class YaoQingHaoYouConfig{

	private Integer id;

	private Integer fnum;

	private Integer level;

	private Map<String, Integer> item;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFnum() {
		return fnum;
	}

	public void setFnum(Integer fnum) {
		this.fnum = fnum;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Map<String, Integer> getItem() {
		return item;
	}

	public void setItem(Map<String, Integer> item) {
		this.item = item;
	}

	public YaoQingHaoYouConfig copy(){
		return null;
	}


}
