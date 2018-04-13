package com.junyou.bus.lianyuboss.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-11-2 下午2:29:27
 */
@Service
public class LianyuBossConfigExportService extends AbsClasspathConfigureParser {
	private String configureName = "MenPaiBossPeiZhi.jat";
	private Map<Integer,LianyuBossConfig> configs;
	private int maxCeng;
	
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		  configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				LianyuBossConfig config = createLianyuBossConfig(tmp);
				configs.put(config.getConfigId(), config);
				 
			}
		}
	}

	public int getMaxCeng() {
		return maxCeng;
	}
	public void setMaxCeng(int maxCeng) {
		this.maxCeng = maxCeng;
	}
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private LianyuBossConfig createLianyuBossConfig(Map<String, Object> tmp){
		LianyuBossConfig config = new LianyuBossConfig();
		config.setConfigId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setId(config.getConfigId());
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		String jiangItemStr = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		Map<String, Integer> jiangItem = ConfigAnalysisUtils.getConfigMap(jiangItemStr);
		config.setRewards(jiangItem);
		String monster = CovertObjectUtil.object2String(tmp.get("bossid"));
		Map<String,Integer> map = new HashMap<>();
		map.put(monster, 1);
		config.setWantedMap(map);
		
		if(config.getConfigId()>maxCeng){
			maxCeng = config.getConfigId();
		}
		return  config;
		
	}
	
	public LianyuBossConfig loadById(int configId){
		return configs.get(configId);
	}
	
}
