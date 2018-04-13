package com.junyou.bus.platform.qq.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.junyou.bus.platform.qq.json.JSONException;
import com.junyou.bus.platform.qq.json.JSONObject;
import com.junyou.log.ChuanQiLog;

public class QqUtil {
	/**
     * 执行API调用
     * 
     * @param scriptName OpenApi CGI名字 ,如/v3/user/get_info
     * @param params OpenApi的参数列表
     * @param protocol HTTP请求协议 "http" / "https"
     * @return 返回服务器响应内容
     * @throws UnsupportedEncodingException 
     */
    public static String api(String scriptName, HashMap<String, String> params, String protocol,String appid,String appkey,String serverName){

        // 无需传sig,会自动生成
        params.remove("sig");

        // 添加固定参数
        params.put("appid", appid);

        // 请求方法
        String method = "post";
        
        // 签名密钥
        String secret = appkey + "&";
        
        // 计算签名
        String sig = "";
		try {
			sig = SnsSigCheck.makeSig(method, scriptName, params, secret);
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("api sig error",e);
		}
        
        params.put("sig", sig);

        StringBuilder sb = new StringBuilder(64);
        sb.append(protocol).append("://").append(serverName).append(scriptName);
        String url = sb.toString(); 

        // cookie
        HashMap<String, String> cookies = null;

        // 发送请求
        String resp = "";
		try {
			resp = SnsNetwork.postRequest(url, params, cookies, protocol);
		} catch (OpensnsException e1) {
			ChuanQiLog.error("",e1);
			ChuanQiLog.error("{} error",scriptName);
		}finally{
			ChuanQiLog.error("appId:{}\t\tappkey:{}",appid,appkey);
			ChuanQiLog.error("url:{}============resp:{}\nparams:{}\n",url,resp,params);
		}
        
        return resp;
    }
    
    /**
     * 是否是黄钻用户
     * @param resp
     * @return 0：不是; 1：是
     * @throws OpensnsException
     */
    public static int isYellowVip(String resp) throws OpensnsException{
		 // 解码JSON
      JSONObject jo = null;
      try 
      {
          jo = new JSONObject(resp);
      } 
      catch (JSONException e) 
      {
   	   ChuanQiLog.error("api return json error",e);
      } 
      // 检测ret值
      int ret = jo.optInt("ret",0);
      int rc = 0;
      if(ret == 0){
    	rc  = jo.optInt("is_yellow_vip", 0);
      }
      
      
      return rc;
	}
    /**
     * 获取黄钻等级
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static int huangZuanLevel(String resp) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
    	int ret = jo.optInt("ret",0);
        int rc = 0;
        if(ret == 0){
    	 rc = jo.optInt("yellow_vip_level", 0);
        }
    	
    	return rc;
    }
    
    
    /**
     * 是否是年费黄钻
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static int nianHuangZuan(String resp) throws OpensnsException{
		 // 解码JSON
      JSONObject jo = null;
      try 
      {
          jo = new JSONObject(resp);
      } 
      catch (JSONException e) 
      {
   	   ChuanQiLog.error("api return json error",e);
      } 

      // 检测ret值
      int ret = jo.optInt("ret",0);
      int rc = 0;
      if(ret == 0){
    	  rc = jo.optInt("is_yellow_year_vip", 0);
      }
      
      return rc;
	}
    /**
     * 获取蓝钻等级
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static int lanZuanLevel(String resp) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
    	int ret = jo.optInt("ret",0);
    	int rc = 0;
    	if(ret == 0){
    		rc = jo.optInt("blue_vip_level", 0);
    	}
    	
    	return rc;
    }
    
    /**
     * 是否是年费蓝钻
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static int nianLanZuan(String resp) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
    	int ret = jo.optInt("ret",0);
    	Object rc = 0;
    	if(ret == 0){
    		//rc = jo.optInt("is_blue_year_vip", 0);
    		rc = jo.opt("is_blue_year_vip");
    	}
    	if (rc instanceof Integer){
    		return (int) rc;
    	}else if(rc instanceof Boolean){
    		boolean b = (boolean) rc;
			if(b){
				return 1;
			}else{
				return 0;
			}
    	}
    	return 0;
    }
    
    /**
     * 获得3366包子等级
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static int _3366BaoZiLevel(String resp) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
    	int ret = jo.optInt("ret",0);
    	int rc = 0;
    	if(ret == 0){
    		rc = jo.optInt("3366_grow_level", 0);
    	}
    	
    	return rc;
    }
    /**
     * 是否是豪华黄钻
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static int haoHuaHuangZuan(String resp) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
    	int ret = jo.optInt("ret",0);
    	int rc = 0;
    	if(ret == 0){
    		rc = jo.optInt("is_yellow_high_vip", 0);
    	}
    	return rc;
    }
    
    /**
     * 是否是年费蓝钻
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static int haoHuaLanZuan(String resp) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
    	int ret = jo.optInt("ret",0);
    	int rc = 0;
    	if(ret == 0){
    		rc = jo.optInt("is_super_blue_vip", 0);
    	}
    	return rc;
    }
    
    
    /**
     * 获取蓝钻过期时间
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static long getLanZuanGuoQiTime(String resp) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
    	int ret = jo.optInt("ret",0);
        long rc = 0;
        if(ret == 0){
        	rc = jo.optInt("vip_valid_time", 0);
        }
    	
    	return rc*1000;
    }
    
    
}
