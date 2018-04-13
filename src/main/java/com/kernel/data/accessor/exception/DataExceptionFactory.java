package com.kernel.data.accessor.exception;


/**
 * @description 异常工厂
 * @author ShiJie Chi
 * @created 2011-11-4上午9:28:31
 */
public class DataExceptionFactory {

  public static RuntimeException getException(String message,Exception e){
	  return new DataException(message, e);
  }
  
  public static RuntimeException getException(String message){
	  return new DataException(message);
  }
  
}
