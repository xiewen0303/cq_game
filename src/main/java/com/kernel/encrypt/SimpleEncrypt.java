package com.kernel.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.junyou.context.GameServerContext;
import com.junyou.session.SessionConstants;
import com.junyou.utils.lottery.Lottery;


/**
 * @description 一个简单的替换加密算法，实现了编码、解码函数对
 * @author hehj
 * 2011-11-2 下午2:08:25
 */
public class SimpleEncrypt {

	private String ticket;//"NITAMAHAIZHENGEINIPOLE";
	private byte[] ticketBytes;// = ticket.getBytes();
	private int ticketLength;// = ticketBytes.length;
	private int ticketIndex = 0;
	private int deTicketIndex = 0;
	
	private String key;
	private byte[] keyBytes;
	private int keyLength;
	private int keyIndex;
	private int deKeyIndex;
	
	/**
	 * @param key 明钥 
	 * @param ticket 密钥
	 */
	public SimpleEncrypt(String key,String ticket) {
		
//		if(null == key || "".equals(key)) throw new RuntimeException("key must be not null or empty.");
//		if(null == ticket || "".equals(ticket)) throw new RuntimeException("ticket must be not null or empty.");
		
		this.ticket = ticket;
		this.ticketBytes = this.ticket.getBytes();
		this.ticketLength = this.ticketBytes.length;
		this.ticketIndex = 0;
		this.deTicketIndex = 0;
		
		this.key = key;
		this.keyBytes = this.key.getBytes();
		this.keyLength = this.keyBytes.length;
		this.keyIndex = 0;
		this.deKeyIndex = 0;
	}
	
	public SimpleEncrypt(String key,byte[] tmpTicketBytes){
		this.ticketBytes = tmpTicketBytes;
		this.ticketLength = this.ticketBytes.length;
		this.ticketIndex = 0;
		this.deTicketIndex = 0;
		
		this.key = key;
		this.keyBytes = this.key.getBytes();
		this.keyLength = this.keyBytes.length;
		this.keyIndex = 0;
		this.deKeyIndex = 0;
	}
	
	public SimpleEncrypt(byte[] tmpKeyBytes,byte[] tmpTicketBytes){
		this.ticketBytes = tmpTicketBytes;
		this.ticketLength = this.ticketBytes.length;
		this.ticketIndex = 0;
		this.deTicketIndex = 0;
		
		this.keyBytes = tmpKeyBytes;
		this.keyLength = this.keyBytes.length;
		this.keyIndex = 0;
		this.deKeyIndex = 0;
	}
	
	/**
	 * 编码 
	 * @param data
	 * 
	 */		
    public byte[] encode(byte[] dataBytes){

    	int dataLength = dataBytes.length;
        
        for (int i= 0; i < dataLength; i++){
            int tkValue = ticketBytes[ticketIndex];
            ticketIndex = ++ticketIndex % ticketLength;
            
            int  keyValue = keyBytes[keyIndex];  
            keyIndex = ++keyIndex % keyLength;

            dataBytes[i] = (byte) ((dataBytes[i] + tkValue - keyValue) );
        }
        
        return dataBytes;
    }

	/**
	 * 解码 
	 * @param data
	 * 
	 */		
    public byte[] decode(byte[] dataBytes){

    	int dataLength = dataBytes.length;
//    	this.deKeyIndex =0;
//    	this.deTicketIndex =0;
        for (int i= 0; i < dataLength; i++){
            int tkValue = ticketBytes[deTicketIndex];	
            deTicketIndex = ++deTicketIndex % ticketLength;
            
            int  keyValue = keyBytes[deKeyIndex];  
            deKeyIndex = ++deKeyIndex % keyLength;

            dataBytes[i] = (byte) ((dataBytes[i] - tkValue + keyValue) );
        }
        
        return dataBytes;
    }
    
    public String getCurrKey(){
    	return key;
    }
    
    public byte[] getCurrKeyBytes(){
    	return keyBytes;
    }
    
    public static String getKey(){
    	String tmp = UUID.randomUUID().toString();
    	int rollValue = Lottery.roll(10);
    	
    	return tmp.substring(0, tmp.length() - rollValue);
    }
    
    private static byte[] getSecdictKey(byte[] pubKey){
		int b = pubKey[pubKey.length - 1] & 0xff;
		int offset = b >> 4 & 15;
		int length = b % 10 + 10;

		byte[] secKey = new byte[length];
		// 计算私钥
		for (int i = 0; i < length; i++) {
			b = pubKey[(offset + i) % SessionConstants.PUBLIC_KEY_STATIC_LENGTH] & 0xff;
			int secKeyOffset = (i & 0) == 1 ? b : GameServerContext.getSecdictBytes().length - 1 - b;
			secKey[i] = GameServerContext.getSecdictBytes()[secKeyOffset];
		}
		
		return secKey;
	}
    
    public static byte[] getKeyBytes(){
		Random r = new Random(System.currentTimeMillis());
		byte[] pubKey = new byte[SessionConstants.PUBLIC_KEY_STATIC_LENGTH + r.nextInt(20)];
		r.nextBytes(pubKey);
		
		return pubKey;
    }
    
}
