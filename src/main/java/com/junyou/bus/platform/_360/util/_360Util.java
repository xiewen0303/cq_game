package com.junyou.bus.platform._360.util;

import com.junyou.utils.common.ConfigureUtil;

/**
 * 360特权
 * @author lxn
 *
 */
public class _360Util {
	private static final String _360TequanParam = "config/platform/360.properties";
	/**
	 *360特权请求地址
	 */
	public static String getTequanUrl() {
		return ConfigureUtil.getProp(_360TequanParam, "tequan_url");
	}
	public static Integer getAid() {
		return ConfigureUtil.getIntProp(_360TequanParam, "aid");
	}
	public static String getType() {
		return ConfigureUtil.getProp(_360TequanParam, "type");
	}
	public static String getGkey() {
		return ConfigureUtil.getProp(_360TequanParam, "gkey");
	}
	public static String getPrivkey() {
		return ConfigureUtil.getProp(_360TequanParam, "privkey");
	}
	//上线后WEB调用允许访问的qid1
	public static String get360TestQid1() {
		return ConfigureUtil.getProp(_360TequanParam, "360_test_qid1");
	}
	//上线后WEB调用允许访问的qid2
	public static String get360TestQid2() {
		return ConfigureUtil.getProp(_360TequanParam, "360_test_qid2");
	}
	//是否限定访问的qid
	public static String get360TestQidRestrict() {
		return ConfigureUtil.getProp(_360TequanParam, "360_test_qid_restrict");
	}
	
	//*********360 V计划***********
	//gkey
	public static String get360VplanGkey() {
		return ConfigureUtil.getProp(_360TequanParam, "360_V_gkey");
	}
	//lkey
	public static String get360VplanLkey() {
		return ConfigureUtil.getProp(_360TequanParam, "360_V_lkey");
	}
	//version
	public static String get360VplangVersion() {
		return ConfigureUtil.getProp(_360TequanParam, "360_V_version");
	}
	//url
	public static String get360VplanUrl() {
		return ConfigureUtil.getProp(_360TequanParam, "360_V_url");
	}
 
	
	
	
}
