package com.junyou.gameconfig.export;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;
import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 全局经验表配置 
 *
 * @author DaoZhen Yuan
 * @date 2013-08-08 20:38:59
 */
public class ExpConfig extends AbsVersion{

	private int level;
	private Long needexp;//升级需要经验
	private int zlValue;
	private ReadOnlyMap<String, Long> attribute;
	
	public int getZlValue() {
		return zlValue;
	}

	public void setZlValue(int zlValue) {
		this.zlValue = zlValue;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	public Long getNeedexp() {
		return needexp;
	}

	public void setNeedexp(Long needexp) {
		this.needexp = needexp;
	}
	public Map<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
}
