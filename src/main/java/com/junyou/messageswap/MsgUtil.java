package com.junyou.messageswap;

/**
 * @description
 * @author hehj
 * 2011-11-25 下午2:41:52
 */
public class MsgUtil {

	/*
	 * 消息交换协议
	 * 1、数据结构(object[] msg)
	 * 2、索引注释
	 * 		msg[0]:指令
	 * 		msg[1]:数据
	 
	 * 		msg[5]:会话id (输入时由连接管理组件填充，输出时由swap填充)
	 * 		msg[6]:连接ip 由连接管理组件填充（输出时不填充）

	 * 		msg[2]:dest_type 消息输入组件类型 swap用 由组件发送时填充
	 * 		msg[3]:from_type 消息来源 swap用 由组件发送时填充
	 * 		msg[9]:消息输出标记0:会话id，1：角色id swap用 由组件发送时填充
	 * 		msg[4]:broadcast_type 消息广播类型1个目标 多个目标 全部目标 最终消息分发用 由组件发送时填充
	 
	 * 		msg[7]:目标角色id[s] 组件线程分发用 由swap填充
	 * 		msg[8]:目标角色场景id 组件线程分发用 由swap填充
	 * 		msg[10]:token值，适用内部定时消息
	 */
	

	
	/**
	 * 取消息指令
	 * @param msg
	 * @return
	 */
	public static Short getCommand(Object[] msg){
		return (Short) msg[0];
	}
	
	public static Object getMsgData(Object[] msg){
		return msg[1];
	}
	
	/**
	 * 取会话id
	 * @param msg
	 * @return
	 */
	public static long getSessionId(Object[] msg){
		return (Long)msg[5];
	}
	
	/**
	 * 取输出标记
	 * @param msg
	 * @return
	 */
	public static int getOutSign(Object[] msg){
		return (Integer)msg[9];
	}
	
	/**
	 * 取消息目标角色id
	 * @param msg
	 * @return
	 */
	public static Long getRoleId(Object[] msg){
		return (Long) msg[7];
	}

	/**
	 * 取消息目标场景id
	 * @param msg
	 * @return
	 */
	public static String getStageId(Object[] msg){
		return (String) msg[8];
	}

	/**
	 * 取消息目标角色ids
	 * @param msg
	 * @return
	 */
	public static String[] getRoleIds(Object[] msg){
		return (String[]) msg[7];
	}
	
	/**
	 * 取消息广播类型
	 * @param msg
	 * @return
	 */
	public static int getBroadcastType(Object[] msg){
		return (Integer)msg[4];
	}

	/**
	 * 获取消息目标类型
	 * @param msg
	 * @return
	 */
	public static int getDestType(Object[] msg){
		return (Integer)msg[2];
	}

	/**
	 * 获取消息来源类型
	 * @param msg
	 * @return
	 */
	public static int getFromType(Object[] msg){
		return (Integer)msg[3];
	}
}
