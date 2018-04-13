package com.junyou.gameconfig.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.junyou.gameconfig.goods.configure.export.GoodsConfig;

/**
 * 
 * @description 物品信息
 *
 * @author LiuJuan
 * @created 2011-12-19 上午09:57:19
 */
public class GoodsIdConfig{

	/**物品大类ID*/
	private String goodsId;
	private List<GoodsConfig> goodsConfigs = new ArrayList<>();
	private List<String> goodsIds = new ArrayList<>();
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public List<GoodsConfig> getGoodsConfigs() {
		return new ArrayList<>(goodsConfigs);
	}
	public void addGoodsConfigs(GoodsConfig goodsConfig) {
		this.goodsConfigs.add(goodsConfig);
	}
	public List<String> getGoodsIds() {
		return new ArrayList<>(goodsIds);
	}
	public void sort(){
		if(goodsConfigs.size() > 1){
			Collections.sort(goodsConfigs, GoodsConfigComparator.getInstance());
		}
		for (GoodsConfig config : goodsConfigs) {
			goodsIds.add(config.getId());
		}
	}
	
}
