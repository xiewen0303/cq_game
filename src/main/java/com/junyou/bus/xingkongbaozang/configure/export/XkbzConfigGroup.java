package com.junyou.bus.xingkongbaozang.configure.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class XkbzConfigGroup {
	
	private Map<Integer, XkbzConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String des2;//活动描述
	private String pic;//背景图
	private Integer clean;//是否每天重置 0 不重置 1重置
	private float goldJifen;//消耗钻石获得积分比例
	private float bGoldJifen;//消耗绑定钻石获得积分比例
	
	private Object[] configVo;
	
	public Map<Integer, XkbzConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<Integer, XkbzConfig> configMap) {
		this.configMap = configMap;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getDes2() {
		return des2;
	}
	public void setDes2(String des2) {
		this.des2 = des2;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Integer getClean() {
		return clean;
	}
	public void setClean(Integer clean) {
		this.clean = clean;
	}
	public float getGoldJifen() {
		return goldJifen;
	}
	public void setGoldJifen(float goldJifen) {
		this.goldJifen = goldJifen;
	}
	public float getbGoldJifen() {
		return bGoldJifen;
	}
	public void setbGoldJifen(float bGoldJifen) {
		this.bGoldJifen = bGoldJifen;
	}
	public Object[] getConfigVo() {
		return configVo;
	}
	public void setConfigVo(Object[] configVo) {
		this.configVo = configVo;
	}



	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

}
