package com.junyou.bus.giftcard.configure;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 礼包卡
 * @author jy
 *
 */
@Component
public class LiBaoConfigExportService extends AbsClasspathConfigureParser {
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "LiBao.jat";
	
	private Map<String,LiBaoConfig> libaoConfigs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<String,LiBaoConfig> configs = new HashMap<String, LiBaoConfig>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				LiBaoConfig config = createLibaoConfig(tmp);
				configs.put(config.getId(), config);
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.libaoConfigs = configs;
	}
	
	/**
	 * @param tmp
	 * @return
	 */
	public LiBaoConfig createLibaoConfig(Map<String, Object> tmp) {
		LiBaoConfig config = new LiBaoConfig();
		config.setId(CovertObjectUtil.object2String(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		StringBuilder sb = new StringBuilder();
		for(int i=1;i<=10;i++){
			String item = CovertObjectUtil.object2String(tmp.get("item"+i));
			if(StringUtils.isNotBlank(item)) sb.append(item).append("|");
		}
		String tmpStr = sb.deleteCharAt(sb.length()-1).toString();
		Map<String,Integer> itemRewards = analysis2Map(tmpStr);
		config.setItems(new ReadOnlyMap<>(itemRewards));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
		config.setBindgold(CovertObjectUtil.object2int(tmp.get("bindgold")));
		return config;
	}
	
	private Map<String,Integer> analysis2Map(String itemsIdStr){
		Map<String,Integer> awards =null;
		if(itemsIdStr != null &&!"".equals(itemsIdStr)){
			String[] items = itemsIdStr.split("\\|");
			awards = new HashMap<>();
			for (String itemsStr : items) {
				String[] datas = itemsStr.split(":");
				awards.put(datas[0],CovertObjectUtil.object2int(datas[1]));
			}
		}
		return awards;
	}
	
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public LiBaoConfig loadById(String id){
		return libaoConfigs.get(id);
	}
	
}