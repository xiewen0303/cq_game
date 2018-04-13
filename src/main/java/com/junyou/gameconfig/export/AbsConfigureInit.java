/**
 * 
 */
package com.junyou.gameconfig.export;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.configure.export.impl.ConfigureContext;
import com.junyou.configure.loader.DirType;
import com.junyou.configure.loader.IConfigureLoader;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-8下午5:45:04
 */
public abstract class AbsConfigureInit {
	
	protected String sign;
	
	private IConfigureLoader loader;
	
	
	@SuppressWarnings("unused")
	private ConfigureContext configureContext;
	
	@Autowired
	public void setConfigureContext(ConfigureContext configureContext) {
		this.configureContext = configureContext;
	}

	public void setLoader(IConfigureLoader loader) {
		this.loader = loader;
	}

	public IConfigureLoader getLoader() {
		return loader;
	}
	
	public String _getSign() {
		return sign;
	}

	/**
	 * 初始化
	 * @param
	 */
	public void init() {
		
		Object[] signInfo = getLoader().loadSign(getConfigureName(),DirType.GLOBAL);
		if(signInfo == null){
			return;
		}
		
		sign = (String)signInfo[0];
		String path = (String)signInfo[1];
		
		byte[] data = getLoader().load(path);
		
		configureDataResolve(data);
	}
	
	protected abstract String getConfigureName();
	
	/**
	 * 配置数据解析
	 * @param
	 */
	protected abstract void configureDataResolve(byte[] data);

	/**
	 * 
	 * @param
	 */
	protected String getIdentity(){
		return ConfigureContext.CONFIGURE_IDENTITY.toString();
	}
	
}
