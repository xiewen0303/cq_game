package com.junyou.stage.tunnel;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.kernel.pool.executor.Message;
import com.kernel.pool.executor.RouteInfo;

public class StageRouteHelper {

	public RouteInfo getRouteInfo(Message message){
		RouteInfo routeInfo = new RouteInfo(CmdGroupInfo.STAGE_GROUP);
		routeInfo.setInfo(message.getStageId());
		return routeInfo;
	}
	
}
