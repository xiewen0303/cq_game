package com.junyou.io.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.junyou.log.ChuanQiLog;

public class GsCountChecker {

	private static int maxCount = 5000;
	
	private static Map<String, String> whiteIps = new HashMap<String, String>();
	
	private static final GsCountChecker INSTANCE = new GsCountChecker();
	
	private GsCountChecker(){}
	
	public static GsCountChecker getInstance() {
		return INSTANCE;
	}
	
	public void refresh(){
		
		int max = 2000;
		InputStream in = null;
		try {
		
			Properties prop =  new Properties();
			in = ClassLoader.getSystemResourceAsStream("config/server-max-count.properties");
			prop.load(in);
			max = Integer.valueOf(prop.getProperty("max_count"));
		
		} catch (IOException e) {
			ChuanQiLog.error("", e);
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					ChuanQiLog.error("", e);
				}
			}
			
		}
		if(max != maxCount){
			reset(max);
		}
		
		refreshWhiteIps();
		
	}
	
	private static void refreshWhiteIps(){
		
		synchronized (whiteIps) {
			InputStream in = null;
			try {

				Properties prop =  new Properties();
				in = ClassLoader.getSystemResourceAsStream("config/white-ip.properties");
				prop.load(in);
				String ipsStr = prop.getProperty("white_ips");
			
				if(null != ipsStr) ipsStr = ipsStr.trim();
				
				whiteIps.clear();
				if(!"".equals(ipsStr)){
					String[] ips = ipsStr.split(",");
					if(ips.length > 0){
						
						for(String ip : ips){
							whiteIps.put(ip, ip);
						}
						
					}
				}
			} catch (IOException e) {
				ChuanQiLog.error("", e);
			}finally{
				if(null != in){
					try {
						in.close();
					} catch (IOException e) {
						ChuanQiLog.error("", e);
					}
				}
			}
		}
		
	}
	
	private static synchronized void reset(int max){
		maxCount = max;
	}
	
	public static void resetGameMaxOnlineCount(int max){
		//最高在线容错
		if(max > 5000){
			max = 5000;
		}
		
		reset(max);
	}
	
	
	public static boolean notWhiteIp(String ip){
		synchronized (whiteIps) {
			return !whiteIps.containsKey(ip);
		}
	}
	
	/**
	 * 获取内存中允许的最高在线人数
	 * @return
	 */
	public int getMaxCount(){
		return maxCount;
	}
	
	public static synchronized boolean isFull(){
		return GameSessionManager.getInstance().getOnlineCount() >= maxCount;
	}
	
	public static synchronized int remainCount(){
		int remainCount = maxCount - GameSessionManager.getInstance().getOnlineCount();
		remainCount = remainCount > 0 ? remainCount : 0;
		return remainCount;
	}
}
