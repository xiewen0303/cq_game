package com.junyou.bus.xingkongbaozang.configure.export;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 
 * @description 七日开服活动配置表 (全民修仙)
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class XkbzConfig {
	
	private Integer id;
	private Integer jifen;//消费类型具体值
	//奖励物品-服务自己用的(通用奖励和职业奖励已合并)
	private Map<String,GoodsConfigureVo> itemMap;
	//奖励物品-给客户端用的(通用奖励和职业奖励已合并)
	private String jianLiClient;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJifen() {
		return jifen;
	}
	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}
	public Map<String, GoodsConfigureVo> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, GoodsConfigureVo> itemMap) {
		this.itemMap = itemMap;
	}
	public String getJianLiClient() {
		return jianLiClient;
	}
	public void setJianLiClient(String jianLiClient) {
		this.jianLiClient = jianLiClient;
	}
	public Object[] getVo(){
		return new Object[]{
				getId(),
				getJifen(),
				getJianLiClient()
		};
	}
	
	
	
	
}
