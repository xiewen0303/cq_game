package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 玄铁兑换表 
 *
 * @author ZHONGDIAN
 * @date 2015-06-01 10:18:23
 */
@Component
public class XuanTieDuiHuanConfigExportService extends AbsClasspathConfigureParser{
	
	private Map<Integer, XuanTieDuiHuanConfig> xuantieMap = new HashMap<>();
	
	/**
	  * configFileName
	 */
	private String configureName = "XuanTieDuiHuan.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				XuanTieDuiHuanConfig config = createXuanTieDuiHuanConfig(tmp);
								
				xuantieMap.put(config.getOrder(), config);
			}
		}
	}
	
	public XuanTieDuiHuanConfig createXuanTieDuiHuanConfig(Map<String, Object> tmp) {
		XuanTieDuiHuanConfig config = new XuanTieDuiHuanConfig();	
							
		config.setMaxcount(CovertObjectUtil.object2int(tmp.get("maxcount")));
											
		config.setNeedlevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
											
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
											
		config.setNeedjob(CovertObjectUtil.object2int(tmp.get("needjob")));
											
		config.setNeedxt(CovertObjectUtil.object2int(tmp.get("needxt")));
											
		config.setItemid(CovertObjectUtil.object2String(tmp.get("itemid")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public XuanTieDuiHuanConfig loadById(Integer id){
		return xuantieMap.get(id);
	}
}