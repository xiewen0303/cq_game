/**
 * 
 */
package com.junyou.gameconfig.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exadel.flamingo.flex.messaging.amf.io.AMF3Deserializer;
import com.junyou.log.ChuanQiLog;

/**
 * @description 游戏配置信息解析工具
 * @author ShiJie Chi
 * @created 2010-6-2下午02:52:29
 */
public class GameConfigUtil {

	/**
	 * 获取配置信息
	 * @param file
	 * @return
	 */
	public static Map<Object, Object> getResourceMap(File file){
		
		FileInputStream f_in;
		ByteArrayInputStream in = null;
		AMF3Deserializer deserializer = null;
		try {
			f_in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int tmp;
			while((tmp=f_in.read())!=-1){
				out.write(tmp);
			}
			f_in.close();
			in = new ByteArrayInputStream(out.toByteArray());
			out.close();
			
			deserializer = new AMF3Deserializer(in);
			@SuppressWarnings("unchecked")
			Map<Object, Object> os = (Map<Object, Object>) deserializer.readObject();
			return os;
		} catch (FileNotFoundException e) {
			ChuanQiLog.error("",e);
			return null;
		} catch (IOException e) {
			ChuanQiLog.error("",e);
			return null;
		}finally{
			try {
				in.close();
				deserializer.close();
			} catch (Exception e2) {
				ChuanQiLog.error("",e2);
			}
		}
	}
	
	public static Map<Object, Object> getResourceMap(byte[] fileData){
		ByteArrayInputStream in = null;
		AMF3Deserializer deserializer = null;
		try {
			in = new ByteArrayInputStream(fileData);
			deserializer = new AMF3Deserializer(in);
			
			@SuppressWarnings("unchecked")
			Map<Object, Object> os = (Map<Object, Object>) deserializer.readObject();
			
			return os;
		} catch (FileNotFoundException e) {
			ChuanQiLog.error("",e);
			return null;
		} catch (IOException e) {
			ChuanQiLog.error("",e);
			return null;
		}finally{
			try {
				in.close();
				deserializer.close();
			} catch (Exception e2) {
				ChuanQiLog.error("",e2);
			}
		}
	}
	
	public static JSONObject getJsonByBytes(byte[] fileData){
		JSONObject json = null;
		try{
			String data = new String(fileData,"utf-8");
			if("".equals(data)){
				return null;
			}
			
			json = JSONObject.parseObject(data);
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		return json;
	}
	
	public static JSONArray getJsonArrayByBytes(byte[] fileData){
		JSONArray json = null;
		try{
			String data = new String(fileData,"utf-8");
			if("".equals(data)){
				return null;
			}
			json = JSONArray.parseArray(data);
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		return json;
	}
	
	public static Object[] getResource(byte[] fileData){
		ByteArrayInputStream in = null;
		AMF3Deserializer deserializer = null;
		try {
			
			byte[] data = ThreeDesEncryptAndDecrypt.getDecryptResourceHandle(fileData);
			in = new ByteArrayInputStream(data);
			deserializer = new AMF3Deserializer(in);
			Object[] os = (Object[]) deserializer.readObject();
			
			return os;
		} catch (FileNotFoundException e) {
			ChuanQiLog.error("",e);
			return null;
		} catch (Exception e) {
			ChuanQiLog.error("游戏配置信息解析错误:",e);
			return null;
		}finally{
			try {
				in.close();
				deserializer.close();
			} catch (Exception e2) {
				ChuanQiLog.error("",e2);
			}
		}
	}
	
	
	public static Object getPublicResource(byte[] fileData){
		ByteArrayInputStream in = null;
		AMF3Deserializer deserializer = null;
		try {
			byte[] data = ThreeDesEncryptAndDecrypt.getDecryptResourceHandle(fileData);
			
			in = new ByteArrayInputStream(data);
			deserializer = new AMF3Deserializer(in);
			Object os = deserializer.readObject();
			
			return os;
		} catch (FileNotFoundException e) {
			ChuanQiLog.error("",e);
			return null;
		} catch (IOException e) {
			ChuanQiLog.error("",e);
			return null;
		}finally{
			try {
				in.close();
				deserializer.close();
			} catch (Exception e2) {
				ChuanQiLog.error("",e2);
			}
		}
	}

	
	
	/**
	 * 获取配置信息
	 * @param file
	 * @return
	 */
	public static Object[] getResource(File file){
		
		FileInputStream f_in;
		ByteArrayInputStream in = null;
		AMF3Deserializer deserializer = null;
		try {
			f_in = new FileInputStream(file);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int tmp;
			while((tmp=f_in.read())!=-1){
				out.write(tmp);
			}
			f_in.close();
			in = new ByteArrayInputStream(out.toByteArray());
			out.close();
			
			deserializer = new AMF3Deserializer(in);
			Object[] os = (Object[]) deserializer.readObject();
			
			return os;
		} catch (FileNotFoundException e) {
			ChuanQiLog.error("",e);
			return null;
		} catch (IOException e) {
			ChuanQiLog.error("",e);
			return null;
		}finally{
			try {
				in.close();
				deserializer.close();
			} catch (Exception e2) {
				ChuanQiLog.error("",e2);
			}
		}
		
	}
	
	/**
	 * 解二进制流配置文件
	 */
	public static DataInputStream binaryRead(String path) throws IOException{
		
		File f = new File(path);
		FileInputStream f_in = new FileInputStream(f);
//		DataInputStream d_in = new DataInputStream(f_in);
//		System.out.println(d_in.readInt());
//		System.out.println(d_in.readInt());
//		int tmp;
//		while((tmp=f_in.read())!=-1){
//			
//			System.out.println(tmp);
//		}
		return new DataInputStream(f_in);
	}
	
	public static void main(String[] args) throws URISyntaxException {
		URI uri = GameConfigUtil.class.getClassLoader().getResource("maplist.bin").toURI();
		File file = new File(uri);
		System.out.println(file.exists());
		
		Object[] data = getResource(file);
		System.out.println(data.length);
	}
}
