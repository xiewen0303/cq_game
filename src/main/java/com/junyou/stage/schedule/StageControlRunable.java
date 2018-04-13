package com.junyou.stage.schedule;

import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.token.ITokenRunnable;

/**
 * @description 
 * @author ShiJie Chi
 * @date 2012-3-27 下午6:07:55 
 */
public class StageControlRunable implements Runnable{

	protected Object[] token;
	
	private Short command;
	
	private Object data;
	
	private Long roleId;
	
	public StageControlRunable(Long roleId,Short commond,Object data) {
		this.command = commond;
		this.data = data;
		this.roleId = roleId;
	}
	
	@Override
	public void run() {
		StageMsgSender.send2StageControl(roleId, command, data);
	}


	
	
}
