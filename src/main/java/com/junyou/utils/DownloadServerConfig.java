package com.junyou.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.http.HttpUtil;


/**
 * 下载游戏启动配置文件
 * @author  作者：wind
 * @version 创建时间：2017-4-28 上午10:14:01
 */
public class DownloadServerConfig {
		
//	private static String ROOT_PATH = DownlandServerConfig.class.getClass().getResource("/").getPath(); 
	private static String ROOT_PATH = DownloadServerConfig.class.getClassLoader().getResource("").getPath();
	public static String gameconfigInfo = "gameconfig-info.properties";
	public static String gamebaseconfig = "game-base-config.xml";
	public static String jdbc = "jdbc.properties";
	
	public static String REMOTE_URL = "{0}/serverInfo.do";
	public static String REMOTE_KF_URL = "{0}/serverKfInfo.do";
	
	public static Pattern p = Pattern.compile("[${](\\w+)[}]");

	
	public static void replaceFiles(String serverId,String remoteUrl,String platform){
		Map<String,String> datas = new HashMap<>();
		datas.put(gameconfigInfo, ROOT_PATH+"/config/");
		datas.put(gamebaseconfig, ROOT_PATH+"/config/");
		datas.put(jdbc, ROOT_PATH+"/config/data/");
		
		Map<String, String> params = getServerConfig(serverId, remoteUrl,platform);
		
		//set openserver time
		long openServerTime = CovertObjectUtil.obj2long(params.get("open_server_time"));
		ServerInfoServiceManager.getInstance().setOpenServerTime(openServerTime);
		
		//replace file
		for (Entry<String,String> entry : datas.entrySet()) {
			String fileName = entry.getKey();
			String path = entry.getValue();
			replaceParam(params, fileName,path);
		}
	}
	
	
	/**
	 * 替换参数
	 * @param datas
	 */
	public static void replaceParam(Map<String,String> datas,String fileName,String path) {
		
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			String inputFile = path+"tpl_"+fileName;
			String outputFile = path+fileName;
			File checkFlag = new File(inputFile);
			if(!checkFlag.exists()){
				ChuanQiLog.debug("模板文件不存在:"+inputFile);
				return;
			}
			fileInputStream = new FileInputStream(inputFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream,"utf-8"));
			StringBuffer allContent = new StringBuffer();
			String content = null;
			while(null != (content = br.readLine())){
				Matcher m  = p.matcher(content);
				while(m.find()){
					String key = m.group(1);
					String value = datas.get(key);
					if(value == null){
						ChuanQiLog.error("游戏启动重要参数缺少,key="+key);
						System.exit(0);
					}
					content = content.replaceAll("\\$\\{"+key+"\\}", value);
				}
				allContent.append(content);
				allContent.append("\r\n");
			}
			
			fileOutputStream = new FileOutputStream(outputFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream,"utf-8"));
			bw.write(allContent.toString());
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fileInputStream != null){
					fileInputStream.close();
				}
				if(fileOutputStream != null){
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 获取远程ServerConfig参数
	 * @param serverId
	 * @param remoteUrl
	 * @return
	 */
	public static Map<String,String> getServerConfig(String serverId,String remoteUrl,String platform){
		Map<String, String> result = null;
		String contentStr = null;
		String url = null;
		String paramStr = null;
		try {
			Map<String,Object> paramObject = new HashMap<>();
			paramObject.put("serverId", serverId);
			paramObject.put("platform", platform);
			paramStr = HttpUtil.paramsMapToString(paramObject);
			
			if(remoteUrl.endsWith("_kf")){
				remoteUrl = remoteUrl.substring(0,remoteUrl.length() - 3);
				url = MessageFormat.format(REMOTE_KF_URL,remoteUrl);
			}else{
				url = MessageFormat.format(REMOTE_URL, remoteUrl);
			}
			
			contentStr = HttpUtil.excuteHttpCall(url, paramStr);
			if(contentStr == null || "".equals(contentStr)){
				ChuanQiLog.error("请求gameserver 重要配置url连接失败，url=" + remoteUrl);
				System.exit(0);
			}
			result = JSONObject.parseObject(contentStr,new TypeReference<Map<String,String>>(){});
		} catch (Exception e) {
			System.out.println("数据：contentStr=" + contentStr+"\t url="+url+"\t paramStr="+paramStr);
			e.printStackTrace();
			System.exit(0);
		}
		return result;
	}

	public static void replaceFiles(String[] args) {
		 try {
			if(args != null && args.length >= 2){
				 String remoteUrl = args[0];
				 String serverId = args[1];
				 String platform ="";
				 if(args.length >= 3){
					 platform = args[2];
				 }
				 replaceFiles(serverId, remoteUrl,platform);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
