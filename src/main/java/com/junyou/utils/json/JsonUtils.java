package com.junyou.utils.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;

public class JsonUtils {
	
	/**
	 * @param params jsonArray.toArray()出来的数据
	 */
	public static Object[] decodeJson(Object[] params) {
		
		for(int i = 0 ; i< params.length ; i++){
			
			Object param = params[i];
			
			if(param instanceof JSONArray){
				
				params[i] = decodeJson(((JSONArray)param).toArray());
			}
		}
		
		return params;
	}
	
	/**
	 * jsonArray转换为Object[],会自动识别Array中的JSONObject和JSONArray
	 * @param jsonArray
	 * @return
	 */
	public static Object[] jsonArray2Array(JSONArray jsonArray){
		
		if( isNull(jsonArray) )return null;
		
		Object[] result = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			Object obj = jsonArray.get(i);
			if( obj instanceof JSONObject ){
				result[i] = jsonObj2Array((JSONObject)obj);
			}else if( obj instanceof JSONArray ){
				result[i] = jsonArray2Array((JSONArray)obj);
			}else{
				result[i] = obj;
			}
		}
		return result;
	}
	
	/**
	 * jsonObject转换为Object[],会自动识别jsonObject中的JSONObject和JSONArray
	 * @param jsonArray
	 * @return
	 */
	private static Object[] jsonObj2Array(JSONObject jsonObj){
		if( isNull(jsonObj) )return null;
		
		Object[] result = new Object[jsonObj.size()];
		
		int i = 0;
		for(Map.Entry<String, Object> entry : jsonObj.entrySet()){
			String key = entry.getKey();
			Object obj = entry.getValue();
			if( obj instanceof JSONObject ){
				result[i++] = new Object[]{key, jsonObj2Array((JSONObject)obj)};
			}else if( obj instanceof JSONArray ){
				result[i++] = new Object[]{key, jsonArray2Array((JSONArray)obj)};
			}else{
				result[i++] = new Object[]{key, obj};
			}
		}
		return result;
	}
	
	public static JSONArray getJsonArrayByBytes(byte[] fileData){
		JSONArray json = null;
		try{
			if(fileData == null){
				return null;
			}
			
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
	
	public static JSONObject getJsonObjectByBytes(byte[] fileData){
		JSONObject json = null;
		try{
			if(fileData == null){
				return null;
			}
			
			String data = new String(fileData,"utf-8");
			json = JSONObject.parseObject(data);
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		return json;
	}
	
	/**
	 * @param params jsonArray.toArray()出来的数据
	 */
	public static Object[] decodeJson2(Object[] params) {
		
		for(int i = 0 ; i< params.length ; i++){
			
			Object param = params[i];
			
			if(param instanceof JSONArray){
				
				params[i] = decodeJson2(((JSONArray)param).toArray());
			}else if( param instanceof JSONObject ){
				params[i] = json2Map( (JSONObject)param );
			}
			
		}
		
		return params;
	}
	/**
	 * JsonObject 转换成Map
	 * @param jsonObj
	 * @return
	 */
	public static Map<Object, Object> json2Map(JSONObject jsonObj){
		 Map<Object, Object> result = new HashMap<Object, Object>();
		
		 for(Map.Entry<String, Object> entry : jsonObj.entrySet()){
			String key = entry.getKey();
			Object obj = entry.getValue();
			if( obj instanceof JSONArray ){
				obj = decodeJson2( ((JSONArray)obj).toArray() );
			}else if( obj instanceof JSONObject ){
				obj = json2Map((JSONObject)obj);
			}
			result.put(key, obj);
		}
		return result;
	}
	
	/**
	 * jsonObject以key、value的形式转换为二位数组
	 * @param jsonStr
	 * @return
	 */
	public static Object[] jsonObj2Array(String jsonStr){
		if( ObjectUtil.strIsEmpty(jsonStr) ){
			return null;
		}
		
		JSONObject jsonObj = JSON.parseObject(jsonStr);
		Object[] result = new Object[jsonObj.size()];
		int i = 0;
		
		for(Map.Entry<String, Object> entry : jsonObj.entrySet()){
			result[i++] = new Object[]{entry.getKey(), entry.getValue()};
		}
		return result;
	}
	
	/**
	 * 二维数组转换为JSONObject
	 * @param skills
	 * @return 返回一个不为null的JSONObject对象
	 */
	public static JSONObject array2JSONObj(Object[] skills){
		JSONObject result = new JSONObject();
		if( skills != null && skills.length > 0 ){
			for( Object tmp : skills ){
				Object[] objs = (Object[])tmp;
				result.put((String)objs[0], objs[1]);
			}
		}
		return result;
	}

	/**
	 * JSONArray转换为JSONObject对象
	 * @return 不为Null的JSONObject
	 */
	public static JSONObject array2JSONObj(JSONArray jsonArray){
		JSONObject result = new JSONObject();
		if( jsonArray != null && jsonArray.size() > 0 ){
			for (int j = 0; j < jsonArray.size(); j++) {
				JSONArray objs = jsonArray.getJSONArray(j);
				result.put(objs.getString(0), objs.get(1));
			}
		}
		return result;
	}
	
	/**
	 * 一维的JSONArray转换为List<String>
	 * @param jsonArray
	 * @return 不为Null的List<String>
	 */
	public static List<String> jsonArray2List(JSONArray jsonArray){
		List<String> result = new ArrayList<String>();
		if( jsonArray != null && jsonArray.size() > 0 ){
			for (int j = 0; j < jsonArray.size(); j++) {
				result.add( jsonArray.getString(j) );
			}
		}
		return result;
	}
	
	/**
	 * JSONObject转换为Map<String, Integer>
	 * @param jsonObj
	 * @return
	 */
	public static Map<String, Integer> json2MapInt(JSONObject jsonObj){
		if( jsonObj == null || jsonObj.isEmpty() ){
			return null;
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(String key : jsonObj.keySet()){
			result.put(key, jsonObj.getIntValue(key));
		}
		return result;
	}
	
	   /**
     * JSONObject转换为Map<String, Long>
     * @param jsonObj
     * @return
     */
    public static Map<String, Long> json2MapLong(JSONObject jsonObj){
        if( jsonObj == null || jsonObj.isEmpty() ){
            return null;
        }
        Map<String, Long> result = new HashMap<String, Long>();
        for(String key : jsonObj.keySet()){
            result.put(key, jsonObj.getLongValue(key));
        }
        return result;
    }
    
	/**
	 * 取Key值，没有则为0
	 * @param jsonObj
	 * @param key
	 * @return
	 */
	public static int getInt(JSONObject jsonObj, String key){
		return jsonObj.getIntValue(key);
	}
	/**
	 * 取Key值，没有则为false
	 * @param jsonObj
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(JSONObject jsonObj, String key){
		Object value = jsonObj.get(key);
		return value == null ? false : (Boolean)value;
	}
	
	/**
	 * 把jsonString转换为JsonObject,如果string为空，则new一个新的JsonObject
	 * @param string
	 * @return
	 */
	public static JSONObject getJSONObj(String string){
		if( ObjectUtil.strIsEmpty(string) ){
			return new JSONObject();
		}else{
			return JSON.parseObject(string);
		}
	}
	/**
	 * 把jsonArrayString转换为JSONArray,如果string为空，则new一个新的JSONArray
	 * @param string
	 * @return
	 */
	public static JSONArray getJSONArray(String string){
		if("null".equals(string) || ObjectUtil.strIsEmpty(string) ){
			return new JSONArray();
		}else{
			return JSONArray.parseArray(string);
		}
	}
	
	/**
	 * JSONArray转换为二维数组(JSONArray中存的还是JOSNArray)
	 * @return 二维数组
	 */
	public static Object[] getJsonArrayToObject(JSONArray array){
		if( array == null || array.size() == 0){
			return null;
		}
		
		Object[] result = new Object[array.size()];
		int i = 0;
		for (Object object : array) {
			result[i++] = ((JSONArray)object).toArray();
		}
		return result;
	}
	/** JSONObject 是否为空 */
	public static boolean isNull(JSONObject jsonObj){
		return jsonObj == null || jsonObj.isEmpty();
	}
	/** JSONArray 是否为空 */
	public static boolean isNull(JSONArray jsonArray){
		return jsonArray == null || jsonArray.isEmpty();
	}
	
	public static void main(String[] args) {
		JSONObject jobj = JSON.parseObject("");
		System.out.println(jobj.toJSONString());
	}
}
