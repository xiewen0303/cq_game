package com.junyou.bus.equip.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 道具类型排序表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:43:38
  *@Description:
 */
@Component
public class QiangHuaBiaoConfigExportService extends AbsClasspathConfigureParser{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "QiangHuaBiao.jat";
	
	private Map<Integer,QiangHuaBiaoConfig>	 qhConfigs = null;
	
//	private Map<Integer,Map<Integer, QiangHuaBiaoConfig>> qiangHuaBiaos = null;
	
//	部位=MaxLevel
//	private Map<Integer,Integer> postitionMaxLevels = null;
	int maxLevel =0;
	
	protected String getConfigureName() {
		return configureName;
	} 

	@Override
	protected void configureDataResolve(byte[] data) {
		qhConfigs = new HashMap<>();
		maxLevel = 0;
//		qiangHuaBiaos = new HashMap<Integer,Map<Integer,QiangHuaBiaoConfig>>();
//		postitionMaxLevels = new HashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				QiangHuaBiaoConfig  config=createQiangHuaConfig(tmp);
				qhConfigs.put(config.getLevel(), config);
//				Map<Integer, QiangHuaBiaoConfig> levelQHConfigs = qiangHuaBiaos.get(config.getPosition());
//				if(levelQHConfigs == null){
//					levelQHConfigs=new HashMap<Integer, QiangHuaBiaoConfig>();
//					qiangHuaBiaos.put(config.getPosition(),levelQHConfigs);
//				}
				
//				levelQHConfigs.put(config.getLevel(), config);
				
				maxLevel = maxLevel< config.getLevel() ? config.getLevel() : maxLevel;
				
//				postitionMaxLevels.put(config.getPosition(), maxLevel<config.getLevel()?config.getLevel():maxLevel);
			}
		}
	}
	
	private QiangHuaBiaoConfig createQiangHuaConfig(Map<String, Object> tmp){
		QiangHuaBiaoConfig config=new QiangHuaBiaoConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level"))); 
		config.setNeedItemCount(CovertObjectUtil.object2int(tmp.get("num")));
		config.setNeedItemId(CovertObjectUtil.object2String(tmp.get("prop")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("money")));
		config.setQhxs(CovertObjectUtil.object2Float(tmp.get("qhxs")));
		config.setSuccessrate(CovertObjectUtil.object2int(tmp.get("successrate")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		
		return config;
	}
	
	public QiangHuaBiaoConfig getQHConfig(int level) {
//		Map<Integer, QiangHuaBiaoConfig> levelQHConfigs = qiangHuaBiaos.get(position);
//		if(levelQHConfigs == null) {
//			return null;
//		}
//		return levelQHConfigs.get(level);
		return qhConfigs.get(level);
	}
	
	public int getMaxLevel(){
		return maxLevel;
	}
	
    public Float getQhxs(int qhLevel) {
        QiangHuaBiaoConfig config = getQHConfig(qhLevel);
        if (null == config)
            return null;
        return config.getQhxs();
    }
	
	
	
//	public int getMaxLevel(int position) {
//		return postitionMaxLevels.get(position)==null?0:postitionMaxLevels.get(position);
//	}
}