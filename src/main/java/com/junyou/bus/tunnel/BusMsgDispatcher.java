package com.junyou.bus.tunnel;

import com.hehj.easyexecutor.front.IEasyFrontend;
import com.junyou.utils.MsgPrintUtil;
import com.kernel.pool.executor.EasyExecutorRunnablePool;
import com.kernel.pool.executor.IBusinessExexutor;
import com.kernel.pool.executor.IRunnablePool;
import com.kernel.pool.executor.Message;
import com.kernel.pool.executor.MsgStatistics;
import com.kernel.pool.executor.RouteInfo;

/**
 * @description 客户端接入消息分发器
 * @author hehj
 * 2011-11-4 下午3:04:30
 */
public class BusMsgDispatcher {

	private ThreadLocal<IRunnablePool> runnableLocal = new ThreadLocal<IRunnablePool>();
	
	private BusRouteHelper routeHelper = new BusRouteHelper();
	
	private IBusinessExexutor businessExexutor;
	
	private IEasyFrontend easyFrontend;
	
	private MsgStatistics msgStatistics;
	
	public BusMsgDispatcher(IBusinessExexutor businessExexutor,IEasyFrontend easyFrontend,MsgStatistics msgStatistics) {
		this.businessExexutor = businessExexutor;
		this.easyFrontend = easyFrontend;
		this.msgStatistics = msgStatistics;
	}

	public void in(Object message) {
		Object[] sourceMsg = (Object[])message;
		Message msg = new Message(sourceMsg);
		
		
		Runnable runnable = getRunnablePool().getRunnable(msg);
		RouteInfo routeInfo = routeHelper.getRouteInfo(msg,(Integer) sourceMsg[2]);
		businessExexutor.execute(runnable, routeInfo);

		MsgPrintUtil.printMsg(msg,MsgPrintUtil.BUS_PRINT,MsgPrintUtil.BUS_PREFIX ,routeInfo.toString());
	}

	private IRunnablePool getRunnablePool(){
		IRunnablePool runnablePool = runnableLocal.get();
		if(null==runnablePool){
			runnablePool = new EasyExecutorRunnablePool(easyFrontend,msgStatistics);
			runnableLocal.set(runnablePool);
		}
		return runnablePool;
	}

}
