package com.junyou.bus.lingjing.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.lingjing.entity.LingJingShuXingCengConfig;
import com.junyou.bus.lingjing.entity.LingJingShuXingConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 灵境属性
 * @author LiuYu
 * 2015-6-29 上午10:03:10
 */
@Service
public class LingJingShuXingConfigServiceExport extends AbsClasspathConfigureParser {

	private String configureName = "LingJingShuXing.jat";
	
	private Map<Integer,LingJingShuXingCengConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,LingJingShuXingCengConfig> configs = new HashMap<>();
		Map<Integer,Map<String,Long>> attributes = new HashMap<>();
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				LingJingShuXingConfig config = createLingJingShuXingConfig(tmp);
				if(config == null){
					continue;
				}
				Map<String,Long> attribute = attributes.get(config.getRank());
				if(attribute == null){
					attribute = new HashMap<>();
					attributes.put(config.getRank(), attribute);
				}
				ObjectUtil.longMapAdd(attribute, config.getAttribute());
				
				LingJingShuXingCengConfig cengConfig = configs.get(config.getRank());
				if(cengConfig == null){
					cengConfig = new LingJingShuXingCengConfig();
					cengConfig.setRank(config.getRank());
					configs.put(config.getRank(), cengConfig);
				}
				cengConfig.setConfig(config);
			}
		}
		int i = 1;
		Map<String,Long> lastAttribute = null;
		for (; i < configs.size();) {
			Map<String,Long> attribute = attributes.get(i++);
			LingJingShuXingCengConfig cengConfig = configs.get(i);
			if(GameServerContext.getGameAppConfig().isDebug() && (attribute == null || cengConfig == null)){
				throw new RuntimeException("灵境属性解析时错误，请策划检查配置。");
			}
			if(lastAttribute != null){
				ObjectUtil.longMapAdd(attribute, lastAttribute);
			}
			lastAttribute = attribute;
			cengConfig.setAttribute(attribute);
		}
		//设置满灵境属性
		Map<String,Long> attribute = attributes.get(i++);
		if(lastAttribute != null){
			ObjectUtil.longMapAdd(attribute, lastAttribute);
		}
		LingJingShuXingCengConfig cengConfig = new LingJingShuXingCengConfig();
		cengConfig.setRank(i);
		cengConfig.setAttribute(attribute);
		configs.put(i, cengConfig);
		
		this.configs = configs;
	}
	
	private LingJingShuXingConfig createLingJingShuXingConfig(Map<String, Object> tmp){
		int rank = CovertObjectUtil.object2int(tmp.get("rank"));
		int type = CovertObjectUtil.object2int(tmp.get("type"));
		if(rank < 1 || type < 1){
			return null;
		}
		LingJingShuXingConfig config = new LingJingShuXingConfig();
		config.setRank(rank);
		config.setType(type);
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(attrs);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public LingJingShuXingCengConfig getCengConfig(int ceng){
		return configs.get(ceng);
	}

	/**
	 * 获取最大重数
	 * @return
	 */
	public int getMaxRank() {
		return configs.size() - 1;
	}

	
	
}
