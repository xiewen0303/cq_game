package com.junyou.bus.boss_jifen.configure;

import java.util.Map;

/**
 * 
 * @description: boss积分配置 
 *
 *	@author ChuBin
 *
 * @date 2017-1-3
 */
public class RoleBossJifenConfig {
	private Integer id;
	private Integer ceng;
	private Integer star;
	private Integer pointX;
	private Integer pointY;
	private Long consumeJifen;
	private Map<String, Long> attribute;
	private Map<String, Long> totalAttrs;
	
	public Map<String, Long> getTotalAttrs() {
		return totalAttrs;
	}

	public void setTotalAttrs(Map<String, Long> totalAttrs) {
		this.totalAttrs = totalAttrs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCeng() {
		return ceng;
	}

	public void setCeng(Integer ceng) {
		this.ceng = ceng;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Integer getPointX() {
		return pointX;
	}

	public void setPointX(Integer pointX) {
		this.pointX = pointX;
	}

	public Integer getPointY() {
		return pointY;
	}

	public void setPointY(Integer pointY) {
		this.pointY = pointY;
	}
	
	public Long getConsumeJifen() {
		return consumeJifen;
	}

	public void setConsumeJifen(Long consumeJifen) {
		this.consumeJifen = consumeJifen;
	}

	public Map<String, Long> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = attribute;
	}
}
