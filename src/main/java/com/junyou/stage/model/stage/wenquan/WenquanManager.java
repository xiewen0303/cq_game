package com.junyou.stage.model.stage.wenquan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.junyou.stage.model.stage.StageManager;

public class WenquanManager {
	private static WenquanManager manager = new WenquanManager();
	
	public void clear(){
		for (WenquanStage stage : activeStages) {
			if(stage.isCanRemove()){
				StageManager.removeCopy(stage);
			}
		}
		activeStages.clear();
		jingyanMap.clear();
		zhenqiMap.clear();
		highAreaRoleIdList.clear();
		cosumeGoldMap.clear();
		julinSet.clear();
	}

	public static WenquanManager getManager() {
		return manager;
	}
	
	private Set<Long> julinSet = new HashSet<Long>();
	public void addJulinSet(Long userRoleId){
		julinSet.add(userRoleId);
	}
	
	public Set<Long> getJulinSet(){
		return julinSet;
	}
	
	private Map<Long,Integer> cosumeGoldMap = new HashMap<Long,Integer>();
	public void addConsumeGold(Long userRoleId, Integer gold) {
		Integer value = cosumeGoldMap.get(userRoleId);
		if (value == null) {
			cosumeGoldMap.put(userRoleId, gold);
		} else {
			cosumeGoldMap.put(userRoleId, gold + value);
		}
	}

	public Integer getConsumeGold(Long userRoleId) {
		Integer value = cosumeGoldMap.get(userRoleId);
		if (value == null) {
			return 0;
		} else {
			return value;
		}
	}

	private List<WenquanStage> activeStages = new ArrayList<>();

	private Map<Long, Long> jingyanMap = new HashMap<Long, Long>();

	public void addJingyan(Long userRoleId, Long jingyan) {
		Long value = jingyanMap.get(userRoleId);
		if (value == null) {
			jingyanMap.put(userRoleId, jingyan);
		} else {
			jingyanMap.put(userRoleId, jingyan + value);
		}
	}

	public Long getJingyan(Long userRoleId) {
		Long value = jingyanMap.get(userRoleId);
		if (value == null) {
			return 0L;
		} else {
			return value;
		}
	}

	private Map<Long, Long> zhenqiMap = new HashMap<Long, Long>();

	public void addZhenqi(Long userRoleId, Long zhenqi) {
		Long value = zhenqiMap.get(userRoleId);
		if (value == null) {
			zhenqiMap.put(userRoleId, zhenqi);
		} else {
			zhenqiMap.put(userRoleId, zhenqi + value);
		}
	}

	public Long getZhenqi(Long userRoleId) {
		Long value = zhenqiMap.get(userRoleId);
		if (value == null) {
			return 0L;
		} else {
			return value;
		}
	}
	
	private List<Long> highAreaRoleIdList = new ArrayList<Long>();
	public boolean isInHighArea(Long userRoleId){
		return highAreaRoleIdList.contains(userRoleId);
	}
	public void setInHighArea(Long userRoleId,boolean flag){
		if(flag){
			if(!highAreaRoleIdList.contains(userRoleId)){
				highAreaRoleIdList.add(userRoleId);
			}
		}else{
			if(highAreaRoleIdList.contains(userRoleId)){
				highAreaRoleIdList.remove(userRoleId);
			}
		}
	}

	public void addStage(WenquanStage stage) {
		activeStages.add(stage);
		StageManager.addStageCopy(stage);
	}
	
	public List<WenquanStage> getStages(){
		return activeStages;
	}
	public void stop(){
		for (WenquanStage stage : activeStages) {
			stage.stop();
		}
	}
}
