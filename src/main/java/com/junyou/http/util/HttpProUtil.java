package com.junyou.http.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.junyou.constants.KeyConstants;
import com.junyou.http.key.KeyEnum;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.sort.MapSort;

/**
 * http服务工具类
 */
public class HttpProUtil {
	
	private HttpProUtil(){
		
	}
	
	/**
	 * 判断输入的字符串参数是否为空
	 * @return boolean 空则返回true,非空则flase
	 */
	public static boolean isEmpty(String input) {
		return null==input || 0==input.length() || 0==input.replaceAll("\\s", "").length();
	}
	
	/**
	 * 判断输入的字节数组是否为空
	 * @return boolean 空则返回true,非空则flase
	 */
	public static boolean isEmpty(byte[] bytes){
		return null==bytes || 0==bytes.length;
	}
	
	/**
	 * 字节数组转为字符串
	 * @see 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
	 */
	public static String getString(byte[] data, String charset){
		if(isEmpty(data)){
			return "";
		}
		if(isEmpty(charset)){
			return new String(data);
		}
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("将byte数组[{}]转为String时发生异常:系统不支持该字符集[{}]",data,charset);
			return new String(data);
		}
	}
	
	/**
	 * 字符串转为字节数组
	 * @see 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
	 */
	public static byte[] getBytes(String data, String charset){
		data = (data==null ? "" : data);
		if(isEmpty(charset)){
			return data.getBytes();
		}
		try {
			return data.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("将字符串[" + data + "]转为byte[]时发生异常:系统不支持该字符集[" + charset + "]");
			return data.getBytes();
		}
	}
	
	/**
	 * 获取http参数
	 * @param msg
	 * @return
	 */
	public static Map<String,Object> getHttpParams(String msg){
		if(isEmpty(msg)){
			return null;
		}
		
		Map<String,Object> params = new HashMap<String, Object>();
		String[] arrMsg = msg.split("&");
		
		for (String param : arrMsg) {
			String[] tmpPm = param.split("=");
			if(tmpPm.length > 1){
				params.put(tmpPm[0], tmpPm[1]);
			}else if(tmpPm.length == 1){
				params.put(tmpPm[0], "");
			}
		}
		
		return params;
	}
	
	/**
	 * http服务 md5 sign
	 * @param keyType
	 * @param paramObject
	 * @return 通过MD5加密过后的 16字节长度的字符值
	 */
	public static String httpMd5_sign(KeyEnum keyEnum,Map<String, Object> paramObject){
		Map<String, Object> sortMap = MapSort.mapSortByKey(paramObject);

		StringBuffer buf = new StringBuffer();
		for (Map.Entry<String, Object> entiry : sortMap.entrySet()) {
			buf.append(entiry.getKey()).append("=").append(entiry.getValue());
		}
		buf.append(KeyConstants.getHttpKey(keyEnum));
		
		return Md5Utils.md5To16(buf.toString());
	}
	
}
