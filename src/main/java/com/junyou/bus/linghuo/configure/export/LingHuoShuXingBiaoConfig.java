package com.junyou.bus.linghuo.configure.export;

import java.util.Map;

/**
 * 
 * @description 灵火属性表 
 *
 * @author ZHONGDIAN
 * @date 2015-07-04 18:55:34
 */
public class LingHuoShuXingBiaoConfig {


	private Integer id;
	
	private Integer lv;//等级
	
	private Map<String,Long> baseAttrs;// 当前等级对应的基本单一属性集合

	private Map<String,Long> attrs;// 此等级对应的所有加成属性集合

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public Map<String, Long> getBaseAttrs() {
        return baseAttrs;
    }

    public void setBaseAttrs(Map<String, Long> baseAttrs) {
        this.baseAttrs = baseAttrs;
    }

}
