package com.junyou.utils.md5;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.junyou.log.ChuanQiLog;
 
/**
 * 3DES加密 （或称为Triple DES）是三重数据加密算法
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-3-27 下午2:35:50
 */

public class ThreeDesEncryptAndEncrypt {
	
	/**
	 * 24 byte key
	 */
	private static final byte[] KEY_BYTE = {0x44, 0x21, 0x3F, 0x58, (byte)0x8E, 0x13, 0x43, 0x39 , 0x38, 0x52, 0x39, 0x15, 0x35, 0x47, 0x36, (byte)0xEF , 0x21, 0x5E, (byte)0xBc, 0x51, 0x32, 0x45, 0x31, (byte)0xE3}; 
    /**
     * five byte head info
     */
    private final static byte[] HEAD_EQ = new byte[]{(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE};
    
    /**
     * Defines the encryption algorithm, available  DES,DESede,Blowfish
     */
	private static final String Algorithm = "DESede";
	
	/**
	 * 加密
	 * @author ydz
	 * @param src 为加密后的缓冲区
	 * @return 加密后的 字节数据
	 */
    public static byte[] encryptMode(byte[] src) {
       try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(KEY_BYTE, Algorithm);

            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
        	ChuanQiLog.error("",e1);
        } catch (javax.crypto.NoSuchPaddingException e2) {
        	ChuanQiLog.error("",e2);
        } catch (java.lang.Exception e3) {
        	ChuanQiLog.error("",e3);
        }
        return null;
    }

    /**
     * 解密
     * @author ydz
     * @param src 为加密后的缓冲区
     * @return 解密后的字节数据
     */
    public static byte[] decryptMode( byte[] src) {      
    try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(KEY_BYTE, Algorithm);

            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
        	ChuanQiLog.error("",e1);
        } catch (javax.crypto.NoSuchPaddingException e2) {
        	ChuanQiLog.error("",e2);
        } catch (java.lang.Exception e3) {
        	ChuanQiLog.error("",e3);
        }
        return null;
    }

    
    /**
     * 获取资源数据
     * @author ydz
     * @param src
     * @return
     */
    public static byte[] getDecryptResourceHandle(byte[] src){
    	if(src == null){
    		return null;
    	}
    	
    	if(src.length <= 5){
    		return src;
    	}
    	
    	
    	byte[] headByte = new byte[5];
    	
    	byte[] finalValue = new byte[src.length - 5];
    	for (int i = 0; i < src.length; i++) {
    		if(i < 5){
    			headByte[i] = src[i];
    		}else{
    			System.arraycopy(src, 5, finalValue, 0, src.length - 5);
    			break;
    		}
		}
    	
    	boolean isNeedDecrypt = true;
    	for (int i = 0; i < HEAD_EQ.length; i++) {
    		
    		if(headByte[i] != HEAD_EQ[i]){
    			isNeedDecrypt = false;
    			break;
    		}
		}
    	
    	if(isNeedDecrypt){
    		return decryptMode(finalValue);
    	}else{
    		return src;
    	}
    }
    
    public static boolean isHead(byte[] src){
    	if(src == null){
    		return false;
    	}
    	
    	if(src.length < 5){
    		return false;
    	}
    	
    	boolean isNeedDecrypt = true;
    	for (int i = 0; i < HEAD_EQ.length; i++) {
    		
    		if(src[i] != HEAD_EQ[i]){
    			isNeedDecrypt = false;
    			break;
    		}
		}
    	return isNeedDecrypt;
    }
}