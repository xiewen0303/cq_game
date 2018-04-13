package com.junyou.bus.shouchong.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 首充活动分组配置
 */
public class ShouChongGroupConfig{

	/**
	 * key:需要充值的元宝数
	 * value:首充数据
	 */
	private Map<Integer,ShouChongActivityConfig> scMap;
	
	/**
	 * key:configId
	 * value:首充数据
	 */
	private Map<Integer,ShouChongActivityConfig> dayRechargeMap;
	
	/**
	 * 升序需要元宝列表(小->大)
	 */
	private List<Integer> sortYbs;
	
	
	private String md5Version;
	
	
	private String desc; //描述
	private String bgContent;//背景
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getBgContent() {
		return bgContent;
	}
	public void setBgContent(String bgContent) {
		this.bgContent = bgContent;
	}

	/**
	 * 获取首冲的档位值(有几档)
	 * @return
	 */
	public int getDangSize(){
		if(sortYbs == null){
			return 0;
		}else{
			return sortYbs.size();
		}
	}
	
	
	/**
	 * 首充档配置根据索引
	 * @return
	 */
	public ShouChongActivityConfig getShouChongDangByIndex(int index){
		if(sortYbs == null || sortYbs.size() == 0){
			return null;
		}
		
		/**
		 * 数组越界判断
		 */
		if((sortYbs.size() - index) < 1){
			return null;
		}
		
		Integer needYb = sortYbs.get(index);
		return scMap.get(needYb);
	}

	public Map<Integer, ShouChongActivityConfig> getScMap() {
		return scMap;
	}
	
	/**
	 * 获得所有配置信息
	 * @return
	 */
	public Map<Integer, ShouChongActivityConfig> getDayRechargeMap() {
		return dayRechargeMap;
	}
	
	/**
	 * 通过配置Id获得充值配置信息
	 * @return
	 */
	public ShouChongActivityConfig getLoopDayChongZhiConfig(int configId){
		return dayRechargeMap.get(configId);
	}

	public void setScMap(Map<Integer, ShouChongActivityConfig> scMap) {
		this.scMap = scMap;
	}
	
	public void setDayRechargeMap(Map<Integer, ShouChongActivityConfig> dayRechargeMap) {
		this.dayRechargeMap = dayRechargeMap;
	}


	public void addShouChongActivityConfig(ShouChongActivityConfig config){
		if(scMap == null){
			scMap = new HashMap<Integer, ShouChongActivityConfig>();
			sortYbs = new ArrayList<>();
		}
		scMap.put(config.getNeedYb(), config);
		
		//因为list可以放重复值对象，所以要判断list里没有这个值
		if(!sortYbs.contains(config.getNeedYb())){
			sortYbs.add(config.getNeedYb());
		}
		if(dayRechargeMap == null){
			dayRechargeMap = new HashMap<>();
		}
		dayRechargeMap.put(config.getConfigId(), config);
	}


	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	
	/**
	 * 需要元宝升序排序(小->大)
	 */
	public void sortNeedYbList(){
		if(sortYbs == null || sortYbs.size() == 1){
			return;
		}else{
			//冒泡排序(小->大)
			for (int i = 0; i < sortYbs.size() - 1; i++) {
				
				for (int j = i+1; j < sortYbs.size(); j++) {
					Integer outValue = sortYbs.get(i);
					Integer innerValue = sortYbs.get(j);
					
					if(innerValue < outValue){
						//互换
						sortYbs.set(j, outValue);
						sortYbs.set(i, innerValue);
					}
				}
			}
		}
	}

	
	/**
	 * 获取排序后的值
	 */
	public List<Integer> getSortYbs(){
		return sortYbs;
	}
}
