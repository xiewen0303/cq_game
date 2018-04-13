package com.junyou.utils.common;

import com.junyou.gameconfig.constants.EffectType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 类型转换工具
 *
 * @author LiuJuan
 * @created 2011-12-12 上午09:35:57
 */
public class CovertObjectUtil {
	
	/**
	 * 属性值转换(obj -> Integer)
	 * @param val
	 * @return
	 */
	public static Integer object2Integer(Object val){
		//
		if(null != val && !"".equals(val.toString().trim())){
			String tmp = val.toString();
			if(tmp.equals("true")){
				return 1;
			}else if(tmp.equals("false")){
				return 0;
			}
			
			if( val instanceof Double ){
				return ((Double)val).intValue();
			}else if( val instanceof Integer ){
				return (Integer)val;
			}else if( val instanceof Long ){
				return ((Long)val).intValue();
			}else if( val instanceof Float ){
				return ((Float)val).intValue();
			}else if(val instanceof BigDecimal){
				return ((BigDecimal)val).intValue();
			}else{
				int index = tmp.indexOf(".");
				if(index != -1){
					tmp = tmp.substring(0, index);
					return Integer.parseInt(tmp);
				}else{
					return Integer.parseInt(tmp);
				}
			}
		}
		return null;
	}
	
	public static int object2int(Object val){
		if(null != val && !"".equals(val.toString().trim())){
			
			if(val instanceof String){
				
				String tmp = val.toString().trim();
				if(tmp.equals("true")){
					return 1;
				}else if(tmp.equals("false")){
					return 0;
				}
				else if(!tmp.matches("^-?\\d+$")){
					return 0;
				}
				
				int index = tmp.indexOf(".");
				if(index != -1){
					tmp = tmp.substring(0, index);
					return Integer.parseInt(tmp);
				}else{
					return Integer.parseInt(tmp);
				}
				
			}else if( val instanceof Double ){
				return ((Double)val).intValue();
			}else if( val instanceof Integer ){
				return (Integer)val;
			}else if( val instanceof Long ){
				return ((Long)val).intValue();
			}else if( val instanceof Float ){
				return ((Float)val).intValue();
			}else if(val instanceof BigDecimal){
				return ((BigDecimal)val).intValue();
			}
		}
		return 0;
	}
	
	public static Integer objectToint(Object val){
		if(null != val && !"".equals(val.toString().trim())){
			String tmp = val.toString();
			if(tmp.equals("true")){
				return 1;
			}else if(tmp.equals("false")){
				return 0;
			}
			
			if( val instanceof Double ){
				return ((Double)val).intValue();
			}else if( val instanceof Integer ){
				return (Integer)val;
			}else if( val instanceof Long ){
				return ((Long)val).intValue();
			}else if( val instanceof Float ){
				return ((Float)val).intValue();
			}else if(val instanceof BigDecimal){
				return ((BigDecimal)val).intValue();
			}else{
				int index = tmp.indexOf(".");
				if(index != -1){
					tmp = tmp.substring(0, index);
					return Integer.parseInt(tmp);
				}else{
					return Integer.parseInt(tmp);
				}
			}
		}
		return 0;
	}
	
	/**
	 * Object转成String,如果为null就返回""
	 * @param value
	 * @return String
	 */
	public static String object2String(Object value) {
		if (value == null){
			return "";
		}else{
			return value.toString().trim();
		}
	}
	
	/**
	 * Object转换成Str,如果为空就返回Null
	 * @param obj
	 * @return
	 */
	public static String obj2StrOrNull(Object obj){
		if( obj == null ){
			return null;
		}
		
		String str = obj.toString().trim();
		if( str.isEmpty() ){
			return null;
		}else{
			return str;
		}
	}
	
	/**
	 * Object转成boolean,如果为null就返回flase(旧方法，不建议用)
	 * @param value
	 * @return boolean
	 */
	public static boolean object2Boolean(Object value) {
		if (value != null && !"".equals(value.toString().trim())){
			String str = value.toString().trim();
			if(!str.equals("0") && !str.equals("false")){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	/**
	 * Object 转换为 boolean(新方法)
	 * @param obj
	 * @return
	 */
	public static boolean object2boolean(Object obj){
		if( obj == null ) return false;
		if( obj instanceof Boolean ){
			return (Boolean)obj;
		}else if( obj instanceof String ){
			String str = ((String)obj); 
			if( str.equals("true") || str.equals("1") )return true;
			else return false;
		}else if( obj instanceof Integer ){
			Integer it = (Integer)obj;
			if( it == 1 ) return true;
			else return false;
		}else if( obj instanceof Long ){
			Long it = (Long)obj;
			if( it == 1 ) return true;
			else return false;
		}else if( obj instanceof Short ){
			Short it = (Short)obj;
			if( it == 1 ) return true;
			else return false;
		}
		return false;
	}
	/**
	 * Object转成Float,如果为null就返回0f
	 * @param value
	 * @return
	 */
	public static Float object2Float(Object value){
		if (value != null && !"".equals(value.toString().trim())){
			String str = value.toString().trim();
			if(!str.equals("")){
				return Float.parseFloat(str);
			}
		}
		return 0f;
	}
	
	/**
	 * object转换为float,为空或转换失败，返回0
	 * @param obj
	 * @return
	 */
	public static float obj2float(Object obj){
		if( obj2StrOrNull(obj) == null ){
			return 0;
		}
		
		if( obj instanceof Float ){
			return (Float)obj;
		}else if( obj instanceof Integer ){
			return ( (Integer)obj ).floatValue();
		}else if( obj instanceof Short ){
			return ( (Short)obj ).floatValue();
		}else if( obj instanceof Double ){
			return ( (Double)obj ).floatValue();
		}else if( obj instanceof Long ){
			return ( (Long)obj ).floatValue();
		}else{
			String objStr = obj.toString().trim();
			try{
				return Float.parseFloat(objStr);
			}catch( Exception e ){
				return 0;
			}
		}
	}

	/**
	 * 属性值转换(obj -> Long)
	 */
	public static Long object2Long(Object val){
		if(null != val && !"".equals(val.toString().trim())){
			String tmp = val.toString().trim();
			if(tmp.equals("true")){
				return 1l;
			}else if(tmp.equals("false")){
				return 0l;
			}
			
			if( val instanceof Double ){
				return ((Double)val).longValue();
			}else if( val instanceof Integer ){
				return ((Integer)val).longValue();
			}else if( val instanceof Long ){
				return (Long)val;
			}else if( val instanceof Float ){
				return ((Float)val).longValue();
			}else if( val instanceof String ){
				BigDecimal bd = new BigDecimal(tmp);
				return bd.longValue();
			}else if(val instanceof BigDecimal){
				return ((BigDecimal)val).longValue();
			}else{
				int index = tmp.indexOf(".");
				if(index != -1){
					tmp = tmp.substring(0, index);
					return Long.parseLong(tmp);
				}else{
					return Long.parseLong(tmp);
				}
			}
		}
		return null;
	}
	/**
	 * object 转换为long,若为空的时候返回0
	 * @param obj
	 * @return
	 */
	public static long obj2long(Object obj){
		if( obj2StrOrNull(obj) == null ){
			return 0l;
		}
		
		if( obj instanceof Long ){
			return (Long)obj;
		}else if( obj instanceof Integer ){
			return ( (Integer)obj ).longValue();
		}else if( obj instanceof Short ){
			return ( (Short)obj ).longValue();
		}else if( obj instanceof Double ){
			return ( (Double)obj ).longValue();
		}else if( obj instanceof Float ){
			return ( (Float)obj ).longValue();
		}else if( obj instanceof String ){
			String objStr = obj.toString().trim();
			BigDecimal bd = new BigDecimal(objStr);
			return bd.longValue();
		}else if(obj instanceof BigDecimal){
			return ((BigDecimal)obj).longValue();
		}else{
			String objStr = obj.toString().trim();
			int index = objStr.indexOf(".");
			try{
				if( index > -1 ){
					objStr = objStr.substring(0, index);
				}
				return Long.parseLong(objStr);
			}catch( Exception e ){
				return 0l;
			}
		}
		
		
	}
	/**
	 * 日期格式String 转换成 long，若格式不对，返回0
	 * @return
	 */
	public static long str2long(String timeStr){
		try{
			SimpleDateFormat simpleDateFormatLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long time = simpleDateFormatLong.parse(timeStr).getTime();
			return time;
		}catch (Exception e) {
			return 0l;
		}
	}
	
	/**
	 * 是否为空
	 * @param val
	 * @return true:为空null 或 ""
	 */
	public static boolean isEmpty(String val){
		
		if(null == val || val.trim().isEmpty()){
			return true;
		}
		
		return false;
	}
	
	public static Map<String, Integer> object2Map(Object object) {
		if(object == null){
			return null;
		}
		
		Map<String,Integer> result = new HashMap<>();
		String[] contents = object.toString().split("\\|");
		for (String content : contents) {
			String[] value = content.split(":");
			result.put(value[0], CovertObjectUtil.object2int(value[1]));
		}
		return result;
	}

    public static Map<String,String> object2Map(String target,String sp1,String sp2) {
        if(target == null){
            return null;
        }

        Map<String,String> result = new HashMap<>();
        String[] contents = target.trim().split(sp1);
        for (String content : contents) {
            String[] value = content.split(sp2);
            if(content.length() >= 2){
                result.put(value[0], value[1]);
            }
        }
        return result;
    }

    public static long getZplus(Map<String,Long> mapData){
		return mapData == null ? 0 : CovertObjectUtil.obj2long(mapData.get(EffectType.zplus.name()));
	}
}
