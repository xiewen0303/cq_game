package com.junyou.bus.bag.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 *  多选宝箱配置表 
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-4-23 下午6:42:03
 */
@Component
public class DuoXuanLiXiangBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer, DuoXuanLiXiangBiaoConfig> config ;
	/**
	  * configFileName
	 */
	private String configureName = "DuoXuanLiXiangBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer, DuoXuanLiXiangBiaoConfig> tmpConfig = new HashMap<>(); 
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DuoXuanLiXiangBiaoConfig config = createDuoXuanLiXiangBiaoConfig(tmp);
								
				if(config != null){
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		this.config = tmpConfig;
	}
	
	public DuoXuanLiXiangBiaoConfig createDuoXuanLiXiangBiaoConfig(Map<String, Object> tmp) {
		DuoXuanLiXiangBiaoConfig config = new DuoXuanLiXiangBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		config.setPaixu1(CovertObjectUtil.object2int(tmp.get("paixu1")));
		config.setPaixu2(CovertObjectUtil.object2int(tmp.get("paixu2")));
		config.setPaixu3(CovertObjectUtil.object2int(tmp.get("paixu3")));
		config.setPaixu4(CovertObjectUtil.object2int(tmp.get("paixu4")));
		
		Map<Integer, Integer> moneyTypeMap = new HashMap<>();
		Map<Integer, Integer> goldMap = new HashMap<>();
		Map<Integer, Map<String, Integer>> tmpMap = new HashMap<>();
		String item1 = CovertObjectUtil.object2String(tmp.get("item1"));
		if(!CovertObjectUtil.isEmpty(item1)){
			Map<String,Integer> map = ConfigAnalysisUtils.getConfigMap(item1);
			tmpMap.put(config.getPaixu1(), map);
			moneyTypeMap.put(config.getPaixu1(), CovertObjectUtil.object2Integer(tmp.get("moneytype1")));
			goldMap.put(config.getPaixu1(), CovertObjectUtil.object2int(tmp.get("gold1")));
		}		
			
		
		String item2 = CovertObjectUtil.object2String(tmp.get("item2"));
		if(!CovertObjectUtil.isEmpty(item2)){
			Map<String,Integer> map = ConfigAnalysisUtils.getConfigMap(item2);
			tmpMap.put(config.getPaixu2(), map);
			moneyTypeMap.put(config.getPaixu2(), CovertObjectUtil.object2Integer(tmp.get("moneytype2")));
			goldMap.put(config.getPaixu2(), CovertObjectUtil.object2int(tmp.get("gold2")));
		}	
			
		String item3 = CovertObjectUtil.object2String(tmp.get("item3"));
		if(!CovertObjectUtil.isEmpty(item3)){
			Map<String,Integer> map = ConfigAnalysisUtils.getConfigMap(item3);
			tmpMap.put(config.getPaixu3(), map);
			moneyTypeMap.put(config.getPaixu3(), CovertObjectUtil.object2Integer(tmp.get("moneytype3")));
			goldMap.put(config.getPaixu3(), CovertObjectUtil.object2int(tmp.get("gold3")));
		}
			
		String item4 = CovertObjectUtil.object2String(tmp.get("item4"));
		if(!CovertObjectUtil.isEmpty(item4)){
			Map<String,Integer> map = ConfigAnalysisUtils.getConfigMap(item4);
			tmpMap.put(config.getPaixu4(), map);
			moneyTypeMap.put(config.getPaixu4(), CovertObjectUtil.object2Integer(tmp.get("moneytype4")));
			goldMap.put(config.getPaixu4(), CovertObjectUtil.object2int(tmp.get("gold4")));
		}
		config.setGoldMap(goldMap);
		config.setMoneytypeMap(moneyTypeMap);
		config.setConfigMap(tmpMap);
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public DuoXuanLiXiangBiaoConfig loadById(Integer id){
		return config.get(id);
	}
	
}