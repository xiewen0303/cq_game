package com.junyou.bus.rfbflower.configue.export;

import java.util.List;
import java.util.Map;


/**
 * @description 在线奖励活动配置表 
 */
public class FlowerCharmRankConfigGroup {
	private String des;//活动描述
	private String pic;//背景图 
	private String md5Version;
	private int maxRank ; //最大排名
	private List<FlowerCharmRankConfig>  configs;
	private Map<String, Integer> itemPrice;
	private Object[] clientData = null;
	
	/**
	 * 转成客户端要的数据
	 * @return
	 */
	private void configsToArray(){
		if(configs!=null && configs.size()>0){
			 clientData = new Object[configs.size()];
			for (int i = 0; i < configs.size(); i++) {
				clientData[i] = configs.get(i).toArray();
			}
		}
		 
	}
	public int getMaxRank() {
		return maxRank;
	}
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}
	public Map<String, Integer> getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Map<String, Integer> itemPrice) {
		this.itemPrice = itemPrice;
	}
	/**
	 * 根据排名获得奖励
	 * @return
	 */
	public FlowerCharmRankConfig getConfigById(int rank){
		if(configs!=null && !configs.isEmpty()){
			for (FlowerCharmRankConfig config : configs) {
				if(rank>=config.getMinRank() && rank<=config.getMaxRank()){
					return config;
				}
			}
		}
		return null;
	}
	
	public Object[] getClientData() {
		return clientData;
	}
	public void setClientData(Object[] clientData) {
		this.clientData = clientData;
	}
	public List<FlowerCharmRankConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(List<FlowerCharmRankConfig> configs) {
		this.configs = configs;
		configsToArray();//把客户端要数据先转好
	}
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	
}
