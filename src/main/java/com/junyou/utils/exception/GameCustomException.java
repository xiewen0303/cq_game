package com.junyou.utils.exception;

import org.apache.logging.log4j.message.ParameterizedMessageFactory;


/**
 * 游戏自定义异常
 */
public class GameCustomException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public GameCustomException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public GameCustomException(String message) {
		super(message);
	}
	
	public GameCustomException(String message, Object... params){
		super(ParameterizedMessageFactory.INSTANCE.newMessage(message, params).getFormattedMessage());
	}

	public GameCustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameCustomException(Throwable cause) {
		super(cause);
	}
}
