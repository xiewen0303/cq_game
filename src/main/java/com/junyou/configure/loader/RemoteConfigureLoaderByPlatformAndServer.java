package com.junyou.configure.loader;

import java.net.URL;

import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.DownloadPathUtil;
import com.junyou.utils.md5.Md5Utils;

/**
 * 远程配置加载器
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-3-27 上午10:47:39
 */
public class RemoteConfigureLoaderByPlatformAndServer implements IConfigureLoader {
	
	
	public final static String SUFFIX = ".jat";
	public final static String PLARFORM_SUFFIX = "_p_";
	public final static String PUBLIC_SUFFIX = "_public";
	
	private String gameconfigBaseUrl;
	
	public void setGameconfigBaseUrl(String gameconfigBaseUrl) {
		this.gameconfigBaseUrl = gameconfigBaseUrl.trim();
	}

	public String getGameconfigBaseUrl(){
		if(gameconfigBaseUrl == null){
			gameconfigBaseUrl = ChuanQiConfigUtil.getLoadDirectoryUrl();
		}
		
		return gameconfigBaseUrl;
	}
	@Override
	public byte[] load(String fileName) {
		
		try {
			byte[] data = null;
			
			URL url = new URL(fileName);
			data = DownloadPathUtil.download(url);
			
			return data;
			
		} catch (Exception e) {
			ChuanQiLog.errorConfig("load config error :" +  fileName + e);
		}
		
		return null;
	}

	@Override
	public Object[] loadSign(String configureName,DirType dirType) {
		
		try {
			
			String requestUrl = getDirTypeRequestUrl(getGameconfigBaseUrl(),configureName);
			String path = null;
			URL url = null;
			byte[] data = null;
			if(dirType == DirType.GLOBAL){
				//全局
				path = requestUrl+PUBLIC_SUFFIX+SUFFIX;
				url = new URL(path);
				data = DownloadPathUtil.download(url);
			}else if(dirType == DirType.PLATFORM){
				//平台
				path = requestUrl+PLARFORM_SUFFIX+ChuanQiConfigUtil.getPlatfromId()+SUFFIX;
				url = new URL(path);
				data = DownloadPathUtil.downloadFuDate(url);
			}else if(dirType == DirType.SERVER){
				//服务器
				path = requestUrl+"_"+ChuanQiConfigUtil.getServerId()+SUFFIX;
				url = new URL(path);
				data = DownloadPathUtil.downloadFuDate(url);
			}
			
			if(data == null){
				return null;
			}
			
			return new Object[]{Md5Utils.md5Bytes(data).trim(),path.trim()};
		} catch (Exception e) {
			ChuanQiLog.errorConfig("load url:"+gameconfigBaseUrl+" sign error :" + configureName + e);
		}
		
		return null;
	}

	private String getDirTypeRequestUrl(String server,String configureName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(server);
		buffer.append("/").append(configureName);
		
		return buffer.toString();
	}

	

}
