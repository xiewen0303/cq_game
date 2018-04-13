package com.junyou.bus.touzi;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.junyou.utils.common.ObjectUtil;

public class MailContentTz {
	private ArrayList<String[]> content = new ArrayList<>();
	private Map<String, Integer> itemMap = new TreeMap<>();
	
	public Map<String, Integer> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Integer> itemMap) {
		this.itemMap = itemMap;
	}
	
	/**
	 * 类型,领取次数
	 * @param type
	 * @param count
	 */
	public void addContent(String[] pContent){
		content.add(pContent);
	}
	
	/**
	 * 道具Id,数量
	 * @param type
	 * @param count
	 */
	public void addItemMap(Map<String,Integer> oldMap){
		ObjectUtil.mapAdd(itemMap, oldMap);
	}
	public ArrayList<String[]> getContent() {
		return content;
	}
}
