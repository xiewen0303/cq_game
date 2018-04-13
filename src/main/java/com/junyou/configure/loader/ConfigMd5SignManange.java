package com.junyou.configure.loader;

import java.util.HashMap;
import java.util.Map;
import com.junyou.utils.md5.Md5Utils;

/**
 * 配置文件MD5key全局管理 
 * @author DaoZheng Yuan
 * 2015年1月19日 下午3:50:31
 */
public class ConfigMd5SignManange {

	/**
	 * key:配置文件名称
	 * value:md5后的sign值
	 */
	private static final Map<String,String> CONFIG_SIGNS = new HashMap<String, String>();
	
	/**
	 * 配置文件MD5 sign
	 * @param fileName
	 * @param datas
	 */
	public static void addConfigSign(String fileName,byte[] datas){
		if(datas == null || fileName == null){
			return;
		}
		
		CONFIG_SIGNS.put(fileName, Md5Utils.md5Bytes(datas));
	}
	
	/**
	 * 获取配置文件的MD5 sign值
	 * @param fileName
	 * @return
	 */
	public static String getConfigSignByFileName(String fileName){
		return CONFIG_SIGNS.get(fileName);
	}
}
