package com.junyou.gameconfig.goods.configure.export;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;
 
/**
 * 道具限制配置表 
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:27:16
  *@Description:
 */
public class DaoJuXzConfig  {

	private Integer id;
	 
	/**
	 * key:vipLevel   value:useCount
	 */
	private Map<Integer,Integer> useTimes;//VIP列表	 每日使用次数限制：0或不填：无限制>0的整数：限制次数
	
	public Integer getId() {
		return id;
	}
 

	public Map<Integer, Integer> getUseTimes() {
		return useTimes;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void addUserTimes(ReadOnlyMap<Integer, Integer> useTimes){
		this.useTimes=useTimes;
	} 
	
	public Integer getUseTimes(int vipLevel){
		return useTimes.get(vipLevel);
	}

}
