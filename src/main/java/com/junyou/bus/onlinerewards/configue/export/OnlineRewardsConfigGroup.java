package com.junyou.bus.onlinerewards.configue.export;

import java.util.List;


/**
 * @description 在线奖励活动配置表 
 */
public class OnlineRewardsConfigGroup {
	private String des;//活动描述
	private String pic;//背景图 
	private String md5Version;
	private List<OnlineRewardsConfig>  configs;
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
	/**
	 * 
	 * @return
	 */
	public OnlineRewardsConfig getConfigById(int configId){
		if(configs!=null && !configs.isEmpty()){
			for (OnlineRewardsConfig config : configs) {
				if(config.getId().intValue()==configId){
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
	public List<OnlineRewardsConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(List<OnlineRewardsConfig> configs) {
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
