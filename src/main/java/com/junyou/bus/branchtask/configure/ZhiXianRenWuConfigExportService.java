package com.junyou.bus.branchtask.configure;

 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 支线任务
 */
@Component
public class ZhiXianRenWuConfigExportService  extends AbsClasspathConfigureParser {
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhiXianRenWuBiao.jat";
	
	private Map<Integer,ZhiXianRenWuConfig> zhiXianRenWuConfigs = null;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		zhiXianRenWuConfigs = new HashMap<Integer, ZhiXianRenWuConfig>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhiXianRenWuConfig config = createRiChangJiangLiConfig(tmp);
				zhiXianRenWuConfigs.put(config.getId(), config);			
			}
		}
	}
	
	public ZhiXianRenWuConfig createRiChangJiangLiConfig(Map<String, Object> tmp) {
		ZhiXianRenWuConfig config = new ZhiXianRenWuConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setAwards(ConfigAnalysisUtils.cover2Map(tmp.get("items")+""));
		config.setDesc(CovertObjectUtil.object2String(tmp.get("desc")));
		config.setExp(CovertObjectUtil.object2int(tmp.get("exp")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		int conditionType =  CovertObjectUtil.object2int(tmp.get("opentype"));
		config.setOpenCondition(OpenConditionFactory.createOpenCondition(conditionType, tmp.get("opencondition")));
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setZhenQi(CovertObjectUtil.object2int(tmp.get("zhenqi")));
		if(BranchEnum.isNumberAdd(config.getType()) || BranchEnum.isNumberCompare(config.getType())){
			config.setTarget(CovertObjectUtil.object2int(tmp.get("target")));	
		}
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZhiXianRenWuConfig loadById(int id){
		return zhiXianRenWuConfigs.get(id);
	}
	
	public Map<Integer,ZhiXianRenWuConfig> loadAll(){
		return zhiXianRenWuConfigs;
	}
}