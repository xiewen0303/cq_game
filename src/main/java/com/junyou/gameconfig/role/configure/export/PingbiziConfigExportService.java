package com.junyou.gameconfig.role.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.log.ChuanQiLog;

/**
 * 游戏屏蔽字库解析
 * @author DaoZheng Yuan
 * 2015年2月28日 下午6:18:19
 */
@Component
public class PingbiziConfigExportService extends AbsClasspathConfigureParser{

	
	private Map<String,String> PBZ_MAP;
	
	/**
	  * 配置文件名字
	 */
	private String configName = "bs.txt";
	
	@Override
	protected String getConfigureName() {
		return configName;
	}
	
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		Map<String,String> PBZ_MAP = new HashMap<String, String>();
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configName, data);
		
		String regexChar = "\\s*\r|\n";
		String pbzs = new String(data);
		String[] tmp = pbzs.split(regexChar);
		try {
			for (int i = 0; i < tmp.length; i++) {
				String tmpChar = tmp[i].trim();
				if(!"".equals(tmpChar)){
					PBZ_MAP.put(tmpChar, null);
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("PingbiziConfigExportService err !");
		}
		this.PBZ_MAP = PBZ_MAP;
	}

	
	/**
	 * 验证是否在屏蔽字库内
	 * @param strChar
	 * @return true:是屏蔽字,false非屏蔽字
	 */
	public boolean checkPingbizi(String strChar){
		if(PBZ_MAP==null){
			return  false;
		}
		if(strChar == null || "".equals(strChar)){
			return false;
		}
		
		return PBZ_MAP.containsKey(strChar);
	}
	
}
