/**
 * 
 */
package com.junyou.configure.loader;

import java.net.URL;

import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.DownloadPathUtil;
import com.junyou.utils.md5.Md5Utils;

/**
 * 远程配置加载器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午1:50:18
 */
public class RemoteConfigureLoaderData implements IConfigureLoader {
	
	private String gameconfigBaseUrl;
	
	public void setGameconfigBaseUrl(String gameconfigBaseUrl) {
		this.gameconfigBaseUrl = gameconfigBaseUrl.trim();
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
			
			String requestUrl = getDirTypeRequestUrl(gameconfigBaseUrl,configureName);
			
			String path = requestUrl;
			URL url = new URL(path);
			byte[] data = DownloadPathUtil.downloadFuDate(url);
			
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
