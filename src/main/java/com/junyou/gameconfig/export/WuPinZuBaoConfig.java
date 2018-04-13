package com.junyou.gameconfig.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 物品组包表配置 
 *
 * @author DaoZhen Yuan
 * @date 2013-02-27 09:50:30
 */
public class WuPinZuBaoConfig extends AbsVersion{

	private String id;
	
	private int needLevel;
	
	private int needYb;
	
	private int needJb;
	
	private String needItemId;
	
	private int needItemCount;
	
	private Map<WuPinZuBaoValue,Float> zubaoMap;
	
	
	public boolean isNeedItem(){
		if(needItemId != null && !needItemId.equals("")){
			return true;
		}
		return false;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNeedLevel() {
		return needLevel;
	}

	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}

	public int getNeedYb() {
		return needYb;
	}

	public void setNeedYb(int needYb) {
		this.needYb = needYb;
	}

	public int getNeedJb() {
		return needJb;
	}

	public void setNeedJb(int needJb) {
		this.needJb = needJb;
	}

	public String getNeedItemId() {
		return needItemId;
	}

	public void setNeedItemId(String needItemId) {
		this.needItemId = needItemId;
	}

	public int getNeedItemCount() {
		return needItemCount;
	}

	public void setNeedItemCount(int needItemCount) {
		this.needItemCount = needItemCount;
	}

	public Map<WuPinZuBaoValue, Float> getZubaoMap() {
		return zubaoMap;
	}

	public void setZubaoMap(Map<WuPinZuBaoValue, Float> zubaoMap) {
		this.zubaoMap = zubaoMap;
	}


}
