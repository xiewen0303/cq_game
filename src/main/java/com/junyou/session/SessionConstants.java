package com.junyou.session;

/**
 * Session 相关常量
 * @author DaoZheng Yuan
 * 2014年11月24日 下午5:07:41
 */
public class SessionConstants {
	
	/**角色id在会话中的key**/
	public static final String ROLE_KEY = "role_key";
	/**账号在会话中的key**/
	public static final String ACCOUNT_KEY = "account_key";
	/**服务器Id在会话中的key**/
	public static final String SERVER_ID_KEY = "server_id_key";
	/**是否防沉迷在会话中的key**/
	public static final String IS_CHENMI_KEY = "is_chenmi_key";
	/**是否是微端登陆在会话中的key**/
	public static final String IS_WEIDUAN_KEY = "is_wd";
	/**	会话是否被挤出断线的key **/
	public static final String JICHU_KEY = "jichu_key";
	/**	剧情key **/
	public static final String JUNQING_KEY = "junqing_key";
	/**角色ip在会话中的id**/
	public static final String IP_KEY = "ip_key";
	/**连上时的时间key**/
	public static final String L_TIME_KEY = "login_s_key";
	/**跨服时，角色ids在会话中的key*/
	public static final String ROLES_KEY = "roles_key";
	/**账号key的连接符*/
	public static final String ACCOUNT_KEY_JOIN = "@";
	
	/**正常session类型*/
	public static final byte NOMAL_SESSION_TYPE = 0;
	/**跨服session类型*/
	public static final byte KUAFU_SESSION_TYPE = 1;
	//--------------------跨服------------------------------
	/**跨服角色集id在会话中的key**/
	public static final String KF_ROLES_KEY = "roles_key";
	
	
	/**进入场景标识**/
	public static final String IN_STAGE_KEY = "IN_STAGE_KEY";
	
	/**
	 * 临时变量，是否开启打印指令统计
	 */
	public static boolean TMP_STATIC_MSG = true;
	
	public static final int PUBLIC_KEY_STATIC_LENGTH = 20;
}
