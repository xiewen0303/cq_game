package com.junyou.bus.platform.qq.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.dao.TencentUserInfoDao;
import com.junyou.bus.platform.qq.entity.TencentUserInfo;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.event.TencentAppCustomLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

/**
 * @author zhongdian
 * 2015-7-29 下午1:50:08
 */
@Service
public class TencentLuoPanService {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private TencentUserInfoDao tencentUserInfoDao;
	/**
	 * 高潜用户日志
	 * @param userRoleId
	 */
	public void gaoqianLog(Long userRoleId){
		RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		if(keyMap == null){
			return;
		}
		String appCustom = keyMap.get("appCustom");
		if(appCustom != null && !"".equals(appCustom)){
			GamePublishEvent.publishEvent(new TencentAppCustomLogEvent(role.getUserId(),appCustom));
		}
	}
	
	public void sendLuoPan(String luoPanUrl,Map<String, String> map,String jiekou){
		StringBuilder tmpSb = new StringBuilder();
		tmpSb.append(luoPanUrl).append(jiekou).append("?");
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
	public void sendLuoPanLM(String luoPanUrl,Map<String, String> map,String jiekou){
		StringBuilder tmpSb = new StringBuilder();
		tmpSb.append(luoPanUrl).append(jiekou).append("&");
		String str = mapChargeString(map);
		tmpSb.append(str);
		URL url;
		try {
			url = new URL(tmpSb.toString());
			downloadLm(url);
		} catch (MalformedURLException e) {
			ChuanQiLog.error("",e);
		}
	}
    
	public void sendLuoPanLMRecharge(String luoPanUrl,Map<String, String> map,String jiekou,String pfkey){
		StringBuilder tmpSb = new StringBuilder();
		tmpSb.append(luoPanUrl).append(jiekou).append("&");
		String str = mapChargeString(map);
		tmpSb.append(str);
		URL url;
		try {
			url = new URL(tmpSb.toString());
			//int rc = downloadLm(url);
			 downloadLm(url);
			//rc 不等于0  日志记录  并且再次上报
			/*if(rc != 0){
				GamePublishEvent.publishEvent(new TencentAppLmSbLogEvent(map.get("openid"),map.get("pf"),pfkey));
				download(url);
			}*/
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
	
    private static void download(final URL url){
    	executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(5000); 
		    		connection.setReadTimeout(5000);  
					connection.connect();
		    		
		    		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 

		    		/* //发送数据到服务器并使用Reader读取返回的数据 
		    		String lines; 
		    		 
		            while ((lines = reader.readLine()) != null) { 
		            	ChuanQiLog.error("腾讯罗盘返回："+lines);
		            } 
		            */
		    	    reader.close(); 
		    	    // 断开连接 
		    		connection.disconnect(); 
					
				}catch (FileNotFoundException e) {
					ChuanQiLog.error("======>"+url.toString()+" not found.",e);
				}catch (IOException e) {
					ChuanQiLog.error("======>"+url.toString()+" not found.",e);
				}
			}
		});
		
		return;
	}
    private static ExecutorService executorService  = Executors.newFixedThreadPool(5);
    
    public static ExecutorService getExecutorService() {
		return executorService;
	}

	private static void downloadLm(final URL url){
    	executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(5000); 
					connection.setReadTimeout(5000);  
					connection.connect();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
					
					// 发送数据到服务器并使用Reader读取返回的数据 
					/*String lines; 
    		 
            while ((lines = reader.readLine()) != null) { 
            	ChuanQiLog.error("腾讯联盟返回："+lines);
            } 
					 */
					// 解码JSON
					/* JSONObject jo = null;
	        try 
	        {
	            jo = (JSONObject) JSON.parse(lines);
	        } 
	        catch (JSONException e) 
	        {
	            ChuanQiLog.error("",e); 
	        } 

	        // 检测ret值
	        //rc = jo.optInt("ret", 0);
	        rc = jo.getIntValue("ret");
					 */
					reader.close(); 
					// 断开连接 
					connection.disconnect(); 
//					return 0;
					
				}catch (FileNotFoundException e) {
					ChuanQiLog.error("======>"+url.toString()+" not found.");
				}catch (IOException e) {
					ChuanQiLog.error("======>"+url.toString()+" not found.");
				}
				
			}
		});
//    	int rc = -1;
//    	return rc;
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
    
    public void tencentViaUser(Long userRoleId){
    	RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		TencentUserInfo info = new TencentUserInfo();
		if(keyMap != null){
			info.setUserRoleId(userRoleId);
			info.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			String via = keyMap.get("via");
			if(via != null){
				info.setVia(via);
			}
			info.setPf(keyMap.get("pf"));
		}else{
			info = new TencentUserInfo();
			info.setUserRoleId(userRoleId);
			info.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		}
		/*if(sessionManagerExportService.isOnline(userRoleId)){
			tencentUserInfoDao.cacheInsert(info, userRoleId);
		}else{*/
			tencentUserInfoDao.dbInsert(info);
		//}
    }
    
    public List<TencentUserInfo> initTencentUserInfos(Long userRoleId){
    	return tencentUserInfoDao.initTencentUserInfo(userRoleId);
    }
    
    public String getUserZhuCePf(Long userRoleId){
    	List<TencentUserInfo> lists = tencentUserInfoDao.cacheAsynLoadAll(userRoleId);
    	if(lists == null || lists.size() <= 0){
    		return null;
    	}
    	TencentUserInfo info = lists.get(0);
    	return info.getPf();
    }
    public String getUserZhuCeVia(Long userRoleId){
    	List<TencentUserInfo> lists = tencentUserInfoDao.cacheAsynLoadAll(userRoleId);
    	if(lists == null || lists.size() <= 0){
    		return null;
    	}
    	TencentUserInfo info = lists.get(0);
    	return info.getVia();
    }
    
}
