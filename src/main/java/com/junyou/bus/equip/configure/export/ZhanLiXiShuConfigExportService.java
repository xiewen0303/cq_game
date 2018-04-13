package com.junyou.bus.equip.configure.export;
 
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 随机属性表 
 *
 * @author wind
 * @date 2015-01-10 15:09:16
 */
@Component
public class ZhanLiXiShuConfigExportService extends AbsClasspathConfigureParser {
 
	/**
	  * configFileName
	 */
	private String configureName = "ZhanLiXiShu.jat";
	
	private Map<String,Float> datas = null;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<String,Float> dataTemps = new ConcurrentHashMap<>(); 
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				String key = CovertObjectUtil.object2String(tmp.get("xpro"));
				Float value = CovertObjectUtil.object2Float(tmp.get("data"));
				dataTemps.put(key, value);			 
			}
		}
		
		datas = dataTemps;
	} 
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 获取战力系数
	 * @param key
	 * @return
	 */
	public Float getZLXS(String key){
		return datas.get(key);
	}
}