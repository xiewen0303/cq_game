package com.junyou.utils.common;

import java.net.URL;

import com.junyou.log.ChuanQiLog;

/**
 * @author DaoZheng Yuan
 * 2015年5月19日 上午10:39:46
 */
public class DownloadRemoteUtil {

	/**
	 * 下载数据
	 * @param bashPath
	 * @param dirName
	 * @param configureName
	 * @return
	 */
	public static byte[] download(String bashPath,String dirName,String configureName){
		byte[] data = null;
		
		try {
			URL url = new URL(getDirRequestUrl(bashPath,dirName,configureName));
			data = DownloadPathUtil.download(url);
			
		} catch (Exception e) {
			ChuanQiLog.errorConfig("DownloadRemoteUtil load config error :" +  configureName + e);
		}
		
		return data;
	}
	
	private static String getDirRequestUrl(String path,String dirName,String configureName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(path);
		if(dirName != null){
			buffer.append("/").append(dirName);
		}
		buffer.append("/").append(configureName);
		
		return buffer.toString();
	}
}
