package com.junyou.bus.chibang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 坐骑等级属性 
 *
 * @author wind
 * @date 2015-04-01 17:50:13
 */
@Component
public class ChiBangShuXingConfigExportService{// extends AbsClasspathConfigureParser {
	
	private int beginLevel;
	
	private Map<Integer,int[]> jdLevels = new HashMap<Integer, int[]>();

	private Map<String,ChiBangShuXingConfig> yujianConfigs = new HashMap<>();
	
	private Map<Integer,Integer> levelJDs = new HashMap<Integer, Integer>();
	
	/**
	  * configFileName
	 */
	private String configureName = "ChiBangShuXingBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		return;
//		Object[] dataList = GameConfigUtil.getResource(data);
//		for (Object obj : dataList) {
//			Map<String, Object> tmp = (Map<String, Object>)obj;
//			if (null != tmp) {
//				ChiBangShuXingConfig config = createYuJianShuXingConfig(tmp);
//								
//				String key = getKey(config.getId(), config.getLevel());
//				yujianConfigs.put(key, config);
//				
//				initJjLevels(config);
//				
//				if(beginLevel > config.getLevel()){
//					beginLevel = config.getLevel();
//				}
//			}
//		}
	}
	
	private void initJjLevels(ChiBangShuXingConfig config){
		int jj = config.getId();
		int[] datas = null;
		if((datas = jdLevels.get(jj)) == null){
			datas = new int[]{config.getLevel(),config.getLevel()};
			jdLevels.put(jj, datas);
		}
		int level  = config.getLevel();
		if(level <datas[0]){
			datas[0]= level;
		}
		if(level >datas[1]){
			datas[1]=level;
		}
		levelJDs.put(config.getLevel(),config.getId());
	}
	
	public ChiBangShuXingConfig getYuJianShuXingConfig(int jj,int dj) {
		String key = getRealKey(jj, dj);
		return	yujianConfigs.get(key);
	}
	
	public String getRealKey(Integer jj,int dj){
		
		int[]  beLevel = jdLevels.get(jj);
		if(beLevel == null) return null;
		
		int beginLevel = beLevel[0]; 
		int endLevel = beLevel[1];
		
		if(dj > endLevel){
			dj = endLevel;
		}
		
		if(dj < beginLevel){
			jj = levelJDs.get(dj);
			if(jj == null){
				return null;
			}
		}
		
		return getKey(jj, dj);
	}
	
	
	
	
	
	/**
	 * @param jj 阶级
	 * @param dj 等级
	 * @return
	 */
	private String getKey(int jj,int dj){
		return jj+"_"+dj;
	} 
	
	public ChiBangShuXingConfig createYuJianShuXingConfig(Map<String, Object> tmp) {
		ChiBangShuXingConfig config = new ChiBangShuXingConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setDatas(attrs);
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public int getBeginLevel(){
		return beginLevel;
	}
	 
}