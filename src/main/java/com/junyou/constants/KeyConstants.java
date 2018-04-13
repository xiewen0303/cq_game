package com.junyou.constants;

import java.util.HashMap;
import java.util.Map;

import com.junyou.http.key.KeyEnum;

/**
 * 全局key保存
 */
public class KeyConstants {
	
	/**通信时的ticket**/
	private static final Map<String, String> keyMap = new HashMap<String, String>();
	static{
		keyMap.put("CN", "cn_huaqiangu2015");
		keyMap.put("TW", "cn_huaqiangu2015");
		keyMap.put("VN", "cn_huaqiangu2015");
		keyMap.put("KR", "GAME_SDKQNNNDKR");
		keyMap.put("TH", "GAME_SDKQNNNDTH");
		keyMap.put("DAMA", "GAME_SDKQNNNDDAMA");
		keyMap.put("US", "GAME_SDKQNNNDUS");
		keyMap.put("ID", "GAME_SDKQNNNDID");
	}
	/**保护服务器通信ticket**/
	public static final String DONE_SERVER_KEY = "QNNNDLZLH_SDK";
	public static final String DONE_SERVER_HEAD = "INITQNNND";
	
	/**获取通信时的ticket**/
	public static String getTicket(String ServerCountry){
		return keyMap.get(ServerCountry);
	}
	
	public static byte[] getTicketBytes(String serverCountry){
		return new byte[]{(byte)124,(byte)88,(byte)180,(byte)112,(byte)215,(byte)53,(byte)123,(byte)53,(byte)115,(byte)102};
	}
	

	/**登录游戏时的验证key,在游戏登录程序中(gate)有一个同样的key保证玩家是通过正常gate进入到我们游戏**/
	public static final String DEFAULT_KEY = "junyou_huaqiangu_gate_key";
	/**http加密普通keytype = 1 的 key**/
	public static final String REMOTE_HTTP_KEY = "junyou_http_qnnnd_key";
	/**http加密keytype = 2 的 key**/
	public static final String REMOTE_HTTP_CHARGE_KEY = "http_miwan_charges_key";
	/**http加密keytype = 3 的 key**/
	public static final String REMOTE_HTTP_GMTOOLS_KEY = "gmtoolshttpjunyouhttpkey";
	/**http加密keytype = 9 的 key**/
	public static final String REMOTE_HTTP_STOPSERVER_KEY = "stopserverjunyouhttpkey";
	
	/**
	 * 获取http服务key
	 * @param type
	 * @return
	 */
	public static String getHttpKey(KeyEnum keyEnum){
		if(keyEnum == null || keyEnum.equals(KeyEnum.NOMAL)){
			return REMOTE_HTTP_KEY;
		}else if(keyEnum.equals(KeyEnum.CONFIDENTIAL)){
			return REMOTE_HTTP_CHARGE_KEY;
		}else if(keyEnum.equals(KeyEnum.GMTOOLS_SAFE)){
			return REMOTE_HTTP_GMTOOLS_KEY;
		}else if(keyEnum.equals(KeyEnum.SECRET)){
			return REMOTE_HTTP_STOPSERVER_KEY;
		}
		
		return null;
	}
}
