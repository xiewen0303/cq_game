package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.HashMap;
import java.util.Map;


/**
 * 好友相关配置（公共配置中）
 * @description 
 * @author Hanchun
 * @date 2015-1-6 下午8:21:43
 */
public class FriendPublicConfig extends AdapterPublicConfig{
	
	private int maxFirend;//好友数量的上线
	
	private int maxBlack;//黑名单数量上限
	
	private int maxEnemy;//仇人数量上限
	
	private Map<Integer,String> data = new HashMap<Integer, String>();
	/**
	 * 获取添加好友的上限
	 * @return
	 */
	public int getMaxFirend() {
		return maxFirend;
	}

	/**
	 * 设置好友上限
	 * @param value1
	 */
	public void setMaxFirend(int maxFirend) {
		this.maxFirend = maxFirend;
	}

	/**
	 * 获取添加黑名单上限
	 * @return
	 */
	public int getMaxBlack() {
		return maxBlack;
	}

	/**
	 * 设置黑名单上限
	 * @param value2
	 */
	public void setMaxBlack(int maxBlack) {
		this.maxBlack = maxBlack;
	}

	/**
	 * 获取添加仇人上限
	 * @return
	 */
	public int getMaxEnemy() {
		return maxEnemy;
	}

	/**
	 * 设置仇人上限
	 * @param value3
	 */
	public void setMaxEnemy(int maxEnemy) {
		this.maxEnemy = maxEnemy;
	}

	public Map<Integer, String> getData() {
		return data;
	}

	public void setData(Map<Integer, String> data) {
		this.data = data;
	}
	
	public  String  getData(Integer key) {
		return data.get(key);
	}

}
