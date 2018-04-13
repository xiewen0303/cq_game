package com.junyou.bus.xianjian.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 仙剑升阶表 
 *
 * @author wind
 * @date 2015-03-31 19:07:18
 */
public class XianJianQianNengBiaoConfig extends AbsVersion{
	
	private Map<String, Long> attrs;

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	

}
