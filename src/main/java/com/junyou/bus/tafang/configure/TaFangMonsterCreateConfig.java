package com.junyou.bus.tafang.configure;

import java.util.List;
import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 塔防刷怪表
 * @author LiuYu
 * 2015-10-9 下午3:02:09
 */
public class TaFangMonsterCreateConfig {
	private int id;
	private int[] start;
	private int[] stop;
	private Map<Integer,List<String>> monsters;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int[] getStart() {
		return start;
	}
	public void setStart(int[] start) {
		this.start = start;
	}
	public int[] getStop() {
		return stop;
	}
	public void setStop(int[] stop) {
		this.stop = stop;
	}
	public void setMonsters(Map<Integer, List<String>> monsters) {
		this.monsters = new ReadOnlyMap<>(monsters);
	}
	public String getMonster(int level,int state){
		if(monsters == null){
			return null;
		}
		List<String> list = monsters.get(level);
		if(list == null || list.size() <= state){
			return null;
		}
		return list.get(state);
	}
	
}
