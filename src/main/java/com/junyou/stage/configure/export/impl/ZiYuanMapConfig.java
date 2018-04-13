package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.List;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 地图上的所有非战斗资源配置表 
 *
 * @author DaoZheng Yuan
 * @date 2013-10-26 16:52:02
 */
public class ZiYuanMapConfig extends AbsVersion{
	
	private String mapId;

	private List<ZiYuanConfig> ziYuans;
	
	private List<ZiYuanConfig> hangZiyuans;//矿战中行会矿资源
	
	private List<ZiYuanConfig> collectZiyuans;//矿战中普通采集矿资源
	
	private ZiYuanConfig bigCrystal;//群雄逐鹿中大水晶资源
	private ZiYuanConfig smallCrystal;//群雄逐鹿中小水晶资源
	
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public List<ZiYuanConfig> getZiYuans() {
		return ziYuans;
	}

	public void addZiYuan(ZiYuanConfig ziYuanConfig){
		if(ziYuans == null){
			ziYuans = new ArrayList<ZiYuanConfig>();
		}
		
		ziYuans.add(ziYuanConfig);
		
//		//矿战中行会矿资源
//		addHhKuang(ziYuanConfig);
//		//矿战中普通采集矿资源
//		addCollectKuang(ziYuanConfig);
//		
//		addBigCrystal(ziYuanConfig);
//		addSmallCrystal(ziYuanConfig);
	}
	
	/**
	 * 群雄逐鹿中大水晶资源
	 * @param ziYuanConfig
	 */
	private void addBigCrystal(ZiYuanConfig ziYuanConfig){
		if(ziYuanConfig.getType() == 5){
			bigCrystal = ziYuanConfig;
		}
	}
	
	/**
	 * 群雄逐鹿中小水晶资源
	 * @param ziYuanConfig
	 */
	private void addSmallCrystal(ZiYuanConfig ziYuanConfig){
		if(ziYuanConfig.getType() == 6){
			smallCrystal = ziYuanConfig;
		}
	}
	
	/**
	 * 矿战中行会矿资源
	 * @param ziYuanConfig
	 */
	private void addHhKuang(ZiYuanConfig ziYuanConfig){
		if(ziYuanConfig.getType() == 4){
			if(hangZiyuans == null){
				hangZiyuans = new ArrayList<ZiYuanConfig>();
			}
			
			if(!hangZiyuans.contains(ziYuanConfig)){
				hangZiyuans.add(ziYuanConfig);
			}
		}
	}
	
	/**
	 * 矿战中普通采集矿资源
	 * @param ziYuanConfig
	 */
	private void addCollectKuang(ZiYuanConfig ziYuanConfig){
		if(ziYuanConfig.getType() == 3){
			if(collectZiyuans == null){
				collectZiyuans = new ArrayList<ZiYuanConfig>();
			}
			
			if(!collectZiyuans.contains(ziYuanConfig)){
				collectZiyuans.add(ziYuanConfig);
			}
		}
	}
	
	
	/**
	 * 获取矿战中行会矿资源
	 * @return
	 */
	public List<ZiYuanConfig> getHangZiyuans() {
		return hangZiyuans;
	}

	/**
	 * 获取矿战中普通采集矿资源
	 * @return
	 */
	public List<ZiYuanConfig> getCollectZiyuans() {
		return collectZiyuans;
	}
	
	/**
	 * 群雄逐鹿中大水晶资源
	 * @return
	 */
	public ZiYuanConfig getBigCrystal() {
		return bigCrystal;
	}

	/**
	 * 群雄逐鹿中小水晶资源
	 * @return
	 */
	public ZiYuanConfig getSmallCrystal() {
		return smallCrystal;
	}

}
