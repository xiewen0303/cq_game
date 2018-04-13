package com.junyou.bus.leichong.configue.export;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 
 * @description 七日开服活动配置表 (全民修仙)
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class LeiChongConfig {
	
	private Integer id;
	private Integer xfValue;//消费类型具体值
	private Integer count = 1;//可领取次数(默认一次)
	//奖励物品-服务自己用的(通用奖励和职业奖励已合并)
	private Map<String,GoodsConfigureVo> jianLiMapA;
	
	//奖励物品-给客户端用的(通用奖励和职业奖励已合并)
	private Object[] jianLiClientMapA;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getXfValue() {
		return xfValue;
	}
	public void setXfValue(Integer xfValue) {
		this.xfValue = xfValue;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Map<String, GoodsConfigureVo> getJianLiMapA() {
		return jianLiMapA;
	}
	public void setJianLiMapA(Map<String, GoodsConfigureVo> jianLiMapA) {
		this.jianLiMapA = jianLiMapA;
	}
	public Object[] getJianLiClientMapA() {
		return jianLiClientMapA;
	}
	public void setJianLiClientMapA(Object[] jianLiClientMapA) {
		this.jianLiClientMapA = jianLiClientMapA;
	}
	
	public Object[] getVo(){
		return new Object[]{
				getId(),
				getXfValue(),
				getJianLiClientMapA()
		};
	}
	
	
	
	
}
