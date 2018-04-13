package com.junyou.utils.exception;

import org.apache.logging.log4j.message.ParameterizedMessageFactory;


/**
 * 游戏自定义异常
 * @author DaoZheng Yuan
 * 2014年11月15日 上午11:15:03
 */
public class JunYouCustomException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public JunYouCustomException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public JunYouCustomException(String message) {
		super(message);
	}
	
	public JunYouCustomException(String message,Object... params){
		super(ParameterizedMessageFactory.INSTANCE.newMessage(message, params).getFormattedMessage());
	}

	public JunYouCustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public JunYouCustomException(Throwable cause) {
		super(cause);
	}
}
