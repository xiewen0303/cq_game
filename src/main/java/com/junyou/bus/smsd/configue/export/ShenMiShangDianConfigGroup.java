package com.junyou.bus.smsd.configue.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class ShenMiShangDianConfigGroup {
	
	private Map<Integer, ShenMiShangDianConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private Integer xfType;//消费类型
	private Integer sxTime;//自动刷新时间
	private Integer sxGold;//刷新元宝
	private Object[] xianshi;
	
	
	public Integer getSxTime() {
		if(sxTime == null || sxTime == 0){
			return 60*60;
		}
		return sxTime;
	}

	public void setSxTime(Integer sxTime) {
		this.sxTime = sxTime;
	}

	public Integer getSxGold() {
		/*if(sxGold == null || sxGold == 0){
			return 10;
		}*/
		return sxGold;
	}

	public void setSxGold(Integer sxGold) {
		this.sxGold = sxGold;
	}


	public Object[] getXianshi() {
		return xianshi;
	}

	public void setXianshi(Object[] xianshi) {
		this.xianshi = xianshi;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getXfType() {
		return xfType;
	}

	public void setXfType(Integer xfType) {
		this.xfType = xfType;
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

	public Map<Integer, ShenMiShangDianConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, ShenMiShangDianConfig> configMap) {
		this.configMap = configMap;
	}
	
	
}
