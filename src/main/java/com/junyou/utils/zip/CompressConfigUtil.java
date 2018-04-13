package com.junyou.utils.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ThreeDesEncryptAndDecrypt;
import com.junyou.utils.codec.AmfUtil; 


/**
 * 配置数据压缩和解压
 * @author DaoZheng Yuan
 * 2013-11-15 上午10:12:06
 */
public class CompressConfigUtil {
	
	private static Logger logger = LogManager.getLogger(CompressConfigUtil.class);
	
	private static int CACHE_SIZE = 1024;
	
	private static Inflater DECOMPRESSER = new Inflater();
	private static Deflater COMPRESSER = new Deflater();
	
	
	/**
	 * 正常数据压缩(如果数据字节大于150则压缩)
	 * @param obj 数据
	 * @return Object[0] 指令
	 */
	public static Object[] compressAddCheckObject(Object obj){
		byte input[] = AmfUtil.convertMsg2Bytes(obj);
		
		if(input.length > GameConstants.PACK_BYTES){
			return new Object[]{ClientCmdType.PACK_DATA, compressBytes(input)};
		}else{
			return new Object[]{ClientCmdType.NO_PACK_DATA, obj};
		}
	}
	
	/**
	 * 正常数据压缩(转成flash对象后再压缩)
	 * @param obj
	 * @return
	 */
	public static byte[] compressBytes(Object obj)
	{
		
		byte input[] = AmfUtil.convertMsg2Bytes(obj);
		
		return compressBytes(input);
	}
	
	/**
	 * 加 密数据解密后再压缩
	 * @param input
	 * @return
	 */
	public static byte[] compressBytesAndDecrypt(byte[] input){
		//处理加密数据
		byte[] data = ThreeDesEncryptAndDecrypt.getDecryptResourceHandle(input);
		
		return compressBytes(data);
	}
	
	
	private static byte[] compressBytes(byte[] input)
	{
		COMPRESSER.reset();
		COMPRESSER.setInput(input);
		COMPRESSER.finish();
		byte output[] = new byte[0];
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try
		{
			byte[] buf = new byte[CACHE_SIZE];
			int got;
			while (!COMPRESSER.finished())
			{
				got = COMPRESSER.deflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
		} finally
		{
			try
			{
				o.close();
			} catch (IOException e)
			{
				logger.error("",e);
			}
		}
		
		return output;
	}
	
	public static byte[] decompressBytes(byte input[]){
		byte output[] = new byte[0];
		DECOMPRESSER.reset();
		DECOMPRESSER.setInput(input);
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try
		{
			byte[] buf = new byte[CACHE_SIZE];
			
			int got;
			while (!DECOMPRESSER.finished())
			{
				got = DECOMPRESSER.inflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
		}catch(Exception e){
			logger.error("",e);
		}finally{
			try
			{
				o.close();
			} catch (IOException e)
			{
				logger.error("",e);
			}
		}
		
		return output;
	}
}
