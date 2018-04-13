package com.junyou.public_.nodecontrol.helper;

import com.junyou.constants.KeyConstants;
import com.junyou.context.GameServerContext;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.md5.Md5Utils;

public class LoginHelper {
	
	public static final String CHECK_IP = "192.168";
	/**
	 * 验证登陆参数是否合法
	 * @param userId
	 * @param timestamp
	 * @param verify
	 * @return
	 */
	public static boolean checkSign(String userId, String timestamp, String verify,String ip){
		
		if(GameServerContext.getGameAppConfig().isDebug() && ip != null && ip.startsWith(CHECK_IP)){
			return true;
		}else{
			String md5Source = new StringBuffer()
			.append(userId)
			.append(KeyConstants.DEFAULT_KEY)
			.append(timestamp)
			.toString();
			String v = Md5Utils.md5To32(md5Source); // MD5加密
			
			return v.equals(verify);
		}
	}
	
	/**
	 * 验证时间
	 * @param timestamp
	 * @return
	 */
	public static boolean checkTime(String timestamp,String ip){
		if(GameServerContext.getGameAppConfig().isDebug() && ip != null && ip.startsWith(CHECK_IP)){
			return true;
		}else{
			if(ObjectUtil.strIsEmpty(timestamp)){
				return false;
			}
			long time = CovertObjectUtil.obj2long(timestamp);
			long cur = System.currentTimeMillis() / 1000;//精确到秒
			
			return Math.abs(cur - time) < 300;//允许范围300秒
		}
	}
	
//	public static void main(String[] args) {
//		String md5Source = new StringBuffer()
//		.append("ydz")
//		.append(KeyConstants.DEFAULT_KEY)
//		.append("123456789")
//		.toString();
//		ChuanQiLog.error(Md5Utils.md5To32(md5Source));
//	}
	
	
}
