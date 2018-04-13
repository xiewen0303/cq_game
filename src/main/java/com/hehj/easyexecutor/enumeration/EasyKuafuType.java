package com.hehj.easyexecutor.enumeration;

/**
 * 跨服指令类型
 * @author DaoZheng Yuan
 * 2014-11-12 下午2:09:49
 */
public enum EasyKuafuType {

	/**
	 * 不需要跨服处理的指令
	 */
	NOT_KUAFU_TYPE
	/**
	 * 直接转发跨服的指令
	 */
	,DIRECT_SWAP_TYPE
	/**
	 * 跨服时才从源服转发到跨服的指令
	 */
	,KFING_S2KF_TYPE
	/**
	 * 跨服服务器分发出来，需要源服务器处理的指令
	 */
	,KF2S_HANDLE_TYPE
	/**
	 * 跨服期间停用的指令
	 */
	,KFING_STOP_TYPE
	;
	
}
