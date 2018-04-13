package com.junyou.bus.equip.configure.export;

import java.util.Map;

/**
 * 
 * @description:套装象位特殊阶段属性配置 
 *
 *	@author ChuBin
 *
 * @date 2016-12-12
 */
public class TaoZhuangXiangWeiJieDuanConfig {
	
	private int buWeiId;//装备部位Id
	private int jd;//象位阶段
	private int star;//星数
	private Map<String,Long> attrs;//属性
	
	public int getBuWeiId() {
		return buWeiId;
	}
	public void setBuWeiId(int buWeiId) {
		this.buWeiId = buWeiId;
	}
	public int getJd() {
		return jd;
	}
	public void setJd(int jd) {
		this.jd = jd;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
}
