package com.junyou.bus.share.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hehj.easyexecutor.Interceptor.IInterceptor;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gs.share.thread.RoleidThreadShare;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.kernel.pool.executor.Message;

public class RoleStateInterceptor implements IInterceptor {

	private Map<Short, Short> exceptCmds = new HashMap<Short, Short>();
	{
		exceptCmds.put(InnerCmdType.BUS_INIT_IN, InnerCmdType.BUS_INIT_IN);
		exceptCmds.put(InnerCmdType.BUS_INIT_OUT, InnerCmdType.BUS_INIT_OUT);
	}
	
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	@Override
	public boolean after(Object message) {
		return true;
	}

	@Override
	public boolean before(Object message) {
		Long roleid = ((Message)message).getRoleId();
		 
		// 例外
		if(exceptCmds.containsKey(((Message)message).getCommand())) return true;
		RoleidThreadShare.setRoleId(roleid);
		return publicRoleStateExportService.isPublicOnline(roleid);
	}

}
