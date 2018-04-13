package com.junyou.gameconfig.export.teleport;

import java.util.HashMap;
import java.util.Map;

public class MustItemCheckRule implements ICheckRule {

	private Map<String,Integer> items = new HashMap<String, Integer>();
	
	public MustItemCheckRule(String mustItem) {
		
		String[] itemArr = mustItem.split(";");
		for(String tmp : itemArr){
			String[] data = tmp.split(":");
			items.put(data[0],Integer.parseInt(data[1]));
		}
		
	}

	@Override
	public boolean check(ICheckCallback callback) {
		
		boolean pass = callback.mustItemCheck(items);
		if(!pass){
			callback.mustItemError();
		}
		
		return pass;
	}

	@Override
	public void handle(ICheckCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
