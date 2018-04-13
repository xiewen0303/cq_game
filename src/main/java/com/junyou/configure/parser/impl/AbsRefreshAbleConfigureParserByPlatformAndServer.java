/**
 * 
 */
package com.junyou.configure.parser.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import com.junyou.configure.loader.DirType;
import com.junyou.configure.parser.AbsConfigureParser;
import com.junyou.configure.schedule.RefreshableNoticeConfigureManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.md5.Md5Utils;




/**
 * 可刷新配置解析器（读取平台及服务器全部配置文件）
 * @author zhubo 
 * @email  zhubo@junyougame.com
 * @date 2015-4-23 上午11:46:40
 */
public abstract class AbsRefreshAbleConfigureParserByPlatformAndServer extends AbsConfigureParser {
	
	
	private Map<DirType,String> signMap;
	
	@Override
	public void init() {
		signMap = new HashMap<DirType, String>();
		signMap.put(DirType.GLOBAL, null);
		signMap.put(DirType.PLATFORM, null);
		signMap.put(DirType.SERVER, null);
		Iterator<DirType> it = signMap.keySet().iterator();
		while(it.hasNext()){
			DirType type = it.next();
			Object[] signInfo = getLoader().loadSign(getConfigureName(),type);
			if(signInfo == null){
				continue ;
			}
			String _sign = (String)signInfo[0];
			String path = (String)signInfo[1];
			byte[] data = getLoader().load(path);
			
			signMap.put(type, _sign);
			configureDataResolve(data);
		}
		
		//增加入管理器
		RefreshableNoticeConfigureManager.addConfig(this);
		
	}
	
	
	/**
	 * 配置版本刷新
	 */
	public int versionRefresh(){
		int result = -1;
		try{
			Iterator<DirType> it = signMap.keySet().iterator();
			while(it.hasNext()){
				DirType type = it.next();
				Object[] signInfo = getLoader().loadSign(getConfigureName(),type);
				if(signInfo == null){
					continue ;
				}
				String _sign = (String)signInfo[0];
				String path = (String)signInfo[1];
				
				String sign = signMap.get(type);
				if(null == sign ||	!sign.equals(_sign)){
					byte[] data = getLoader().load(path);
					
					//验证下载后的文件的md5值和取到的版本号是否一致，不一致的话不做任何处理
					if( _sign != null && !_sign.equals(Md5Utils.md5Bytes(data))){
						continue;
					}
					
					signMap.put(type, _sign);
					
					clearData();
					
					configureDataResolve(data);
					result = 0;
				}
			}
			
	
		}catch(Exception e){
			ChuanQiLog.error("versionrefresh failed:" + getConfigureName());
			result = -2;
		}
		return result;
		
	}

	/**
	 * 清理旧数据
	 */
	protected abstract void clearData();
	
	/**
	 * 获取当前配置简称
	 * @return
	 */
	public abstract String getSortName();
	
}
