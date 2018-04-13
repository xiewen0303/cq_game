package com.junyou.bus.chengjiu.configure.export;

import java.util.Map;

/**
 * 
 * 成就配置表
 */
public class ChengJiuPeiZhiConfig {

	private Integer number;// 成就条件数值

	private String chenghao;// 成就达到激活称号（为空不激活）

	private Integer type;// 成就类型

	private Integer cjvalue;// 成就达成获得的成就点数

	private Integer id;

	private Map<String, Integer> items;//获取的奖励

	private Map<String, Long> attrs;// 基础属性

	private Integer pageType;// 成就标签页类型

	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getChenghao() {
		return chenghao;
	}

	public void setChenghao(String chenghao) {
		this.chenghao = chenghao;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCjvalue() {
		return cjvalue;
	}

	public void setCjvalue(Integer cjvalue) {
		this.cjvalue = cjvalue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<String, Integer> getItems() {
		return items;
	}

	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public ChengJiuPeiZhiConfig copy() {
		return null;
	}
}