package com.junyou.utils.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.exadel.flamingo.flex.messaging.amf.io.AMF3Serializer;
import com.junyou.log.ChuanQiLog;

public class AmfUtil {

	/**
	 * 内部调用 
	 * @param outMsg
	 * @return
	 */
	public static byte[] convertMsg2Bytes(Object outMsg){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		AMF3Serializer amf3Serializer = new AMF3Serializer(bos);
		
		byte[] outByte = null;
		try {
			
			amf3Serializer.writeObject(outMsg);
			amf3Serializer.flush();
			
			outByte = bos.toByteArray();
			
		} catch (IOException e) {
			ChuanQiLog.error("",e);
		}finally{
			try {
				bos.close();
				amf3Serializer.close();
			} catch (IOException e) {
				ChuanQiLog.errorConfig("finally close err!",e);
			}
		}
		
		return outByte;
	}
	
	/**
	 * 通知客户端调用
	 * @param cmd
	 * @param data
	 * @return
	 */
	public static byte[] convertMsg2Bytes(Object cmd,Object data){
		if(cmd == null){
			ChuanQiLog.debug(JSON.toJSONString(data));
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		AMF3Serializer amf3Serializer = new AMF3Serializer(bos);
		
		byte[] outByte = null;
		try {
			amf3Serializer.writeShort(Integer.parseInt(cmd+""));
			amf3Serializer.writeObject(data);
			amf3Serializer.flush();
			
			outByte = bos.toByteArray();
		} catch (IOException e) {
			ChuanQiLog.errorConfig("",e);
		}finally{
			try {
				bos.close();
				amf3Serializer.close();
			} catch (IOException e) {
				ChuanQiLog.errorConfig("finally close err!",e);
			}
		}
		
		return outByte;
	}
	
}
