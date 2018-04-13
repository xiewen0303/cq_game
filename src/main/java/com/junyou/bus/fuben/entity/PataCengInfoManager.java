package com.junyou.bus.fuben.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author LiuYu
 * 2015-6-11 上午9:53:35
 */
public class PataCengInfoManager {
	private static PataCengInfoManager manager = new PataCengInfoManager();
	public static PataCengInfoManager getManager(){
		return manager;
	}
	
	private Map<Integer,PataCengInfo> pataCengInfo;
	
	public void init(Map<Integer,JSONObject> pataCengInfo){
		this.pataCengInfo = new HashMap<>();
		if(pataCengInfo != null){
			for (Entry<Integer, JSONObject> entry : pataCengInfo.entrySet()) {
				this.pataCengInfo.put(entry.getKey(), JSON.parseObject(entry.getValue().toString(), PataCengInfo.class));
			}
		}
	}

	public Map<Integer, PataCengInfo> getPataCengInfo() {
		return pataCengInfo;
	}
	
	public PataCengInfo getCengInfo(Integer cengId){
		return pataCengInfo.get(cengId);
	}
	
	public void initCengInfo(Integer cengId,String roleName,int time){
		PataCengInfo cengInfo = new PataCengInfo();
		cengInfo.setCengId(cengId);
		cengInfo.setLastRole(roleName);
		cengInfo.setLastTime(time);
		pataCengInfo.put(cengId, cengInfo);
	}
	
}
