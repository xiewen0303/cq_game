package com.junyou.bus.xiaofei.configure.export;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 
 * @description 七日开服活动配置表 (消费排行)
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class XiaofeiConfig {
	
	private Integer id;
	private Integer min;
	private Integer max;
	
	private String emailItem;
	public String getEmailItem() {
		return emailItem;
	}

	public void setEmailItem(String emailItem) {
		this.emailItem = emailItem;
	}

	//奖励物品-服务自己用的(通用奖励和职业奖励已合并)
	private Map<String,GoodsConfigureVo> jianLiMap;
	
	//奖励物品-给客户端用的(通用奖励和职业奖励已合并)
	private Object[] jianLiClientMap;

	public Map<String, GoodsConfigureVo> getJianLiMap() {
		return jianLiMap;
	}

	public void setJianLiMap(Map<String, GoodsConfigureVo> jianLiMap) {
		this.jianLiMap = jianLiMap;
	}

	public Object[] getJianLiClientMap() {
		return jianLiClientMap;
	}

	public void setJianLiClientMap(Object[] jianLiClientMap) {
		this.jianLiClientMap = jianLiClientMap;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	
	
	
}
