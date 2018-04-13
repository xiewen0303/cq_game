package com.junyou.bus.platform.qq.confiure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 * 腾讯TGP兑换配置
 * @author zhongdian
 *
 */
public class QqTgpDuiHuanPublicConfig extends AdapterPublicConfig {

	Map<String, Map<String, Integer>> tgpMap = new HashMap<>();

	public Map<String, Map<String, Integer>> getTgpMap() {
		return tgpMap;
	}

	public void setTgpMap(Map<String, Map<String, Integer>> tgpMap) {
		this.tgpMap = tgpMap;
	}


}
