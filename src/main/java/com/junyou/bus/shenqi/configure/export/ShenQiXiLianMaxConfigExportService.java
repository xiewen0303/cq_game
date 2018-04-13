package com.junyou.bus.shenqi.configure.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 神器洗练上限值表数据
 * @author lxn
 *
 */
@Component
public class ShenQiXiLianMaxConfigExportService extends AbsClasspathConfigureParser { 
	
	private Map<Integer, Map<String, Long>> attrMaxMap;
	/**
	 * configFileName
	 */
	private String configureName = "ShenQiXiLian.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		ConfigMd5SignManange.addConfigSign(configureName, data); 
		Object[] dataList = GameConfigUtil.getResource(data);
		attrMaxMap  = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				createShenQiShuXingConfig(tmp);
			}
		}
	}
	public void createShenQiShuXingConfig(Map<String, Object> tmp) {
		Map<String, Long> attrMap  = new HashMap<>();
		for (Entry<String, Object>  entry : tmp.entrySet()) {
			String str  = (String)entry.getKey();
			if(str.contains("x")){
				long value = CovertObjectUtil.object2Long(tmp.get(str));
				attrMap.put(str, value);
			}
			
		}
		int id =CovertObjectUtil.object2int(tmp.get("id"));
		attrMaxMap.put(id, new ReadOnlyMap<>(attrMap));
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 根据id获得配置
	 * @param id
	 * @return
	 */
	public Map<String, Long> loadById(int id) {
		return this.attrMaxMap.get(id);
	}

	/**
	 * 获得全部的配置
	 * @return
	 */
	public Map<Integer, Map<String, Long>> getAll() {
		return this.attrMaxMap;
	}

}
