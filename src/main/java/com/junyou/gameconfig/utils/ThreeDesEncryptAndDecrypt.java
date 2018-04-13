package com.junyou.gameconfig.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.junyou.log.ChuanQiLog;
 

/**
 * 3DES加密 （或称为Triple DES）是三重数据加密算法
 */
public class ThreeDesEncryptAndDecrypt {
	/**
	 * 24 byte key
	 */
	private static final byte[] KEY_BYTE = {0x5b,(byte)0xe4,(byte)0xb8,(byte)0x89,(byte)0xe4,(byte)0xb8,(byte)0xaa,(byte)0xe8,(byte)0x87,(byte)0xad,(byte)0xe7,(byte)0x9a,(byte)0xae,(byte)0xe5,(byte)0x8c,(byte)0xa0,0x5f,0x6a,0x61,0x74,0x6b,0x65,0x79,0x5d};
    /**
     * five byte head info
     */
    private final static byte[] HEAD_EQ = new byte[]{(byte)0xEA,(byte)0xEA,(byte)0xEA,(byte)0xEA,(byte)0xEA};
    
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
    		if(i >= 5){
    			finalValue[i-5] = src[i];
    		}else{
    			headByte[i] = src[i];
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
}