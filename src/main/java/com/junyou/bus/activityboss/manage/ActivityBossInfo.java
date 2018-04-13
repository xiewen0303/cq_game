package com.junyou.bus.activityboss.manage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 活动boss的记录信息
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-29 上午11:43:04
 */
public class ActivityBossInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3908977634630317071L;
	
	private int id; //定时刷怪表配置Id
	
	private int mapId;
	
	//line = BossKillInfo (击杀boss的信息)
	private Map<Integer,BossKillInfo> bossKillInfos = new HashMap<>(); 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ActivityBossInfo(int id, int mapId) {
		super();
		this.id = id;
		this.mapId = mapId;
	}

//	@Override
//	public boolean equals(Object obj) {
//		 if(obj != null){
//			 ActivityBossInfo abi = (ActivityBossInfo)obj;
//			 if(abi.getId() == this.getId()){
//				 return true;
//			 }
//		 }
//		 return false;
//	}
	
	public Map<Integer, BossKillInfo> getBossKillInfos() {
		return bossKillInfos;
	}

	public void setBossKillInfos(Map<Integer, BossKillInfo> bossKillInfos) {
		this.bossKillInfos = bossKillInfos;
	}

	public BossKillInfo getBossKillInfo(int line){
		return bossKillInfos.get(line);
	}
	
	public void addBossKillInfo(BossKillInfo bkInfo){
		bossKillInfos.put(bkInfo.getLine(), bkInfo);
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
}
