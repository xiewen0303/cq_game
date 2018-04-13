package com.junyou.bus.kaifuactivity.configure.export;

import java.util.ArrayList;
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
public class KaiFuXianJieJingJiGroupConfig {
	
	private Map<Integer, KaiFuXianJieJingJiConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//活动底图
	private String des1;
	
	private List<Object[]> pxData = new ArrayList<>();
	private List<Object[]> tzData = new ArrayList<>();
	

	public List<Object[]> getPxData() {
		return pxData;
	}

	public void setPxData(List<Object[]> pxData) {
		this.pxData = pxData;
	}

	public List<Object[]> getTzData() {
		return tzData;
	}

	public void setTzData(List<Object[]> tzData) {
		this.tzData = tzData;
	}

	public String getDes1() {
		return des1;
	}

	public void setDes1(String des1) {
		this.des1 = des1;
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

	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Map<Integer, KaiFuXianJieJingJiConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, KaiFuXianJieJingJiConfig> configMap) {
		this.configMap = configMap;
	}
	
	
}
