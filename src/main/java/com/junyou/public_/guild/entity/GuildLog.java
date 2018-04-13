package com.junyou.public_.guild.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.kernel.data.dao.AbsVersion;

public class GuildLog extends AbsVersion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long time;
	
	private Object[] info;
	
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Object[] getInfo() {
		return info;
	}
	
	public GuildLog(JSONArray json) {
		time = json.getLong(0);
		Object operate = json.get(1);
		Object[] user = null;
		JSONArray userJson = json.getJSONArray(2);
		if(userJson != null){
			user = new Object[]{userJson.get(0),userJson.get(1)};
		}
		Object[] data = null;
		JSONArray dataJson = json.getJSONArray(3);
		if(dataJson != null){
			data = new Object[dataJson.size()];
			for (int i = 0; i < data.length; i++) {
				data[i] = dataJson.get(i);
			}
		}
		info = new Object[]{time,operate,user,data};
	}
	
	/**
	 * 创建日志记录
	 * @param time			时间
	 * @param operate		操作类型
	 * @param user	操作人[0:名字，1：guid]
	 * @param data	附加数据
	 */
	public GuildLog(Long time, Integer operate, Object[] user, Object[] data) {
		this.time = time;
		info = new Object[]{time,operate,user,data};
	}
	
}
