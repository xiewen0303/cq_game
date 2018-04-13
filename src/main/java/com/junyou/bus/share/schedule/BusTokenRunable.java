package com.junyou.bus.share.schedule;

import com.junyou.bus.tunnel.BusMsgSender;
import com.kernel.token.ITokenRunnable;

/**
 * @description 
 * @author ShiJie Chi
 * @date 2012-3-27 下午6:07:55 
 */
public class BusTokenRunable implements ITokenRunnable {

	private Object[] token;

	private Long roleId;
	
	private Short command;
	
	private Object data;
	
	public BusTokenRunable(Long roleId,Short commond,Object data) {
		this.roleId = roleId;
		this.command = commond;
		this.data = data;
	}
	
	@Override
	public void run() {
		BusMsgSender.send2BusInnerToken(roleId, command, data, token);
	}

	@Override
	public void setToken(Object[] token) {
		this.token = token;
	}
}
