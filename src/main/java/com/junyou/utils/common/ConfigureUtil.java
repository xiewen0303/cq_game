package com.junyou.utils.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import com.junyou.log.ChuanQiLog;

public class ConfigureUtil {
	
	private static Map<String, Properties> propMap = new HashMap<String, Properties>();
	
	public static int getIntProp(String propFileName, String key){
		return Integer.valueOf(getStringPro(getProperties(propFileName), key));
	}

	public static double getDoubleProp(String propFileName,String key){
		return Double.valueOf(getStringPro(getProperties(propFileName), key));
	}
	
	public static String getProp(String propFileName,String key){
		return getStringPro(getProperties(propFileName), key);
	}
	
	public static String getPropAllowNull(String propFileName,String key){
		return getStringProAllowNull(getProperties(propFileName), key);
	}
	
	public static String[] getProps(String propFileName,String keyStartWith){
		Properties prop = getProperties(propFileName);
		String []results = new String[0]; 
		Set<Map.Entry<Object, Object>> sets =  prop.entrySet();
		for(Entry<Object, Object> entry:sets){
			String key = (String)entry.getKey();
			if(key.startsWith(keyStartWith)){
				results = (String[])ArrayUtils.add(results, (String)entry.getValue());
			}
		}
		return results;
	}

	private static Properties getProperties(String propFileName){
		if(!propMap.containsKey(propFileName)){
			synchronized (propMap) {
				if(!propMap.containsKey(propFileName)){
				    try {
				    	Properties prop =  new Properties();
				    	prop.load(ClassLoader.getSystemResourceAsStream(propFileName));
				    	propMap.put(propFileName, prop);
				    } catch (Exception e) {
				        throw new RuntimeException("error to load ' " + propFileName + " ',pls check it.",e);
				    }
				
				}
			}
		}
		return propMap.get(propFileName);
	}
	
	public static int reloadProperties(String propFileName){
		 try {
		    	Properties prop =  new Properties();
		    	prop.load(ClassLoader.getSystemResourceAsStream(propFileName));
		    	propMap.put(propFileName, prop);
		    } catch (Exception e) {
		       ChuanQiLog.error("error to load ' " + propFileName + " ',pls check it.",e);
		        return -1;
		    }
		 return 1;
	}
	
	private static String getStringPro(Properties prop,String key){
		
		if(null==prop) throw new IllegalStateException("' properties ' has not been initialized.");
		
		String str = prop.getProperty(key);
		
		if(null==str) throw new RuntimeException("' " + key + " ' was not configured.");

		return str.trim();
	}
	
	private static String getStringProAllowNull(Properties prop,String key){
		if(null==prop){
			return null;
		}
		
		String str = prop.getProperty(key);
		if(null==str){
			return null;
		}
		return str.trim();
	}

}
