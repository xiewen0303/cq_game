package com.junyou.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @description
 */
public class ChuanQiLog {
	
	private static Logger INFO = LogManager.getLogger("info_logger");
	private static Logger LOG = LogManager.getLogger("error_logger");
	private static Logger START_LOG = LogManager.getLogger("start_logger");
	private static Logger STOP_LOG = LogManager.getLogger("stop_logger");
	private static Logger _24HOURLOG = LogManager.getLogger("_24Hour_logger");
	private static Logger _60MINLOG = LogManager.getLogger("_60min_logger");
	private static Logger _1MINLOG = LogManager.getLogger("_1min_logger");
	private static Logger PINGLOG = LogManager.getLogger("ping_logger");
	private static Logger FRAME_LOG = LogManager.getLogger("frame_logger");
	private static Logger CONFIG_LOG = LogManager.getLogger("config_error_logger");
	private static Logger DEVLOG = LogManager.getLogger("_dev_logger");
	private static Logger MEMORY_LOG = LogManager.getLogger("memory_logger");
	private static Logger IO_ERROR_LOGGER = LogManager.getLogger("io_error_logger");
	private static Logger STAGE_ERROR_LOGGER = LogManager.getLogger("stage_err_logger");
	private static Logger JY_LOGGER = LogManager.getLogger("jy_logger");
	
	/**
	 * 游戏启动日志
	 * @param message
	 * @param args
	 */
	public static void info(String message,Object... args){
		INFO.info(message,args);
	}
	
	/**
	 * 错误信息打印
	 * @param
	 */
	public static void error(String message,Exception e){
		LOG.error(message, e);
	}
	
	/**
	 * 游戏启动日志
	 * @param message
	 * @param args
	 */
	public static void startLog(String message,Object... args){
		START_LOG.error(message,args);
	}
	
	/**
	 * 游戏停机日志
	 * @param message
	 * @param args
	 */
	public static void stopLog(String message,Object... args){
		STOP_LOG.error(message,args);
	}
	/**
	 * 错误信息打印
	 * @param
	 */
	public static void error(String message,Object... args){
		LOG.error(message,args);
	}
	
	/**
	 * 框架错误信息打印(场景指令没成功执行等信息)
	 * @param message
	 * @param e
	 */
	public static void errorFrame(String message){
		FRAME_LOG.error(message);
	}
	
	/**
	 * 内存监控日志
	 * @param message
	 */
	public static void memInfo(String message){
		MEMORY_LOG.error(message);
	}
	
	/**
	 * 内存监控日志
	 * @param message
	 */
	public static void memInfo(String message,Exception e){
		MEMORY_LOG.error(message,e);
	}
	
	/**
	 * 框架错误信息打印(场景指令没成功执行等信息)
	 * @param message
	 * @param e
	 */
	public static void errorFrame(String message,Exception e){
		FRAME_LOG.error(message,e);
	}
	
	/**
	 * 框架错误信息打印(场景指令没成功执行等信息)
	 * @param message
	 * @param e
	 */
	public static void errorFrame(String message,Object... args){
		FRAME_LOG.error(message,args);
	}
	
	public static boolean isFrameDebug(){
		return FRAME_LOG.isDebugEnabled();
	}
	
	/**
	 * 配置错误信息打印(配置指令没成功执行等信息)
	 * @param message
	 * @param e
	 */
	public static void errorConfig(String message,Object... args){
		CONFIG_LOG.error(message, args);
	}
	
	/**
	 * 配置错误信息打印(配置指令没成功执行等信息)
	 * @param message
	 * @param e
	 */
	public static void errorConfig(String message,Exception e){
		CONFIG_LOG.error(message, e);
	}
	
	/**
	 * 配置错误信息打印(配置指令没成功执行等信息)
	 * @param message
	 * @param e
	 */
	public static void errorConfig(String message){
		CONFIG_LOG.error(message);
	}

	/**
	 * 打印DEBUG
	 * @param message
	 */
	public static void debug(String message){
		if(LOG.isDebugEnabled()){
			LOG.debug(message);
		}
	}
	/**
	 * 打印DEBUG
	 * @param message
	 */
	public static void debug(String message,Object... args){
		if(LOG.isDebugEnabled()){
			LOG.debug(message,args);
		}
	}
	
	
	/**
	 * 错误信息打印
	 * @param
	 */
	public static void error(String message){
		LOG.error(message);
	}

	/**
	 * 1天解析间隔信息打印
	 * @param
	 */
	public static void _24HourLog(String message){
		_24HOURLOG.error(message);
	}

	/**
	 * 60分钟解析间隔信息打印
	 * @param
	 */
	public static void _60MinuteLog(String message){
		_60MINLOG.error(message);
	}

	/**
	 * 30分钟解析间隔信息打印
	 * @param
	 */
	public static void _1MinuteLog(String message){
		_1MINLOG.error(message);
	}
	
	/**
	 * 校验Ping失败踢玩家下线的日志
	 * @param
	 */
	public static void _pingLog(String message){
		PINGLOG.error(message);
	}
	
	/**
	 * 开发人员日志(info级别，用于BUG查找)
	 * @param msg
	 * @param parameters
	 */
	public static void deployInfo(String msg){
		DEVLOG.info(msg);
	}	
	
	public static void ioError(String message){
		IO_ERROR_LOGGER.error(message);
	}
	
	public static void ioError(String message,Throwable e){
		IO_ERROR_LOGGER.error(message,e);
	}
	
	public static void ioError(String message,Object... args){
		IO_ERROR_LOGGER.error(message,args);
	}
	
	public static void stageError(String message){
		STAGE_ERROR_LOGGER.error(message);
	}
	
	public static void stageError(String message,Throwable e){
		STAGE_ERROR_LOGGER.error(message,e);
	}
	
	public static void stageError(String message,Object... args){
		STAGE_ERROR_LOGGER.error(message,args);
	}
	
	public static void jyLog(String message){
		JY_LOGGER.error(message);
	}
	
}
