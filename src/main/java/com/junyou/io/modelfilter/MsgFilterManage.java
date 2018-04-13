package com.junyou.io.modelfilter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MsgFilterManage {
	
	/**
	 * 已关闭临时关闭的模块
	 * key:服务器模块名称
	 * value:功能分布表的模块对应serverid
	 */
	private static Map<String,Integer> CLOSE_MODULES;
	
	/**
	 * 关闭的正整数指令
	 * key:正整数指令
	 */
	private static Map<Short,Object> CLOSE_CMDS;
	
	/**
	 * 是否打开主端口消息通道
	 */
	private static volatile boolean OPEN_MSG = false;
	
	
	/**
	 * 消息入口是否打开
	 * @return true:已打开,false:未打开
	 */
	public static boolean isOpenMsg(){
		return OPEN_MSG;
	}
	
	/**
	 * 打开端口消息通道
	 */
	public static void changeOpenMsg(){
		OPEN_MSG = true;
	}
	
	/**
	 * 关闭主端口消息通道
	 */
	public static void changeCloseMsg(){
		OPEN_MSG = false;
	}
	
	/**
	 * 验证模块是否关闭
	 * @param modeName
	 * @return true:已关闭
	 */
	public static boolean checkIsClose(String modeName){
		if(CLOSE_MODULES == null){
			return false;
		}
		
		return CLOSE_MODULES.containsKey(modeName); 
	}
	
	/**
	 * 指令是否关闭
	 * @param cmd 指令
	 * @return true:已关闭
	 */
	public static boolean checkIsClose(Short cmd){
		if(CLOSE_CMDS == null){
			return false;
		}
		
		return CLOSE_CMDS.containsKey(cmd);
	}
	
	/**
	 * 添加临时半闭的模块
	 * @param modName
	 * @param modId
	 */
	public static void add(String modName,Integer modId){
		if(CLOSE_MODULES == null){
			CLOSE_MODULES = new HashMap<>();
		}
		
		CLOSE_MODULES.put(modName, modId);
	} 
	
	/**
	 * 添加临时半闭的指令
	 * @param cmd
	 */
	public static void addCmd(Short cmd){
		if(CLOSE_CMDS == null){
			CLOSE_CMDS = new HashMap<>();
		}
		
		CLOSE_CMDS.put(cmd, null);
	}
	
	/**
	 * 移除不需要关闭的msgCode
	 * @param keys
	 */
	public static Integer removeMod(String modName){ 
		if(CLOSE_MODULES == null){
			return null;
		}else{
			Integer modId = CLOSE_MODULES.remove(modName);
			return modId; 
		}
	}

	/**
	 * 移除不需要关闭的指令
	 * @param cmd
	 * @return
	 */
	public static Object removeCmd(Short cmd){
		if(CLOSE_CMDS == null){
			return null;
		}else{
			return CLOSE_CMDS.remove(cmd);
		}
	}
	
	/**
	 * 获取关闭的模块功能分布表的配置serverid
	 * @param modName
	 * @return
	 */
	public static Integer getCloseModIdByModName(String modName){
		if(CLOSE_MODULES == null){
			return null;
		}else{
			return CLOSE_MODULES.get(modName); 
		}
	}
	
	/**
	 * 获取已关闭的模块名称
	 * @return
	 */
	public static Set<String> getCloseModelInfos() {
		if(CLOSE_MODULES == null){
			return null;
		}else{
			return CLOSE_MODULES.keySet();
		}
	} 
	
	/**
	 * 获取所有已关闭的指令
	 * @return
	 */
	public static Set<Short> getCloseCmdInfos(){
		if(CLOSE_CMDS == null){
			return null;
		}else{
			return CLOSE_CMDS.keySet();
		}
	}
	
	/**
	 * 获取已关闭的模块功能分布表id
	 * @return
	 */
	public static Collection<Integer> getCloseModelClientIds(){
		if(CLOSE_MODULES == null){
			return null;
		}else{
			return CLOSE_MODULES.values();
		}
	}
}
