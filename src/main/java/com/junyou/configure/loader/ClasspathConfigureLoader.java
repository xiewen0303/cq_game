/**
 * 
 */
package com.junyou.configure.loader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.junyou.log.ChuanQiLog;

/**
 * @description 本地配置加载器
 * @author ShiJie Chi
 * @created 2011-12-9下午2:14:36
 */
@Component
public class ClasspathConfigureLoader implements IConfigureLoader {
	
	public final static String GLOBAL_FILE_DIR = "config/game/sys_config/";
	public final static String MAP_RESOURCE_FILE_DIR = "config/game/map_config/";
	
	@Override
	public byte[] load(String fileName) {
		
		BufferedInputStream in = null;
		try {

			File file = new File(ClassLoader.getSystemResource("").getFile()+fileName);
			
			in = new BufferedInputStream(new FileInputStream(file));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int tmp;
			while((tmp = in.read())!=-1){
				out.write(tmp);
			}
			
			byte[] data = out.toByteArray();
			
			return data;
			
		} catch (Exception e) {
			ChuanQiLog.errorConfig("load config error :" + fileName + e);
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					ChuanQiLog.errorConfig("load config error :" + fileName + e);
				}
			}
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
