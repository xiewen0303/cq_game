package com.junyou.err;
/**
 * 用于http请求返回值的错误信息常量
 * @author LiuYu
 * @date 2014-12-22 下午1:09:05
 */
public class HttpErrorCode {
	
	
	//-------------------通用错误信息-----------------
	/**成功*/
	public static final String SUCCESS = "1";
	/**参数错误*/
	public static final String ARGS_ERROR = "-1";
	/**用户不存在*/
	public static final String USER_NOT_EXIST = "-2";
	/**角色不存在*/
	public static final String ROLE_NOT_EXIST = "-3";
	
	
	//----------------修改用户类型----------------------
	/**角色拥有公会，无法设置为GM*/
	public static final String ROLE_HAS_GUILD = "-21";
	
	//----------------邮件----------------------------
	/**邮件类型错误*/
	public static final String EMAIL_TYPE_ERROR = "-21";
	/**没有角色id*/
	public static final String EMAIL_NO_ROLEID = "-22";
	/**游戏认定为重复邮件*/
	public static final String EMAIL_CHONGFU = "-23";

	
	//----------------背包物品删除----------------------------
	/**获取不到不到物品*/
	public static final String NO_GOODS_ERROR = "-31";
	/**删除数量大于物品原有数量*/
	public static final String COUNT_ERROR = "-32";
	/**物品已被删除*/
	public static final String GOODS_IS_DELETE = "-33";
	
	//---------------------公会-------------------
	/**公会不存在*/
	public static final String GUILD_IS_NULL = "-41";
	/**公会名不合法*/
	public static final String GUILD_NAME_ERROR = "-42";
	/**公会公告不合法*/
	public static final String GUILD_NOTICE_ERROR = "-43";
	/**公会成员不存在*/
	public static final String GUILD_MEMBER_IS_NULL = "-44";
	/**选定的新会长不在当前公会*/
	public static final String GUILD_MEMBER_IS_NOT_IN_THIS_GUILD = "-45";
	/**不能开除会长*/
	public static final String GUILD_CANNOT_KICK_LEADER = "-46";
	
	
}
