package com.junyou.bus.oncechong.configure.export;

import java.util.Map;
import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 单笔充值
 */
public class OnceChongConfig {
	
	private Integer id;
	private Integer endValue;//充值元宝值
	private Integer beginValue;//充值元宝值
	
	//奖励物品-服务自己用的(通用奖励和职业奖励已合并)
	private Map<Byte,Map<String,GoodsConfigureVo>> jianLiMap;
	
	//奖励物品-服务自己用的(第一次奖励和职业奖励已合并)
	private Map<Byte,Map<String,GoodsConfigureVo>> firstJianLiMap;
	
	//奖励物品-给客户端用的(通用奖励和职业奖励已合并)
	private Map<Byte,Object[]> jianLiClientMap; 
	
	//奖励物品-给客户端用的(通用奖励和职业奖励已合并)
	private Map<Byte,Object[]> firstJianLiClientMap; 
	
	private int fanhuana;
	private int fanhuanb;
	
	public int getFanhuana() {
		return fanhuana;
	}
	public void setFanhuana(int fanhuana) {
		this.fanhuana = fanhuana;
	}
	public int getFanhuanb() {
		return fanhuanb;
	}
	public void setFanhuanb(int fanhuanb) {
		this.fanhuanb = fanhuanb;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEndValue() {
		return endValue;
	}
	public void setEndValue(Integer endValue) {
		this.endValue = endValue;
	}
	public Integer getBeginValue() {
		return beginValue;
	}
	public void setBeginValue(Integer beginValue) {
		this.beginValue = beginValue;
	}
	public Map<String, GoodsConfigureVo> getJianLiMap(Byte job) {
		if(jianLiMap == null){
			return null;
		}
		return jianLiMap.get(job);
	}
	
	public Map<String, GoodsConfigureVo> getFirstJianLiMap(Byte job) {
		if(firstJianLiMap == null){
			return null;
		}
		return firstJianLiMap.get(job);
	}
	
	public void setJianLiMap(Map<Byte, Map<String, GoodsConfigureVo>> jianLiMap) {
		this.jianLiMap = jianLiMap;
	}
	
	public Object[] getJianLiClientMap(Byte job) {
		if(jianLiClientMap == null){
			return null;
		}
		return jianLiClientMap.get(job);
	}
	
	public Object[] getFirstJianLiClientMap(Byte job) {
		if(firstJianLiClientMap == null){
			return null;
		}
		return firstJianLiClientMap.get(job);
	}
	
	public void setJianLiClientMap(Map<Byte, Object[]> jianLiClientMapA) {
		this.jianLiClientMap = jianLiClientMapA;
	}
	public void setFirstJianLiMap(
			Map<Byte, Map<String, GoodsConfigureVo>> firstJianLiMap) {
		this.firstJianLiMap = firstJianLiMap;
	}
	public void setFirstJianLiClientMap(Map<Byte, Object[]> firstJianLiClientMap) {
		this.firstJianLiClientMap = firstJianLiClientMap;
	}
}
