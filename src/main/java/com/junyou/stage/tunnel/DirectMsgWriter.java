/**
 * 
 */
package com.junyou.stage.tunnel;

import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;

/**
 * @description 带缓存消息输出器（线程安全）
 * @author ShiJie Chi
 * @created 2011-12-6下午1:45:54
 */
public class DirectMsgWriter implements IMsgWriter {
	
	private static DirectMsgWriter writer = new DirectMsgWriter();
	
	private DirectMsgWriter() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void writeMsg(Long[] roleIds, Short command, Object data) {
		
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2Many(roleIds, command, data);
		}else{
			StageMsgSender.send2Many(roleIds, command, data);
		}
	}
	
	@Override
	public void writeMsg(Long roleId, Short command, Object result) {
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2One(roleId, command, result);
		}else{
			StageMsgSender.send2One(roleId, command, result);
		}
	}
	
	@Override
	public void writePublicMsg(Long roleId, Short command, Object result) {
		StageMsgSender.send2Public(roleId, command, result);
	}

	/**
	 * @param
	 */
	@Override
	public void flush() {
	}
	
	/**
	 * 获取实例
	 * @param
	 */
	public static IMsgWriter getInstance() {
		return writer;
	}


}
