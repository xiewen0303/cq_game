/**
 * 
 */
package com.junyou.configure.parser;

import javax.annotation.PostConstruct;

import com.junyou.configure.export.impl.ConfigureContext;
import com.junyou.configure.loader.DirType;
import com.junyou.configure.loader.IConfigureLoader;
import com.junyou.log.ChuanQiLog;
/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-8下午5:45:04
 */
public abstract class AbsConfigureParser {
	
	protected String sign;
	

	public String _getSign() {
		return sign;
	}

	/**
	 * 初始化
	 * @param
	 */
	@PostConstruct
	public void init() {
		
		try{
			Object[] signInfo = getLoader().loadSign(getConfigureName(),DirType.GLOBAL);
			if(signInfo == null){
				return;
			}
			
			sign = (String)signInfo[0];
			String path = (String)signInfo[1];
			
			byte[] data = getLoader().load(path);
			
			configureDataResolve(data);
			
		}catch(Exception e){
			ChuanQiLog.error(getConfigureName(), e);
			ChuanQiLog.error(getConfigureName()+" is null");
		}
		
	}
	
	/**
	 * 配置数据解析
	 * @param
	 */
	protected abstract void configureDataResolve(byte[] data);

	protected abstract String getConfigureName();
	
	/**
	 * 获取配置资源加载器
	 */
	protected abstract IConfigureLoader getLoader();
	
	/**
	 * 
	 * @param
	 */
	protected String getIdentity(){
		return ConfigureContext.CONFIGURE_IDENTITY.toString();
	}
	
	
}
