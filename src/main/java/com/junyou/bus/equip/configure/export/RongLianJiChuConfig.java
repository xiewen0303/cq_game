package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.log.ChuanQiLog;

/**
 * 
 * @description 熔炼基础表 
 *
 * @author ZHONGDIAN
 * @date 2015-05-31 18:32:40
 */
public class RongLianJiChuConfig{

	private Integer rlzmax;
	private Integer xuantie;
	private Map<Object[],Float> itemMap;
	


	
	public Integer getRlzmax() {
		return rlzmax;
	}
	public void setRlzmax(Integer rlzmax) {
		this.rlzmax = rlzmax;
	}
	public Integer getXuantie() {
		return xuantie;
	}

	public void setXuantie(Integer xuantie) {
		this.xuantie = xuantie;
	}

	public Map<Object[], Float> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<Object[], Float> itemMap) {
		try {
			this.itemMap = new HashMap<Object[], Float>(itemMap);
		} catch (Exception e) {
			ChuanQiLog.error("ronglianjichuConfig:", e);
		}
	}
	
	
	public RongLianJiChuConfig copy(){
		return null;
	}


}
