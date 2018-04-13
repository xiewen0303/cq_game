package com.junyou.bus.tunnel;

import static com.hehj.easyexecutor.cmd.CmdGroupInfo.BUS_CACHE_GROUP;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.BUS_INIT_GROUP;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.KUAFU_GROUP;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.STAGECONTROL_GROUP;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_bus;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_bus_init;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_kuafu_server;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_stage_control;

import com.kernel.pool.executor.Message;
import com.kernel.pool.executor.RouteInfo;

public class BusRouteHelper {

	public RouteInfo getRouteInfo(Message message,int destType){
		
		RouteInfo routeInfo = null;
		switch (destType) {
		case dest_type_bus: // bus cache
				routeInfo = new RouteInfo(BUS_CACHE_GROUP);
				routeInfo.setInfo(message.getRoleId().toString());
			break;
		case dest_type_stage_control: // stage-control
			routeInfo = new RouteInfo(STAGECONTROL_GROUP);
			routeInfo.setInfo(message.getRoleId().toString());
			break;
		case dest_type_bus_init: // bus-init
			routeInfo = new RouteInfo(BUS_INIT_GROUP);
			routeInfo.setInfo(message.getRoleId().toString());
			break;
		case dest_type_kuafu_server: // kuafu
			routeInfo = new RouteInfo(KUAFU_GROUP);
			routeInfo.setInfo(message.getRoleId().toString());
			break;
		}
		
		return routeInfo;
	}
	
}
