package com.junyou.bus.platform.qq.confiure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class QqRechargePublicConfig extends AdapterPublicConfig {

	Map<String, String> chargeMap = new HashMap<String, String>();

	public Map<String, String> getChargeMap() {
		return chargeMap;
	}

	public void setChargeMap(Map<String, String> chargeMap) {
		this.chargeMap = chargeMap;
	}
	
	
}
