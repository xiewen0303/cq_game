package com.junyou.bus.platform.utils;

import com.junyou.utils.common.ConfigureUtil;

/**
 * 37Vplan
 * @author lxn
 *
 */
public class _37Util {
	private static final String _360TequanParam = "config/platform/37.properties";
	/**
	 *37Vplan会员参与活动的等级要求
	 */
	public static int getVipLimitLevel() {
		String vipStr  = ConfigureUtil.getProp(_360TequanParam, "vip_limit_level");
		return Integer.parseInt(vipStr);
	}
	 
 
	
	
	
}
