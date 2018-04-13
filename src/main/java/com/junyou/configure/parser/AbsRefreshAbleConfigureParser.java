/**
 * 
 */
package com.junyou.configure.parser;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.configure.loader.DirType;
import com.junyou.configure.schedule.RefreshableConfigureScheduler;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.md5.Md5Utils;



/**
 * @description 可刷新配置解析器
 * @author ShiJie Chi
 * @created 2011-12-8下午5:45:04
 */
public abstract class AbsRefreshAbleConfigureParser extends AbsConfigureParser {
	
	@Autowired
	private RefreshableConfigureScheduler scheduler;
	
	@Override
	public void init() {
		
		super.init();
		
		//增加入定时刷新器中
		scheduler.addRefreshableParser(this);
		
	}
	
	
	/**
	 * 配置版本刷新
	 */
	public void versionRefresh(){
		
		try{
			
			//1、获取版本号，如发现版本号发生变化，则拉取最新数据
			//2、覆盖版本号和数据
			Object[] signInfo = getLoader().loadSign(getConfigureName(),DirType.GLOBAL);
			if(signInfo == null){
				return ;
			}
			String _sign = (String)signInfo[0];
			String path = (String)signInfo[1];
			
			if(null == this.sign ||	!this.sign.equals(_sign)){
				byte[] data = getLoader().load(path);
				
				//验证下载后的文件的md5值和取到的版本号是否一致，不一致的话不做任何处理
				if( _sign != null && !_sign.equals(Md5Utils.md5Bytes(data))){
					return;
				}
				
				this.sign = _sign;
				
				clearData();
				
				configureDataResolve(data);
			}
	
		}catch(Exception e){
			ChuanQiLog.error("versionrefresh failed:" + getConfigureName());
		}
		
		
	}

	/**
	 * 清理旧数据
	 */
	protected abstract void clearData();
	
}
