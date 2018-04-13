package com.junyou.bus.tianyu.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 天羽潜能丹
 *
 * @author wind
 * @date 2015-03-31 19:07:18
 */
public class TianYuQianNengBiaoConfig extends AbsVersion{
	
	private Map<String, Long> attrs;

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	

}
