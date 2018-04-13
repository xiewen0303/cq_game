/**
 * 
 */
package com.kernel.data.filedb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.junyou.bus.caidan.dao.CaidanLogDao;
import com.junyou.bus.caidan.entity.CaidanLog;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;

/**
 * @description 文件存储
 * @author ShiJie Chi
 * @created 2010-10-26上午09:09:18
 */
public class Filedb {
	
	private static String fileDbRootDir = ClassLoader.getSystemResource("").getFile() + "data/";
	private static File fileDbDir = null;
	
	public static final String CONTENT_SEPERATE_SIGN = "~!@#@!~";
	
	static{
		try {
			fileDbDir = new File(fileDbRootDir);
			CslFileUtil.mkdir(fileDbDir);
		} catch (Exception e) {
			ChuanQiLog.error("filedb create fileDbRootDir error", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 移除数据
	 * @param component 指定模块
	 * @param key 主键
	 */
	public static void removeFile(String component, String key){
		
		File file = new File(new File(fileDbDir,component),key );
		CslFileUtil.delFile(file);
		
	}
	
	/**
	 * 创建文件
	 * @param component 指定模块
	 * @param key 指定主键
	 */
	public static File mkFile(String component,String key){
		
		File file = new File(new File(fileDbDir,component),key);
		CslFileUtil.delFile(file);
		
		CslFileUtil.mkFile(file);
		
		return file;
	}

	/**
	 * 获取文件
	 * @param component 指定模块
	 * @param key 主键
	 */
	public static File getFile(String component, String key) {
		
		File file = new File(new File(fileDbDir,component),key);
		if(file.exists()){
			return file;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 写文件
	 * @param component	指定模块
	 * @param key	主键
	 * @param info	写入内容
	 */
	public static void writeFile(String component,String key,String info){
		if(ObjectUtil.strIsEmpty(info)){
			return;
		}
		FileOutputStream fos = null;
		try {
			File file = Filedb.getFile(component, key);
			if(null == file){
				file = Filedb.mkFile(component, key);
			}
			fos = new FileOutputStream(file);
			fos.write(info.getBytes("utf-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(null != fos){
				try {
					fos.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static String readFile(String component,String key) {
		File file = Filedb.getFile(component, key);
		StringBuilder stringBuilder = new StringBuilder();
		if(null != file){
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));  
				String line = null;  
				while ((line = br.readLine()) != null) {  
					stringBuilder.append(line).append("\r\n");
				}  
			} catch (Exception e) {
				throw new RuntimeException(e);
			}finally{
				if(null != br){
					try {
						br.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return stringBuilder.toString();
	}
}
