package com.junyou.utils;

import com.hehj.easyexecutor.cmd.EasyKuafuCmdInfo;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.messageswap.MsgUtil;
import com.junyou.utils.codec.AmfUtil;

public class KuafuCmdUtil {

	/**
	 * 是否往跨服服务器转发本条场景指令
	 * @param cmd
	 * @return
	 */
	private static boolean isSwapInKuafu(short cmd, Object[] msg){
		//是跨服时直接转发的指令 && 角色正在跨服
		if( isSwapInKuafu(cmd) && KuafuManager.kuafuIng(MsgUtil.getRoleId(msg))){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 是否为直接转发的指令
	 */
	public static boolean isDirectSwap(short cmd){
		return EasyKuafuCmdInfo.getDirectSwapCmds().containsKey(cmd);
	}
	/**
	 * 是否为在跨服时直接转发的指令
	 */
	public static boolean isSwapInKuafu(short cmd){
		return EasyKuafuCmdInfo.getKuafuDirectSwapCmds().containsKey(cmd);
	}
	/**
	 * 是否为跨服服务器分发出来的需要源服务器处理的指令
	 */
	public static boolean isSourceHandleCmd(short cmd){
		return EasyKuafuCmdInfo.getSourceHandleCmds().containsKey(cmd);
	}
	/**
	 * 是否为跨服服务器分发出来的全服（跨服）指令
	 */
	public static boolean isSendToManyCmd(short cmd){
		return InnerCmdType.INNER_KF_TO_MANY_CLIENT == cmd;
	}
	/**
	 * 是否为跨服服务器分发出来的单人（跨服）指令
	 */
	public static boolean isSendToOneCmd(short cmd){
		return InnerCmdType.INNER_KF_TO_ONE_CLIENT == cmd;
	}
	/**
	 * 是否是转发指令
	 * @param cmd
	 * @return
	 */
	public static boolean isZhuanfaCmd(short cmd){
		return InnerCmdType.INNER_KF_TO_ONE_CLIENT == cmd || InnerCmdType.INNER_KF_TO_MANY_CLIENT == cmd;
	}
	/**
	 * 是否为可转发的跨服指令
	 * @param cmd
	 * @return
	 */
	public static boolean swapCmdNow(short cmd, Object[] msg){
		//当前服务器不是跨服服务器  && ( 直接转发的指令||跨服状态下转发的指令 )
		if(!KuafuConfigPropUtil.isKuafuServer() && ((isDirectSwap(cmd)) || isSwapInKuafu(cmd, msg)) ){
			return true;
		}
		return false;
	}
	
	/**
	 * 指令是否被禁止(跨服期间一些指令会被禁止)
	 */
	public static boolean isForbidCmds(short cmd, Object[] msg){
		boolean isFor =  !KuafuConfigPropUtil.isKuafuServer() && (EasyKuafuCmdInfo.getStopHandleCmds().containsKey(cmd) && roleIsInKuafu(msg));
		if(isFor){
			Long roleId = MsgUtil.getRoleId(msg);
			GameSession session = GameSessionManager.getInstance().getSession4RoleId(roleId);
			if(session != null){
				session.sendMsg(AmfUtil.convertMsg2Bytes(cmd, AppErrorCode.KUAFUING_CANNOT_DO_THIS));
			}
		}
		return isFor;
	}
	
	/**
	 * 角色是否正在跨服
	 * @param msg
	 * @return
	 */
	private static boolean roleIsInKuafu(Object[] msg){
		return KuafuManager.kuafuIng(MsgUtil.getRoleId(msg));
	}
	
}
