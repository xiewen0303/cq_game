package com.junyou.utils.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.junyou.log.ChuanQiLog;

/**
 * http客户端请求工具类
 */
public class HttpClientMocker {

	public static String requestMockGet(String requestUrl,String[]... params){
		
		String destUrl = requestUrl;
		String paramStr = null;
		if(null!=params){
			int i =0;
			StringBuilder builder = new StringBuilder(); 
	        for(String[] param : params){
	        	if(i==0){
	        		++i;
	        		builder.append(param[0]).append("=").append(param[1]);
	        	}else{
	        		builder.append("&").append(param[0]).append("=").append(param[1]);
	        	}
	        }
	        paramStr = builder.toString();
		}

		URL url = null;
		HttpURLConnection connection = null;
		BufferedInputStream in = null;
		BufferedReader reader = null;
		try {
			url = new URL(destUrl+"?"+paramStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDefaultUseCaches(false);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			
			in = new BufferedInputStream(connection.getInputStream());
			
			reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
			
			String s = null;
			StringBuilder rspBuilder = new StringBuilder();
			while(null != (s = reader.readLine())){
				rspBuilder.append(s);
			}
			
			
			return rspBuilder.toString();
		}catch (Exception e) {
			ChuanQiLog.error("requestUrl:{}",requestUrl,e);
			return "";
		}finally{
			try {
				if(connection != null){ connection.disconnect();}
				if(in != null){in.close();}
				if(reader != null){reader.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static String requestMockPost(String requestUrl,String[]... params){
		String destUrl = requestUrl;
		
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(destUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);  
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			String paramStr = "";
			if(null!=params){
				int i =0;
				StringBuilder builder = new StringBuilder(); 
		        for(String[] param : params){
		        	if(i==0){
		        		++i;
		        		builder.append(param[0]).append("=").append(param[1]);
		        	}else{
		        		builder.append("&").append(param[0]).append("=").append(param[1]);
		        	}
		        }
		        paramStr = builder.toString();
			}else{
				paramStr = "";
			}
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());     
			out.write(paramStr); //向页面传递数据。post的关键所在！     
			out.flush();     
			out.close();
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String s = null;
			StringBuilder rspBuilder = new StringBuilder();
			while(null != (s = reader.readLine())){
				rspBuilder.append(s);
			}
			in.close();
			
			reader.close();
			
			return rspBuilder.toString();
		}catch (Exception e) {
			ChuanQiLog.error("requestUrl:{}",requestUrl,e);
			return "";
		}finally{
			if(connection != null){
				connection.disconnect();
			}
		}
	}
	public static String requestMockPost(String requestUrl,String paramStr){
		String destUrl = requestUrl;
		
		URL url;
		HttpURLConnection connection = null;
		OutputStreamWriter out = null;
		BufferedInputStream in = null;
		BufferedReader reader = null;
		StringBuilder rspBuilder =  new StringBuilder();;
		try {
			
			url = new URL(destUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);  
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			
			out = new OutputStreamWriter(connection.getOutputStream(),"utf-8");     
			out.write(paramStr); //向页面传递数据。post的关键所在！     
			out.flush();     
			
			
			in = new BufferedInputStream(connection.getInputStream());
			
			reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
			
			String s = null;
			
			while(null != (s = reader.readLine())){
				rspBuilder.append(s);
			}
			in.close();
			reader.close();
			
		}catch (Exception e) {
			ChuanQiLog.error("requestUrl:{}",requestUrl,e);
			return "";
		}finally{
			try {
				if(connection != null){
					connection.disconnect();
				}
				if(out != null){out.close();}
				if(in != null){in.close();}
				if(reader != null){reader.close();}
			} catch (Exception e) {
				ChuanQiLog.error("关闭http连接错误",e);
				return "";
			}
		}
		return rspBuilder.toString();
	}
	
	
	/**
	 * 获取远程文本文件内容
	 * 
	 * @param destUrl
	 *            远程Url
	 * @param fileName
	 *            远程文件名
	 * @param timeout
	 *            连接超时时间
	 * @return
	 */
	public static String requestRemoteFileData(String destUrl, String fileName, int timeout) {
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(destUrl + fileName);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(15000);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuilder builder = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String lineStr = null;
				while ((lineStr = reader.readLine()) != null) {
					builder.append(lineStr);
				}
				return builder.toString();
			}

		} catch (SocketTimeoutException e) {
			ChuanQiLog.error("java.net.SocketTimeoutException: connect timed out,{"+destUrl+"}{"+fileName+"}");
		} catch (FileNotFoundException e) {
			ChuanQiLog.error("remote file {"+destUrl+"}{"+fileName+"} not found");
		} catch (ConnectException e) {
			ChuanQiLog.error("connect url {"+destUrl+"}{"+fileName+"} timeout");
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					ChuanQiLog.error("close reader  {"+destUrl+"}{"+fileName+"} io exception");
				}
			}
			if(connection != null){
				connection.disconnect();
				connection = null;
			}
		}
		return null;
	}
}
