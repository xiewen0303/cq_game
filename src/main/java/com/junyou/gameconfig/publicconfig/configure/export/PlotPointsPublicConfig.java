package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description 剧情传送坐标信息
 */
public class PlotPointsPublicConfig extends AdapterPublicConfig {
	
	private Map<Integer,int[]> result = new HashMap<>();
	
	public void addData(String msg) {
		String[] data = msg.split(":");
		result.put(Integer.parseInt(data[0]), new int[]{Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(data[3])});
	}
	
	public int[] getData(int key){
		return result.get(key);
	}
}