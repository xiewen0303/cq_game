/**
 * 
 */
package com.junyou.configure.parser.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.configure.loader.ClasspathConfigureLoader;
import com.junyou.configure.loader.DirType;
import com.junyou.configure.loader.IConfigureLoader;
import com.junyou.configure.parser.AbsConfigureParser;



/**
 * @description 配置文件组配置
 * @author ShiJie Chi
 * @created 2011-12-8下午5:45:04
 */
public abstract class AbsGroupFileAbleConfigureInit extends AbsConfigureParser {
	
	@Autowired
	private ClasspathConfigureLoader configureLoader;
	
	/**
	 * 初始化
	 * @param
	 */
	public void init() {
		String[] configNameGroup  = getGroupConfigureNames();
		
		if(configNameGroup != null && configNameGroup.length > 0){
			for (String configName : configNameGroup) {
				Object[] signInfo = getLoader().loadSign(configName,DirType.GLOBAL);
				sign = (String)signInfo[0];
				String path = (String)signInfo[1];
				
				byte[] data = getLoader().load(path);
				
				configureDataResolve(data,configName);
			}
		}

	}
	
	public void configureDataResolve(byte[] data){
		
	}
	
	/**
	 * 配置数据解析
	 * @param
	 */
	protected abstract void configureDataResolve(byte[] data,String configName);
	
	
	protected String getConfigureName() {
		
		return null;
	}
	
	@Override
	protected IConfigureLoader getLoader() {
		return configureLoader;
	}
	
	protected abstract String[] getGroupConfigureNames();
}
