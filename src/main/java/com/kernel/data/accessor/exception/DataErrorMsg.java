/**
 * 
 */
package com.kernel.data.accessor.exception;


/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-3下午3:46:43
 */
public class DataErrorMsg {

	public static String REDUPLICATE_PRIMARY_KEY = "reduplicate entity insert!";
	
	public static String ERROR_FOR_UPDATE = "no exist entity for update!";
	
	public static String ERROR_FOR_DELETE = "no exist entity for delete!";
	
	public static String ERROR_NOT_SUPPORT_LOADALL = "unsupport loadAll!";
	
	public static String ERROR_UNSUPPORT_OPERATION = "unsupport operation";
	
	public static String getModelErrorMsg(String errorMsg,String model){
		return errorMsg + " model:" + model;
	}
	
}
