package com.junyou.bus.platform.utils;

import com.junyou.utils.ChuanQiConfigUtil;

/**
 * 平台常量
 * @author LiuYu
 * 2015-6-6 下午5:07:30
 */
public class PlatformConstants {
	/**
	 * 37平台id 
	 */
	public static final String PLATFORM_37 = "37wan";
	/**2345平台id*/
	public static final String PLATFORM_2345 = "2345";
	/**kis平台id*/
	public static final String PLATFORM_KIS = "kis";
	/**
	 * qq平台id 
	 */
	public static final String PLATFORM_QZONE = "qzone";
	
	/**
	 * pps平台id 
	 */
	public static final String PLATFORM_PPS = "pps";
	
	/**
	 * 台湾
	 */
	public static final String PLATFORM_TAIWAN_1 = "gm99";
	public static final String PLATFORM_TAIWAN_2 = "gamexdd";
	
	/**
	 * 越南平台
	 */
	public static final String PLATFORM_YUENAN = "vtcgame";
	
	/**
	 * 韩国平台
	 */
	public static final String PLATFROM_HANGUO = "panggame";
	
	/**
	 * 是不是37平台
	 * @return true:37平台
	 */
	public static boolean is37(){
		return ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_37);
	}
	
	/**
	 * 是不是pps平台
	 * @return true:pps平台
	 */
	public static boolean isPPs(){
		return ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_PPS);
	}
	/**
	 * 是不是qq平台
	 * @return true:qq平台
	 */
	public static boolean isQQ(){
		return ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_QZONE);
	}
	/**
	 * 是不是台湾的
	 */
	public static boolean isTaiWan(){
		return ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_TAIWAN_1) || ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_TAIWAN_2);
	}
	
	/**
	 * 是不是越南平台
	 */
	public static boolean isYueNan(){
		return ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_YUENAN);
	}
	/**
	 * 是不是37玩及其下属平台
	 * @return true:pps平台
	 */
	public static boolean is37wanGroup(){
		return ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_37) || ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_2345) || ChuanQiConfigUtil.getPlatfromId().equals(PLATFORM_KIS);
	}
	
	/**
	 * 是不是韩国平台 
	 * @return
	 */
	public static boolean isHanGuo(){
	    return ChuanQiConfigUtil.getPlatfromId().equals(PLATFROM_HANGUO);
	}
	
}
