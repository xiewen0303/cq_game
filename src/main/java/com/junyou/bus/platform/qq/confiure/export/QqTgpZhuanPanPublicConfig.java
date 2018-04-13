package com.junyou.bus.platform.qq.confiure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 * 腾讯TGP转盘配置
 * @author zhongdian
 *
 */
public class QqTgpZhuanPanPublicConfig extends AdapterPublicConfig {

	Map<Map<String, Integer>,Integer> tgpMap = new HashMap<>();
	Map<Map<String, Integer>,Integer> geziMap = new HashMap<>();

	public Map<Map<String, Integer>,Integer> getTgpMap() {
		return tgpMap;
	}

	public void setTgpMap(Map<Map<String, Integer>, Integer> tgpMap) {
		this.tgpMap = tgpMap;
	}

	public Map<Map<String, Integer>, Integer> getGeziMap() {
		return geziMap;
	}

	public void setGeziMap(Map<Map<String, Integer>, Integer> geziMap) {
		this.geziMap = geziMap;
	}
	
	
	
	

}
