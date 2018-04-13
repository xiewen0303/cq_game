package com.junyou.bus.shenqi.configure.export;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.kernel.data.dao.AbsVersion;

/**
 * 神器洗练
 */
public class ShenQiXiLianConfig extends AbsVersion implements
Comparable<ShenQiXiLianConfig>{ 
	
	private int rank;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	// JSONArray 数组第一位表示道具洗练，第二位表示元宝洗练.
	private Map<String, JSONArray> xiLianMap;
	
	public Map<String, JSONArray> getXiLianMap() {
		return xiLianMap;
	}
	public void setXiLianMap(Map<String, JSONArray> xiLianMap) {
		this.xiLianMap = xiLianMap;
	}
	 
	/**
	 * 按照rank从小到大排序下
	 */
	@Override
	public int compareTo(ShenQiXiLianConfig o) {
		if(o.getRank()<rank){
			return 1;//升序
		}else{
			return -1;
		}
	}
	 
}
