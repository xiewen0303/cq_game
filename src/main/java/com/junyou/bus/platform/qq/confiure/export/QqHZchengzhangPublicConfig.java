package com.junyou.bus.platform.qq.confiure.export;

import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class QqHZchengzhangPublicConfig extends AdapterPublicConfig {
	private Map<Integer, Integer> levels;
	
	public Map<Integer, Integer> getLevels() {
		return levels;
	}
	
	private List<Integer> nList ;
	
	public List<Integer> getnList(){
		return nList;
	}
	

	public void setnList(List<Integer> nList) {
		this.nList = nList;
	}


	public void setLevels(Map<Integer, Integer> levels) {
		this.levels = levels;
	}
	public Integer getLevelByN(Integer n){
		return levels.get(n);
	}
	private Map<Integer, Map<String, Integer>> items;

	public Map<Integer, Map<String, Integer>> getItems() {
		return items;
	}

	public void setItems(Map<Integer, Map<String, Integer>> items) {
		this.items = items;
	}

	public Map<String,Integer> getItemsByN(Integer n){
		return items.get(n);
	}
} 
