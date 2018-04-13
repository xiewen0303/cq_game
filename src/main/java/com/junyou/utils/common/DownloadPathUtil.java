/**
 * 
 */
package com.junyou.utils.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import com.junyou.log.ChuanQiLog;

/**
 * @description 下载路径工具
 * @author ShiJie Chi
 * @created 2011-6-28下午03:27:09
 */
public class DownloadPathUtil {
	
	public static final String REMOTE_ROOT_DIR_URL = "http://192.168.2.202:8080/web/";
	
	private static File LOCAL_ROOT_DIR = null;//new File(ClassLoader.getSystemResource("").getFile());
	
	private static String REMOTE_GLOBAL_RESOURCE_DIR_URL = REMOTE_ROOT_DIR_URL + "config/java/";
	
	private static String REMOTE_MAP_RESOURCE_DIR_URL = REMOTE_ROOT_DIR_URL + "res/map";
	
	private static String REMOTE_MAP_COPY_DIR_URL = REMOTE_ROOT_DIR_URL + "res/dungeon";
	
	private static String REMOTE_RESOURCE_DIR_URL = REMOTE_ROOT_DIR_URL + "config/java/initvalue";
	
	/**
	 * 获取本地根目录
	 * @param
	 */
	public static File getLocalRootDir(){
		if(null == LOCAL_ROOT_DIR){
			LOCAL_ROOT_DIR = new File(ClassLoader.getSystemResource("").getFile());
		}
		return LOCAL_ROOT_DIR;
	}
	
	/**
	 * 设置本地根目录
	 * @param
	 */
	public static void setLocalRootDir(File dir){
		LOCAL_ROOT_DIR = dir;
	}
	
	public static String getRemoteResourceDirUrl(){
		return REMOTE_RESOURCE_DIR_URL;
	}
	
	/**
	 * 远程公共资源url
	 * @param
	 */
	public static String getRemoteGlobalResourceDirUrl(){
		return REMOTE_GLOBAL_RESOURCE_DIR_URL;
	}
	
	/**
	 * 远程地图资源url
	 * @param
	 */
	public static String getRemoteMapResourceDirUrl(){
		return REMOTE_MAP_RESOURCE_DIR_URL;
	}
	
	/**
	 * 远程副本配置
	 * @param
	 */
	public static String getRemoteMapCopyDirUrl(){
		return REMOTE_MAP_COPY_DIR_URL;
	}
	
	/**
	 * 创建目录/文件
	 * @param
	 */
	public static boolean mkdir(File file){
		if(!file.exists()){
			if(!file.getParentFile().exists()){
				mkdir(file.getParentFile());
			}
			
			return file.mkdir();
		}else{
			return false;
		}
	}
	
	/**
	 * 下载文件到指定目录
	 * @param remoteUrl 远程目录路径
	 * @param storeDir 保存目录
	 * @param storeName 文件名
	 */
	public static void downloadFile(String remoteUrl,File storeDir,String storeName){
		
		try {
			URL url = new URL(remoteUrl);
			downloadFile(url, storeDir, storeName);
		} catch (MalformedURLException e) {
			System.out.println("======>"+remoteUrl+" not found.");
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * 下载文件到指定目录
	 * @param url 远程目录路径
	 * @param storeDir 保存目录
	 * @param storeName 文件名
	 */
	public static void downloadFile(URL url,File storeDir,String storeName){
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			
			File storeFile = new File(storeDir,storeName);
			
			FileOutputStream out = new FileOutputStream(storeFile);
			byte[] buf = new byte[1024];
			int readSize=0;
			while((readSize=in.read(buf))!=-1){
				out.write(buf, 0, readSize);
			}
			in.close();
			out.flush();
			out.close();
		}catch (FileNotFoundException e) {
			System.out.println("======>"+url.toString()+" not found.");
		}catch (IOException e) {
//			throw new RuntimeException(e);
			System.out.println("======>"+url.toString()+" not found.");
		}
	}
	
	/**
	 * @param
	 */
	public static byte[] download(URL url){
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			
			InputStream in = connection.getInputStream();
//			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int tmp;
			while((tmp = in.read())!=-1){
				out.write(tmp);
			}
			in.close();
			
			byte[] result = out.toByteArray();
			
			out.close();
			
			return result;
			
		}catch (FileNotFoundException e) {
			System.out.println("======>"+url.toString()+" not found.");
		}catch (IOException e) {
			System.out.println("======>"+url.toString()+" not found.");
		}
		return null;
	}
	
	/**
	 * 带超时时间的请求
	 * @param url
	 * @param timeout (单位毫秒)
	 * @return
	 */
	public static byte[] download(URL url,int timeout){
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(15000);
			connection.connect();
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int tmp;
			while((tmp = in.read())!=-1){
				out.write(tmp);
			}
			in.close();
			
			byte[] result = out.toByteArray();
			
			out.close();
			
			return result;
			
		}catch (FileNotFoundException e) {
			System.out.println("======>"+url.toString()+" not found.");
		}catch (IOException e) {
			System.out.println("======>"+url.toString()+" not found.");
		}
		return null;
	}
	
	
	/**
	 * @param
	 */
	public static byte[] downloadFuDate(URL url){
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int tmp;
			while((tmp = in.read())!=-1){
				out.write(tmp);
			}
			in.close();
			
			byte[] result = out.toByteArray();
			
			out.close();
			
			return result;
			
		}catch (FileNotFoundException e) {
			ChuanQiLog.error("======>"+url.toString()+" not found.");
		}catch (IOException e) {
			ChuanQiLog.error("======>"+url.toString()+" not found.");
		}
		
		return null;
	}
	
}
