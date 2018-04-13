package com.junyou.bus.platform.qq.confiure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 * 腾讯暑假活动配置
 * @author zhongdian
 *
 */
public class QqShuJiaPublicConfig extends AdapterPublicConfig {

	Map<String, String> renwuMap = new HashMap<String, String>();

	public Map<String, String> getRenwuMap() {
		return renwuMap;
	}

	public void setRenwuMap(Map<String, String> renwuMap) {
		this.renwuMap = renwuMap;
	}

}
