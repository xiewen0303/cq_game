package com.hehj.easyexecutor.cmd;

import java.util.HashMap;
import java.util.Map;

import com.hehj.easyexecutor.enumeration.EasyKuafuType;

/**
 * @author DaoZheng Yuan
 * 2014-11-13 下午12:26:27
 */
public class EasyKuafuCmdInfo {

	/**
	 * 直接转发到跨服服务器的指令
	 */
	private final static Map<Short, String> DIRECT_SWAP_CMDS = new HashMap<>();
	/**
	 * 跨服时直接转发到跨服服务器的指令
	 */
	private final static Map<Short, String> KUAFU_DIRECT_SWAP_CMDS = new HashMap<>();
	/**
	 * 跨服服务器分发出来的需要源服务器处理的指令
	 */
	private final static Map<Short, String> SOURCE_HANDLE_CMDS = new HashMap<>();
	/**
	 * 跨服期间禁止的指令
	 */
	private final static Map<Short, String> STOP_HANDLE_CMDS = new HashMap<>();
	
	
	/**
	 * 跨服指令注册
	 * @param cmd
	 * @param type
	 */
	public static void registerKuafuCmds(short cmd,EasyKuafuType type){
		
		switch (type) {
		
		case DIRECT_SWAP_TYPE:
			
			//直接转发跨服的指令
			DIRECT_SWAP_CMDS.put(cmd, null);
			break;
		case KFING_S2KF_TYPE:
			
			//跨服时才从源服转发到跨服的指令
			KUAFU_DIRECT_SWAP_CMDS.put(cmd, null);
			break;
		case KF2S_HANDLE_TYPE:
			
			//跨服服务器分发出来，需要源服务器处理的指令
			SOURCE_HANDLE_CMDS.put(cmd, null);
			break;
		case KFING_STOP_TYPE:
			
			//跨服期间禁止的指令
			STOP_HANDLE_CMDS.put(cmd, null);
			break;

		default:
			//不需要跨服处理的指令
			break;
		}
	}
	
	/**
	 * 直接转发跨服的指令
	 * @return
	 */
	public static Map<Short, String> getDirectSwapCmds(){
		return DIRECT_SWAP_CMDS;
	}
	
	/**
	 * 跨服时才从源服转发到跨服的指令
	 * @return
	 */
	public static Map<Short, String> getKuafuDirectSwapCmds(){
		return KUAFU_DIRECT_SWAP_CMDS;
	}
	
	/**
	 * 跨服时才从源服转发到跨服的指令
	 * @return
	 */
	public static Map<Short, String> getSourceHandleCmds(){
		return SOURCE_HANDLE_CMDS;
	}
	
	/**
	 * 跨服期间禁止的指令
	 * @return
	 */
	public static Map<Short, String> getStopHandleCmds(){
		return STOP_HANDLE_CMDS;
	}
}
