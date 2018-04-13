package com.junyou.bus.equip.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 附属强化表
 * @author LiuYu
 * @date 2015-7-24 下午3:49:10
 */
@Component
public class FuShuQiangHuaBiaoConfigExportService extends AbsClasspathConfigureParser{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "FuShuQiangHuaBiao.jat";
	
	private Map<Integer,QiangHuaBiaoConfig>	 qhConfigs = null;
	
	int maxLevel =0;
	
	protected String getConfigureName() {
		return configureName;
	} 

	@Override
	protected void configureDataResolve(byte[] data) {
		qhConfigs = new HashMap<>();
		maxLevel = 0;
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				QiangHuaBiaoConfig  config=createQiangHuaConfig(tmp);
				qhConfigs.put(config.getLevel(), config);
				maxLevel = maxLevel< config.getLevel() ? config.getLevel() : maxLevel;
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
		config.setSuccessrate(10000);//必定成功
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		
		return config;
	}
	
	public QiangHuaBiaoConfig getQHConfig(int level) {
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
	
}