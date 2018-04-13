package com.junyou.bus.xiuxian.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修仙礼包组
 * @author DaoZheng Yuan
 * 2015年6月7日 下午2:48:16
 */
public class XiuXianGroupConfig {

	//用于存放背景图
	private String bgImageName;
	
	//购买数据
	private Map<Integer,XiuXianConfig> configs;
	
	//有序的礼包类型
	private List<Integer> lbIds;
	
	//文件MD5值
	private String md5Version;;
	
	
	public String getBgImageName() {
		return bgImageName;
	}

	public void setBgImageName(String bgImageName) {
		this.bgImageName = bgImageName;
	}

	public Map<Integer, XiuXianConfig> getConfigs() {
		return configs;
	}
	
	public XiuXianConfig getXiuXianConfigById(int id){
		if(configs == null){
			return null;
		}
		
		return configs.get(id);
	}

	/**
	 * 加入数据
	 * @param config
	 */
	public void addXiuXianConfig(XiuXianConfig config){
		if(configs == null){
			configs = new HashMap<>();
		}
		configs.put(config.getId(), config);
	}
	
	public List<Integer> getLbIds() {
		return lbIds;
	}

	/**
	 * 加入id
	 * @param id
	 */
	public void addLbIds(Integer id) {
		if(lbIds == null){
			lbIds = new ArrayList<>();
		}
		
		if(!lbIds.contains(id)){
			lbIds.add(id);
		}
	}

	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	
}
