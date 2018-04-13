package com.junyou.bus.equip.configure.export;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import org.springframework.stereotype.Component; 
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @description 套装属性
 *
 * @author wind
 * @date 2015-01-10 15:09:16
 */
@Component
public class TaoZhuangBiaoConfigExportService extends AbsClasspathConfigureParser {
 
	/**
	  * configFileName
	 */
	private String configureName = "TaoZhuangBiao.jat"; 
	
	/**
	 * <id,<count,config>>
	 */
	private Map<Integer,TaoZhuangBiaoCategoryConfig> configs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<Integer,TaoZhuangBiaoCategoryConfig> configs = new HashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				
				TaoZhuangBiaoConfig config = createTaoZhuangBiaoConfig(tmp);
				TaoZhuangBiaoCategoryConfig categoryConfig = configs.get(config.getId());
				if(categoryConfig == null){
					categoryConfig = new TaoZhuangBiaoCategoryConfig();
					configs.put(config.getId(), categoryConfig);
				}
				categoryConfig.addConfig(config);
			}
		}
		initConfig(configs);
		this.configs = configs;
	}
	
	private void initConfig(Map<Integer,TaoZhuangBiaoCategoryConfig> configs){
		for (TaoZhuangBiaoCategoryConfig config : configs.values()) {
			config.init();
		}
	}
	
	public TaoZhuangBiaoConfig createTaoZhuangBiaoConfig(Map<String, Object> tmp) {
		 
		
		TaoZhuangBiaoConfig config =new TaoZhuangBiaoConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		 
		int count = CovertObjectUtil.object2int(tmp.get("count"));
		config.setCount(count);
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		
		config.setAttrs(attrs);
		
		return config;
	}
	
	 
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 获得对应的属性
	 * @param id
	 * @param count
	 * @return  结果可以为null 表示配置有问题
	 */
	public Map<String,Long> getAttrs(Integer id,int count){
		
		TaoZhuangBiaoCategoryConfig categoryConfig = configs.get(id);
		if(categoryConfig == null){
			ChuanQiLog.error("没有找到套装配置Id:"+id);
			return null;
		}
		
		TaoZhuangBiaoConfig config = categoryConfig.getConfigByCount(count);
		if(config != null){
			return config.getAttrs();
		}
		 
		return null;
	}
	
}