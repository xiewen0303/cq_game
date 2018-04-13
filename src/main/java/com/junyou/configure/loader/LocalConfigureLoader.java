/**
 * 
 */
package com.junyou.configure.loader;

import java.net.MalformedURLException;
import java.net.URL;

import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.DownloadPathUtil;

/**
 * @description 本地配置加载器
 * @author ShiJie Chi
 * @created 2011-12-9下午2:14:36
 */
public class LocalConfigureLoader implements IConfigureLoader {
	
	public final static String GLOBAL_FILE_DIR = "/web/java/zhcn/";
	public final static String MAP_RESOURCE_FILE_DIR = "/web/r/m/";
	
	public final static String SERVER = "192.168.0.5";
	
	@Override
	public byte[] load(String fileName) {
		
		try {
			URL url = new URL("http", SERVER, fileName);
			
			byte[] data = DownloadPathUtil.download(url);
			
			return data;
			
		} catch (MalformedURLException e) {
			ChuanQiLog.errorConfig("load config error :" + fileName+e);
		}
		
		
		return null;
	}

	@Override
	public Object[] loadSign(String fileName,DirType dirType) {
		
		String dir = GLOBAL_FILE_DIR;
		if(dirType.equals(DirType.MAPRESOURCE)){
			dir = MAP_RESOURCE_FILE_DIR;
		}
		String filePath = dir + fileName;
		
		return new Object[]{null,filePath};
	}
	

}
