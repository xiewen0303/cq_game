package com.junyou.bus.lianchong.configure.export;

import java.util.List;

/**
 * @description 连充活动配置表 
 */
public class LianChongConfigGroup {
	private String des;//活动描述
	private String pic;//背景图 
	private String md5Version;
	private List<LianChongConfig>  configs;
	
	/**
	 * 转成客户端要的数据
	 * @return
	 */
	public Object[] configsToArray(){
		Object[] obj =null;
		if(configs!=null && configs.size()>0){
			 obj = new Object[configs.size()];
			for (int i = 0; i < configs.size(); i++) {
				obj[i] = configs.get(i).toArray();
			}
		}
		return obj;
	}
	/**
	 * 
	 * @return
	 */
	public LianChongConfig getConfigById(int configId){
		if(configs!=null && configs.size()>0){
			for (LianChongConfig config : configs) {
				if(config.getId()==configId){
					return config;
				}
			}
		}
		return null;
	}
	public List<LianChongConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(List<LianChongConfig> configs) {
		this.configs = configs;
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
