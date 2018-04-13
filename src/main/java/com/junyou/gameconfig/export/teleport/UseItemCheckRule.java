package com.junyou.gameconfig.export.teleport;

import java.util.HashMap;
import java.util.Map;

public class UseItemCheckRule implements ICheckRule {

	private Map<String,Integer> items = new HashMap<String, Integer>();
	
	public UseItemCheckRule(String useItem) {
		
		String[] itemArr = useItem.split(";");
		for(String tmp : itemArr){
			String[] data = tmp.split(":");
			items.put(data[0],Integer.parseInt(data[1]));
		}
		
	}

	@Override
	public boolean check(ICheckCallback callback) {
		boolean pass = callback.useItemCheck(items);
		if(!pass){
			callback.useItemError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
	
		for(String item : items.keySet()){
			callback.useItemHandle(item, items.get(item));
		}
		
	}

}
