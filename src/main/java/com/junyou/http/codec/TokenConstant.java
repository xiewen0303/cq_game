package com.junyou.http.codec;


/** 
 * 常量类
 */
public class TokenConstant {

	public static final String CHARSET = "UTF-8";
	
	/***************业务类型********************/
	public static final String BUSI_TYPE_TCP = "TCP";
	public static final String BUSI_TYPE_HTTP = "HTTP";
	
	public static final String REQ_TYPE_GET = "GET";
	public static final String REQ_TYPE_POST = "POST";
	
	/*-----------------------------------------------------*/
	public static final String BUSI_CODE_HTTP_1_1 = "GET / HTTP/1.1";
	public static final String BUSI_CODE_FAVICON_ICO = "GET /favicon.ico HTTP/1.1";
	public static final String BUSI_CODE_FAVICON_ICO_NAME = "/favicon.ico";

	/**方法参数名称**/
	public static final String METHOD_PARAM_NAME = "funName";
	/**http服务key类型参数名称**/
	public static final String KEY_TYPE_PARAM_NAME = "keytype";
	/**签名参名称**/
	public static final String SIGN_PARAM_NAME = "sign";
	/**http参数分隔符**/
	public static final String HTTP_PARAM_SPLIT_CHAR = "?";
	
	/*-----------------------------------------------------*/
	public static final String CONTENT_LENGTH = "Content-Length:";

	/*-----------------------------------------------------*/
	public static final String SPLIT_FULL_CHAR = "\r\n\r\n";
}
