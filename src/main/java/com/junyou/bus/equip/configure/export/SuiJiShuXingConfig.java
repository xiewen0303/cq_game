package com.junyou.bus.equip.configure.export;

import java.util.Map;

/**
 * 
 * @description 随机属性表 
 *
 * @author wind
 * @date 2015-01-10 15:09:16
 */
public class SuiJiShuXingConfig {
	
	
	private Float odds; 

	private Integer id; 

	private Integer fenzu;
	
	private Map<String,Long> attrs;
	
	private Integer level;
	
	private Integer nextId;
	
	private Integer tpId;

	public Float getOdds() {
		return odds;
	}

	public void setOdds(Float odds) {
		this.odds = odds;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFenzu() {
		return fenzu;
	}

	public void setFenzu(Integer fenzu) {
		this.fenzu = fenzu;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getNextId() {
		return nextId;
	}

	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}

	public Integer getTpId() {
		return tpId;
	}

	public void setTpId(Integer tpId) {
		this.tpId = tpId;
	}
	
	
}
