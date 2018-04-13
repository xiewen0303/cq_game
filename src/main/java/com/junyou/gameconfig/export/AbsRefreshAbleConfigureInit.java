/**
 * 
 */
package com.junyou.gameconfig.export;

import com.junyou.configure.loader.DirType;
import com.junyou.log.ChuanQiLog;


/**
 * @description 可刷新配置
 * @author ShiJie Chi
 * @created 2011-12-8下午5:45:04
 */
public abstract class AbsRefreshAbleConfigureInit extends AbsConfigureInit {
	
	/**
	 * 配置版本刷新
	 */
	public void versionRefresh(){
		
		try{
			
			//1、获取版本号，如发现版本号发生变化，则拉取最新数据
			//2、覆盖版本号和数据
			Object[] signInfo = getLoader().loadSign(getConfigureName(),DirType.GLOBAL);
			if(signInfo == null){
				return;
			}
			
			String _sign = (String)signInfo[0];
			String path = (String)signInfo[1];
			
			if(!_sign.equals(this.sign)){
				byte[] data = getLoader().load(path);
				this.sign = _sign;
				
				clearData();
				
				configureDataResolve(data);
			}
	
		}catch(Exception e){
			ChuanQiLog.error("versionrefresh failed:" + getConfigureName(),e);
		}
		
		
	}

	/**
	 * 清理旧数据
	 */
	protected abstract void clearData();
	
}
