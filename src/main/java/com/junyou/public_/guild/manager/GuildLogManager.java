package com.junyou.public_.guild.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.junyou.constants.GameConstants;
import com.junyou.public_.guild.entity.GuildLog;

public class GuildLogManager {

	private List<GuildLog> logList = new LinkedList<GuildLog>();
	private boolean update = false;
	private byte[] lock = new byte[0];
	private List<GuildLog> logOutList = new ArrayList<GuildLog>();
	private int updateCount;
	
	public void addLog(GuildLog log){
		synchronized (lock) {
			update = true;
			if(logList.size() >= GameConstants.MAX_LOG){
				logList.remove(0);
			}
			logList.add(log);
		}
		updateCount++;
	}
	
	public void initLog(JSONArray array){
		update = true;
		for (Object object : array) {
			JSONArray json = (JSONArray) JSONArray.toJSON(object);
			logList.add(new GuildLog(json));
		}
	}
	
	private void refreshLog(){
		if(!update){
			return;
		}
		synchronized (lock) {
			if(update){
				logOutList.clear();
				int size = logList.size();
				for(int i = size-1;i >= 0;i--){
					logOutList.add(logList.get(i));
				}
			}
		}
	}
	
	public Object[] getLogs(int start,int size){
		refreshLog();
		int max = logOutList.size() - start;
		if(max > size){
			max = size;
		}
		if(max < 0 ){
			max = 0;
		}
		Object[] out = new Object[max];
		for(int i = 0;i < max;i++){
			int index = i + start;
			out[i] = logOutList.get(index).getInfo();
		}
		return new Object[]{start,logOutList.size(),out};
	}
	
	public int count(){
		return logOutList.size();
	}
	
	/**
	 * 最终写入
	 */
	public void finalWrite(){
		if(updateCount > 0){
			updateCount = 10;
		}
	}
	
	public JSONArray getFileSource(){
		if(updateCount < 10){
			return null;
		}
		JSONArray array = new JSONArray();
		synchronized (lock) {
			for (GuildLog log : logList) {
				array.add(log.getInfo());
			}
		}
		updateCount = 0;
		return array;
	}
	
}
