package com.junyou.public_.tunnel;

import static com.hehj.easyexecutor.cmd.CmdGroupInfo.GUILD_GROUP;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.LOGIN_GROUP;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.NODE_CONTROL_GROUP;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.PUBLIC_GROUP;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.pool.executor.RouteInfo;

public class PublicRouteHelper {

	public RouteInfo getRouteInfo(Message message,int destType){
		
		RouteInfo routeInfo = null;
		switch (destType) {
		case 4: // login
			Object data = message.getData();
			String info = null;
			if(data instanceof Double){
				Long roleId = LongUtils.obj2long(data);
				info = roleId.toString();
			}else{
				info = (String) message.<Object[]>getData()[0];
			}
			
			routeInfo = new RouteInfo(LOGIN_GROUP);
			routeInfo.setInfo(info);
			break;
		case 6: // public
			Short cmd = message.getCommand();
			if(CmdGroupInfo.isModule(cmd, NODE_CONTROL_GROUP)){
				routeInfo = new RouteInfo(NODE_CONTROL_GROUP);
				routeInfo.setInfo(message.getRoleId().toString());
			}else if(CmdGroupInfo.isModule(cmd, GUILD_GROUP)){
				routeInfo = new RouteInfo(GUILD_GROUP);
				routeInfo.setInfo(message.getRoleId().toString());
			}else{
				routeInfo = new RouteInfo(PUBLIC_GROUP);
				routeInfo.setInfo(CmdGroupInfo.getCmdModule(cmd));
			}
			break;
		}
		
		return routeInfo;
	}
	
}
