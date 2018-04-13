package com.junyou.utils.http;

import java.util.Map;

import com.junyou.utils.http.HttpClientMocker;

public class HttpUtil{
	
	private static final String SIGN_PARAM_NAME = "sign";
	
	/**
	 * 拼装http服务器请求需要的url参数[工具方法],带签名
	 * @param paramObject
	 * @return
	 */
	public static String paramsMapSignToString(Map<String,Object> paramObject,String sign){
		//空判定
		if(paramObject == null){
			return "";
		}
		
		StringBuffer buf = new StringBuffer();
		for (Map.Entry<String, Object> entry : paramObject.entrySet()) {
			buf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		
		return buf.append(SIGN_PARAM_NAME).append("=").append(sign).toString();
	}
	
	/**
	 * 拼装http服务器请求需要的url参数[工具方法]
	 * @param paramObject
	 * @return
	 */
	public static String paramsMapToString(Map<String,Object> paramObject){
		//空判定
		if(paramObject == null){
			return "";
		}
		
		StringBuffer buf = new StringBuffer();
		for (Map.Entry<String, Object> entry : paramObject.entrySet()) {
			buf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		
		return buf.toString();
	}
	
	
	/**
	 * 执行http调用 [工具方法]
	 * @param url	游戏服务器url
	 * @param paramStr	参数
	 * @return
	 */
	public static String excuteHttpCall(String url,String paramStr){
		return HttpClientMocker.requestMockPost(url, paramStr);
	}
}
