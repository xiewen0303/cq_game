package com.junyou.bus.platform.qq.confiure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 * 腾讯微端奖励配置
 * @author zhongdian
 *
 */
public class QqWeiDuanPublicConfig extends AdapterPublicConfig {

	Map<Integer, Map<String, Integer>> weiDuanMap = new HashMap<>();

	public Map<Integer, Map<String, Integer>> getWeiDuanMap() {
		return weiDuanMap;
	}

	public void setWeiDuanMap(Map<Integer, Map<String, Integer>> weiDuanMap) {
		this.weiDuanMap = weiDuanMap;
	}


}
