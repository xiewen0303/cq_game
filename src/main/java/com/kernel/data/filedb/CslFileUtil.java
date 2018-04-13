package com.kernel.data.filedb;

import java.io.File;
import java.io.IOException;

public class CslFileUtil {
	
	public static void mkdir(File file){
		if(!file.exists()){
			if(!file.getParentFile().exists()){
				mkdir(file.getParentFile());
			}
			file.mkdir();
		}
	}
	
	public static void mkFile(File file){
		if(!file.exists()){
			if(!file.getParentFile().exists()){
				mkdir(file.getParentFile());
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void delFile(File file){
		if(file.exists()){
			if(file.isDirectory()){
			
				File[] files = file.listFiles();
				if( null!=files ){
					for(File tmp : files){
						delFile(tmp);
					}
				}
				file.delete();
			}else{
				file.delete();
			}
		}
	}
}
