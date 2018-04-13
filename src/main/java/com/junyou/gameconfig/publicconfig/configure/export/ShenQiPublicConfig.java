package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.log.LogPrintHandle;


/**
 * 神器相关
 * @author LiuYu
 * @date 2015-7-8 上午9:37:35
 */
public class ShenQiPublicConfig extends AdapterPublicConfig{
	private int sqId;
	private int gold;
	private int day;
	private int delayDay;// 延迟指定开服天数后开启限时神器活动
	private JSONArray jsonArray;
	
	public int getSqId() {
		return sqId;
	}
	public void setSqId(int sqId) {
		this.sqId = sqId;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getDelayDay() {
        return delayDay;
    }
    public void setDelayDay(int delayDay) {
        this.delayDay = delayDay;
    }
    
    public JSONArray getConsume(){
		if(jsonArray == null){
			Map<String,Integer> map = new HashMap<>();
			map.put(ModulePropIdConstant.GOLD_GOODS_ID, gold);
			jsonArray = LogPrintHandle.getLogGoodsParam(map, null);
		}
		return jsonArray;
	}
	
}
