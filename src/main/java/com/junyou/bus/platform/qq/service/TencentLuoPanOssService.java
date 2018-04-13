package com.junyou.bus.platform.qq.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.log.ChuanQiLog;

/**
 * @author zhongdian
 * 2015-7-29 下午1:50:08
 */
@Service
public class TencentLuoPanOssService {

	public void sendLuoPan(String luoPanUrl,Map<String, String> map){
		StringBuilder tmpSb = new StringBuilder();
		tmpSb.append(luoPanUrl);
		String str = mapChargeString(map);
		tmpSb.append(str);
		URL url;
		try {
			url = new URL(tmpSb.toString());
			download(url);
		} catch (MalformedURLException e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 将map中的参数转成url请求字符串
	 * @param map
	 * @return
	 */
	private String mapChargeString(Map<String, String> map){
		String str = "";
		for (String key : map.keySet()) {
			if("".equals(str)){
				String s = map.get(key);
				if(s == null){
					s = "";
				}
				str = key+"="+s;
			}else{
				String s = map.get(key);
				if(s == null){
					s = "";
				}
				str = str + "&"+key+"="+s;
			}
		}
		return str;
	}
    private static void download( final URL url){
    	TencentLuoPanService.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(5000); 
		    		connection.setReadTimeout(5000);  
					connection.connect();
		    		
		    		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 

		    		// 发送数据到服务器并使用Reader读取返回的数据 
		    	/*	String lines; 
		    		 
		            while ((lines = reader.readLine()) != null) { 
		            	ChuanQiLog.error("腾讯OSS罗盘返回："+lines);
		            } */
		            
		    	    reader.close(); 
		    	    // 断开连接 
		    		connection.disconnect(); 
				}catch (Exception e) {
					ChuanQiLog.error("======>"+url.toString()+" not found.");
				}
				return;
			}
		});
    	return;
	}
    
    public int pfChargePfId(String pf){
    	if(pf == null){
    		return 0;
    	}
    	if(pf.equals(QqConstants.QZONE)){
    		return 1;
    	}else if(pf.equals(QqConstants.PENGYOU)){
    		return 2;
    	}else if(pf.equals(QqConstants.QQYOUXI)){
    		return 10;
    	}else if(pf.equals(QqConstants.WEBSITE)){
    		return 12;
    	}else if(pf.equals(QqConstants.QJIA)){
    		return 4;
    	}else if(pf.equals(QqConstants.WEIBO)){
    		return 3;
    	}else if(pf.equals(QqConstants.QQGAME)){
    		return 10;
    	}else if(pf.equals(QqConstants._3366)){
    		return 11;
    	}else if(pf.startsWith(QqConstants.MANYOU)){
    		return 15;
    	}
    	return 0;
    }
    
}
