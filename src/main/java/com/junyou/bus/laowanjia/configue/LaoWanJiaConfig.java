package com.junyou.bus.laowanjia.configue;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 
 * @description 老玩家回归
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class LaoWanJiaConfig {
	
	private Integer id;
	private Integer loginDay;//登陆天数
	private Integer reGold;//累计充值元宝
	//奖励物品-服务自己用的(通用奖励和职业奖励已合并)
	private Map<String,GoodsConfigureVo> jianLiMapA;
	private Map<String,GoodsConfigureVo> jianLiMapB;
	
	//奖励物品-给客户端用的(通用奖励和职业奖励已合并)
	private Object[] jianLiClientMapA;
	private Object[] jianLiClientMapB;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getLoginDay() {
		return loginDay;
	}
	public void setLoginDay(Integer loginDay) {
		this.loginDay = loginDay;
	}
	public Integer getReGold() {
		return reGold;
	}
	public void setReGold(Integer reGold) {
		this.reGold = reGold;
	}
	public Map<String, GoodsConfigureVo> getJianLiMapA() {
		if(jianLiMapA == null){
			return null;
		}
		return jianLiMapA;
	}
	public void setJianLiMapA(Map<String, GoodsConfigureVo> jianLiMapA) {
		this.jianLiMapA = jianLiMapA;
	}
	
	public Map<String, GoodsConfigureVo> getJianLiMapB() {
		if(jianLiMapB == null){
			return null;
		}
		return jianLiMapB;
	}
	public void setJianLiMapB(Map<String, GoodsConfigureVo> jianLiMapB) {
		this.jianLiMapB = jianLiMapB;
	}
	public Object[] getJianLiClientMapA() {
		if(jianLiClientMapA == null){
			return null;
		}
		return jianLiClientMapA;
	}
	public void setJianLiClientMapA(Object[] jianLiClientMapA) {
		this.jianLiClientMapA = jianLiClientMapA;
	}
	public Object[] getJianLiClientMapB() {
		if(jianLiClientMapB == null){
			return null;
		}
		return jianLiClientMapB;
	}
	public void setJianLiClientMapB(Object[] jianLiClientMapB) {
		this.jianLiClientMapB = jianLiClientMapB;
	}
	
	
	
}
