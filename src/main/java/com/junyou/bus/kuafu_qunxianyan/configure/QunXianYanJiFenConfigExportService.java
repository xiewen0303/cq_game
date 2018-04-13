package com.junyou.bus.kuafu_qunxianyan.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 群仙宴积分表 
 *
 * @author ZHONGDIAN
 * @date 2016-03-28 15:57:01
 */
@Component
public class QunXianYanJiFenConfigExportService extends AbsClasspathConfigureParser  {
	
	private Map<Integer, QunXianYanJiFenConfig> configs = null;
	
	/**
	  * configFileName
	 */
	private String configureName = "QunXianYanJiFen.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		
		configs = new HashMap<Integer, QunXianYanJiFenConfig>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				QunXianYanJiFenConfig config = createQunXianYanJiFenConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public QunXianYanJiFenConfig createQunXianYanJiFenConfig(Map<String, Object> tmp) {
		QunXianYanJiFenConfig config = new QunXianYanJiFenConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setGonggao1(CovertObjectUtil.object2int(tmp.get("gonggao1")));
											
		config.setZiYuanId(CovertObjectUtil.object2String(tmp.get("ziyuanid")));
											
		config.setGonggao(CovertObjectUtil.object2int(tmp.get("gonggao")));
											
		config.setJifen(CovertObjectUtil.object2int(tmp.get("jiangjf")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public QunXianYanJiFenConfig getConfigByZiYuanId(String zyId){
		for (Integer id : configs.keySet()) {
			QunXianYanJiFenConfig config = configs.get(id);
			if(zyId.equals(config.getZiYuanId())){
				return config;
			}
		}
		return null;
	}
	
	public QunXianYanJiFenConfig loadById(Integer id){
		return configs.get(id);
	}
}