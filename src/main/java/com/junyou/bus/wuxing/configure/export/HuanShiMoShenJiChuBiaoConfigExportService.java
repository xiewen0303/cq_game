package com.junyou.bus.wuxing.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 五行配置表 
 *
 * @author ZHONGDIAN
 * @date 2016-04-11 15:28:13
 */
@Component
public class HuanShiMoShenJiChuBiaoConfigExportService extends AbsClasspathConfigureParser{
	
	private Map<Integer, HuanShiMoShenJiChuBiaoConfig> configs = new HashMap<Integer, HuanShiMoShenJiChuBiaoConfig>();
	/**
	  * configFileName
	 */
	private String configureName = "HuanShiMoShenJiChuBiao.jat";
	/**
	 * 最大等级
	 */
	private int maxLevel;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
	    if(null == data){
            ChuanQiLog.error("{} not found", getConfigureName());
            return ;
        }
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				HuanShiMoShenJiChuBiaoConfig config = createHuanShiMoShenJiChuBiaoConfig(tmp);
								
				configs.put(config.getId(),config);
				maxLevel = maxLevel < config.getLevel()?config.getLevel():maxLevel; 
			}
		}
	}
	
	public HuanShiMoShenJiChuBiaoConfig createHuanShiMoShenJiChuBiaoConfig(Map<String, Object> tmp) {
		HuanShiMoShenJiChuBiaoConfig config = new HuanShiMoShenJiChuBiaoConfig();	
				
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
											
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setZfzmin(CovertObjectUtil.object2int(tmp.get("zfzmin")));
											
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
											
		config.setCztime(CovertObjectUtil.obj2float(tmp.get("cztime")));
											
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
											
		config.setGgopen(CovertObjectUtil.object2int(tmp.get("ggopen"))  == 1);
											
		config.setLevel(CovertObjectUtil.object2int(tmp.get("dengjie")));
											
		config.setZfzmin3(CovertObjectUtil.object2int(tmp.get("zfzmin3")));
											
		config.setZfzmin2(CovertObjectUtil.object2int(tmp.get("zfzmin2")));
											
		config.setItem(CovertObjectUtil.object2String(tmp.get("id1")));
											
		config.setZfztime(CovertObjectUtil.object2int(tmp.get("zfztime")) == 1);
											
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
											
		config.setPro(CovertObjectUtil.object2int(tmp.get("pro")));
											
		config.setZfzmax(CovertObjectUtil.object2int(tmp.get("zfzmax")));
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		/*Map<String, Long> futiAttrs = new HashMap<>();
		if(attrs != null){
			if(attrs.get(EffectType.x64.name()) != null){
				long x64AttrVal = attrs.remove(EffectType.x64.name());
				futiAttrs.put(EffectType.x64.name(), x64AttrVal);
			}
			if(attrs.get(EffectType.x65.name()) != null){
				long x65AttrVal = attrs.remove(EffectType.x65.name());
				futiAttrs.put(EffectType.x65.name(), x65AttrVal);
			}
			if(attrs.get(EffectType.x66.name()) != null){
				long x66AttrVal = attrs.remove(EffectType.x66.name());
				futiAttrs.put(EffectType.x66.name(), x66AttrVal);
			}
			if(attrs.get(EffectType.x67.name()) != null){
				long x67AttrVal = attrs.remove(EffectType.x67.name());
				futiAttrs.put(EffectType.x67.name(), x67AttrVal);
			}
			if(attrs.get(EffectType.x68.name()) != null){
				long x68AttrVal = attrs.remove(EffectType.x68.name());
				futiAttrs.put(EffectType.x68.name(), x68AttrVal);
			}
			if(attrs.get(EffectType.x69.name()) != null){
				long x69AttrVal = attrs.remove(EffectType.x69.name());
				futiAttrs.put(EffectType.x69.name(), x69AttrVal);
			}
			if(attrs.get(EffectType.x70.name()) != null){
				long x70AttrVal = attrs.remove(EffectType.x70.name());
				futiAttrs.put(EffectType.x70.name(), x70AttrVal);
			}
			if(attrs.get(EffectType.x71.name()) != null){
				long x71AttrVal = attrs.remove(EffectType.x71.name());
				futiAttrs.put(EffectType.x71.name(), x71AttrVal);
			}
			if(attrs.get(EffectType.x72.name()) != null){
				long x72AttrVal = attrs.remove(EffectType.x72.name());
				futiAttrs.put(EffectType.x72.name(), x72AttrVal);
			}
			if(attrs.get(EffectType.x73.name()) != null){
				long x73AttrVal = attrs.remove(EffectType.x73.name());
				futiAttrs.put(EffectType.x73.name(), x73AttrVal);
			}
		}
		
		config.setFutiAttrs(futiAttrs);*/
		config.setAttrs(new ReadOnlyMap<>(attrs));
		
		return config;
	}
	
	public List<String> getConsumeIds(String id1) {
		List<String> result = new ArrayList<>();
		List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
		result.addAll(ids);
		return result; 
	}
	/**
	 * 根据类型和等级获取配置
	 * @param type
	 * @param level
	 * @return
	 */
	public HuanShiMoShenJiChuBiaoConfig getWuXingConfigByTypeAndLevel(Integer type,Integer level){
		for (Integer id : configs.keySet()) {
			HuanShiMoShenJiChuBiaoConfig config = configs.get(id);
			if(config.getType() == type.intValue() && config.getLevel() == level.intValue()){
				return config;
			}
			
		}
		return null;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public HuanShiMoShenJiChuBiaoConfig loadById(Integer id){
		return configs.get(id);
	}
	
	public int getMaxJjLevel(){
		return maxLevel;
	}
}