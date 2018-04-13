package com.junyou.gameconfig.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 
 *
 * @author LiuJuan
 * @date 2012-3-12 下午4:48:57
 */
@Component
public class PublicCdExportService extends AbsClasspathConfigureParser{

	
	/**
	 * 配置名
	 */
	private String configureName = "CdBiao.jat";
	
	private Map<String, PublicCdConfig> configs = new HashMap<String, PublicCdConfig>();
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj:dataList) {
			Map<String,Object> tmp = (Map<String,Object>)obj;
			if (null != tmp) {
				PublicCdConfig publicCdConfig = new PublicCdConfig();
				String cdId = CovertObjectUtil.object2String(tmp.get("cdid"));
				int cdTime = CovertObjectUtil.object2int(tmp.get("cdtime")); 
				publicCdConfig.setCdId(cdId);
				publicCdConfig.setCdTime(cdTime);
				configs.put(publicCdConfig.getCdId(), publicCdConfig);
			}
		}
	}

	protected String getConfigureName() {
		
		return configureName;
	}
	
	public PublicCdConfig load(String cdId) {
		
		return configs.get(cdId);
	}
}
