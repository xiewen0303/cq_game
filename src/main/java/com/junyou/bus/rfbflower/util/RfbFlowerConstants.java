package com.junyou.bus.rfbflower.util;


public class RfbFlowerConstants {
	/**鲜花榜redis个人缓存的数据Map中key,玩家名称**/
	public static final String FIELD_USER_NAME = "1";
	/**鲜花榜redis个人缓存的数据Map中key,玩家职业**/
	public static final String FIELD_USER_JOB= "2";
	/**鲜花榜redis个人缓存的数据Map中key,玩家上榜时间**/
	public static final String FIELD_SHANGBANG_TIME= "3";
	/**鲜花榜redis个人缓存的数据Map中key,玩家所在服，便于客服事件查询数据**/
	public static final String FIELD_SERVER_ID= "4";
	/**排行榜一页显示9条**/
	public static final int ONE_PAGE_NUM= 9; 
	/**排行榜起始条**/
	public static final int ONE_PAGE_START= 0; 
	/**排行榜最大名次**/
	public static final int FLOWER_MAX_RANK= 50; 
	/**老活动数据设置活动结束后10天后过期(秒)**/
	public static final int FLOWER_OLD_DATA_EXPIRE_TIME= 10 *24 * 60 * 60;
}
