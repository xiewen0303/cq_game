package com.junyou.bus.login.configure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class RefabuLoginConfigGroup {
	
	private Map<Integer, RefabuLoginConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private List<String> clientItem;//客户端要的奖励数据
	
	public List<String> getClientItem() {
		return clientItem;
	}

	public void setClientItem(List<String> clientItem) {
		this.clientItem = clientItem;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Map<Integer, RefabuLoginConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, RefabuLoginConfig> configMap) {
		this.configMap = configMap;
	}
	
	
}
