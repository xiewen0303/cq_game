package com.kernel.utils;

import com.junyou.analysis.KuafuAppConfig;
import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.constants.GameConstants;
import com.junyou.utils.codec.SerializableUtil;

public class SerializableHelper {
	
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	public static Object deserialize(byte[]bytes){
		KuafuAppConfig kuafuAppConfig = ServerInfoConfigManager.getInstance().getKuafuAppConfig();
		
		Object o = null;
		if(GameConstants.PROTOCOL_JAVA.equals(kuafuAppConfig.getProtocol())){
			o = SerializableUtil.javaDeserialize(bytes);
		}else if(GameConstants.PROTOCOL_FST.equals(kuafuAppConfig.getProtocol())){
			o = SerializableUtil.fstDeserialize(bytes);
		}
		
		return o;
	}
	
	/**
	 * 序列化
	 * @param o
	 * @return
	 */
	public static byte[] serialize(Object o){
		KuafuAppConfig kuafuAppConfig = ServerInfoConfigManager.getInstance().getKuafuAppConfig();
		
		byte[] b = null;
		if(GameConstants.PROTOCOL_JAVA.equals(kuafuAppConfig.getProtocol())){
			b = SerializableUtil.javaSerialize(o);
		}else if(GameConstants.PROTOCOL_FST.equals(kuafuAppConfig.getProtocol())){
			b = SerializableUtil.fstSerialize(o);
		}
		
		return b;
	}
}
