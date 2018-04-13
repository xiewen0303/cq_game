package com.junyou.gameconfig.goods.configure.export;

 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 功能分布配置(后台某个功能关闭时,通知客户端也关掉这个功能模块)
 * @author DaoZheng Yuan
 * 2015年1月31日 下午4:47:55
 */
@Component
public class GongNengFenBuConfigExportService  extends AbsClasspathConfigureParser{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "GongNengFenBu.jat";
	
	private Map<String,Integer> MOD_IDS = new HashMap<>();
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 根据模块名获取功能分布表的模块id
	 * @param modName
	 * @return
	 */
	public Integer getModIdByModName(String modName){
		return MOD_IDS.get(modName);
	}
	
	

	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				MOD_IDS.put(CovertObjectUtil.object2String(tmp.get("id")),CovertObjectUtil.object2int(tmp.get("serverid")));
			}
		} 
	}
}