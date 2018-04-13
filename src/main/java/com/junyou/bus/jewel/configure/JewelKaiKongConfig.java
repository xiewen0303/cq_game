package com.junyou.bus.jewel.configure;

import java.util.Map;
/**
 * 开孔表
 * @author lxn
 *
 */
public class JewelKaiKongConfig {

	private Integer geiWeiId;//格位id
	private Map<Integer, Object[]> consumeMap;//孔编号id:Object[] =[孔编号id,开孔登记level,开孔消耗银两money]
	
	public Map<Integer, Object[]> getConsumeMap() {
		return consumeMap;
	}
	public void setConsumeMap(Map<Integer, Object[]> consumeMap) {
		this.consumeMap = consumeMap;
	}
	public Integer getGeiWeiId() {
		return geiWeiId;
	}
	public void setGeiWeiId(Integer geiWeiId) {
		this.geiWeiId = geiWeiId;
	}
	 
	
	
}
