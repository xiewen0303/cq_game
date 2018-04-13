package com.junyou.bus.rfbactivity.configure.export;

import java.util.Map;

/**
 * 热发布活动关系解析
 * @author DaoZheng Yuan
 * 2015年5月18日 上午11:24:43
 */
public class ReFaBuGxConfig {

	//关系ID
	private int id;
	
	//大类活动配置名称
	private String activityConfigName;
	
	//是否是全服活动
	private boolean isFull;
	
	//只上的服务器列表
	private Map<String,Object> onlyFus;
	
	//只上的平台列表
	private Map<String,Object> onlyPts;
	
	//不用上的服务器列表
	private Map<String,Object> noFus;
	
	//不用上的平台列表
	private Map<String,Object> noPts;
	
	//关系是否已删除
	private boolean isDel;
	//开服几天后开启
	private int days;
	
	public boolean isServerDays(int serverDay){
		if(days == 0){
			return true;
		}else if(serverDay <= days ){
			return false;
		}
		return true;
	}
	
	/**
	 * 是否在only包含在服配置里
	 * @param serverId
	 *  @return true:包含
	 */
	public boolean isContainOnlyFu(String serverId){
		if(onlyFus == null){
			return false;
		}
		
		return onlyFus.containsKey(serverId);
	}
	
	/**
	 * 是否在only包含在平台配置里
	 * @param ptId
	 * @return true:包含
	 */
	public boolean isContainOnlyPt(String ptId){
		if(onlyPts == null){
			return false;
		}
		
		return onlyPts.containsKey(ptId);
	}
	
	/**
	 * 是否在no包含在服配置里
	 * @param serverId
	 *  @return true:包含
	 */
	public boolean isContainNoFu(String serverId){
		if(noFus == null){
			return false;
		}
		
		return noFus.containsKey(serverId);
	}
	
	/**
	 * 是否在no包含在平台配置里
	 * @param ptId
	 *  @return true:包含
	 */
	public boolean isContainNoPt(String ptId){
		if(noPts == null){
			return false;
		}
		
		return noPts.containsKey(ptId);
	}


	/**
	 * 关系ID
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 大类活动配置名称
	 * @return
	 */
	public String getActivityConfigName() {
		return activityConfigName;
	}

	public void setActivityConfigName(String activityConfigName) {
		this.activityConfigName = activityConfigName;
	}

	/**
	 * 是否是全服活动
	 * @return true:是
	 */
	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	/**
	 * 只上的服务器列表
	 * @return
	 */
	public Map<String, Object> getOnlyFus() {
		return onlyFus;
	}

	public void setOnlyFus(Map<String, Object> onlyFus) {
		this.onlyFus = onlyFus;
	}

	/**
	 * 只上的平台列表
	 * @return
	 */
	public Map<String, Object> getOnlyPts() {
		return onlyPts;
	}

	public void setOnlyPts(Map<String, Object> onlyPts) {
		this.onlyPts = onlyPts;
	}

	/**
	 * 不用上的服务器列表
	 * @return
	 */
	public Map<String, Object> getNoFus() {
		return noFus;
	}

	public void setNoFus(Map<String, Object> noFus) {
		this.noFus = noFus;
	}

	/**
	 * 不用上的平台列表
	 * @return
	 */
	public Map<String, Object> getNoPts() {
		return noPts;
	}

	public void setNoPts(Map<String, Object> noPts) {
		this.noPts = noPts;
	}

	/**
	 * 关系是否已删除
	 * @return true:已删除
	 */
	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	
}
