package com.junyou.bus.equip.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 神器格位
 *
 * @author chenxiaobing
 * @date 2016-012-5
 */
@Component
public class ShenQiGeWeiConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer,List<ShenQiGeWeiConfig>> configs = new HashMap<Integer,List<ShenQiGeWeiConfig>>();
	
	/**
	  * configFileName
	 */
	private String configureName = "ShenQiGeWei.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShenQiGeWeiConfig config = createShenQiGeWeiConfig(tmp);
				if(configs.containsKey(config.getId())){
					configs.get(config.getId()).add(config);
				}else{
					List<ShenQiGeWeiConfig> list = new ArrayList();
					list.add(config);
					configs.put(config.getId(), list);
				}
			}
		}
	}
	
	public ShenQiGeWeiConfig createShenQiGeWeiConfig(Map<String, Object> tmp) {
		ShenQiGeWeiConfig config = new ShenQiGeWeiConfig();	
							
		config.setId(CovertObjectUtil.object2Integer(tmp.get("id")));
											
		config.setData(CovertObjectUtil.object2Integer(tmp.get("data")));
											
		config.setGeWei(CovertObjectUtil.object2Integer(tmp.get("gewei")));
		
		config.setTiaojian(CovertObjectUtil.object2Integer(tmp.get("tiaojian")));
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public List<ShenQiGeWeiConfig> loadById(Integer id){
		return configs.get(id);
	}
}