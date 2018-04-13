package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class QqLZnianfeiPublicConfig extends AdapterPublicConfig {
	private Map<String, Integer> nianfei;

	private Map<String, Integer> haohua;
	
	public Map<String, Integer> getHaohua() {
		return haohua;
	}

	public void setHaohua(Map<String, Integer> haohua) {
		this.haohua = haohua;
	}

	public Map<String, Integer> getNianfei() {
		return nianfei;
	}

	public void setNianfei(Map<String, Integer> nianfei) {
		this.nianfei = nianfei;
	}
	

}
