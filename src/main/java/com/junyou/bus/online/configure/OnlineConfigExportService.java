package com.junyou.bus.online.configure;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 在线奖励
 * @author jy
 *
 */
@Component
public class OnlineConfigExportService extends AbsClasspathConfigureParser {
	 
	
	/**
	  * configFileName TODO
	 */
	private String configureName = "ZaiXianJiangLi.jat";
	
	private Map<Integer,OnlineConfig> onlineConfigs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer,OnlineConfig> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				OnlineConfig config = createOnlineConfig(tmp);
				configs.put(config.getId(), config);
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.onlineConfigs = configs;
	}
	
	/**
	 * @param tmp
	 * @return
	 */
	public OnlineConfig createOnlineConfig(Map<String, Object> tmp) {
		OnlineConfig config = new OnlineConfig();
		Map<Byte,Float> weights = new HashMap<>();
		Map<Byte,Map<String,Integer>> itemRewards = new HashMap<>();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		for(byte i=1;i<=GameConstants.ROLE_ONLINE_VALUE;i++){
			String items = CovertObjectUtil.object2String(tmp.get("jiangitem"+i));
			if(StringUtils.isNotBlank(items)){
				Map<String,Integer> tmpRewards = new HashMap<String,Integer>();
				tmpRewards.put(items, CovertObjectUtil.object2int(tmp.get("count"+i)));
				itemRewards.put(i, tmpRewards);
				weights.put(i, CovertObjectUtil.object2Float(tmp.get("odds"+i)));
			}
		}
		if(weights.size()>0 && itemRewards.size()>0){
			config.setWeights(weights);
			config.setRewardItems(itemRewards);
		}
		return config;
	}
	
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public OnlineConfig loadById(Integer id){
		return onlineConfigs.get(id);
	}
	
}