package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * 守护副本公共配置
 * @author LiuYu
 * @date 2015-4-27 下午2:57:08
 */
public class ShouhuPublicConfig extends AdapterPublicConfig{
	private String shouhuId;
	private Integer[] shouhuPoint;
	private Integer[] monsterPoint;
	private int mapId;
	private int needGold;
	public String getShouhuId() {
		return shouhuId;
	}
	public void setShouhuId(String shouhuId) {
		this.shouhuId = shouhuId;
	}
	public Integer[] getShouhuPoint() {
		return shouhuPoint;
	}
	public void setShouhuPoint(Integer[] shouhuPoint) {
		this.shouhuPoint = shouhuPoint;
	}
	public Integer[] getMonsterPoint() {
		return monsterPoint;
	}
	public void setMonsterPoint(Integer[] monsterPoint) {
		this.monsterPoint = monsterPoint;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getNeedGold() {
		return needGold;
	}
	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}
	
	
	
}
