package com.junyou.stage.tunnel;

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
public class StageMsgDispatcher {

	private ThreadLocal<IRunnablePool> runnableLocal = new ThreadLocal<IRunnablePool>();
	
	private StageRouteHelper routeHelper = new StageRouteHelper();
	
	private IBusinessExexutor businessExexutor;
	
	private IEasyFrontend easyFrontend;

	private MsgStatistics msgStatistics;
	
	public StageMsgDispatcher(IBusinessExexutor businessExexutor,IEasyFrontend easyFrontend,MsgStatistics msgStatistics) {
		this.businessExexutor = businessExexutor;
		this.easyFrontend = easyFrontend;
		this.msgStatistics = msgStatistics;
	}

	public void in(Object message) {
		
		Message msg = new Message((Object[])message);
		
		
		Runnable runnable = getRunnablePool().getRunnable(msg);
		RouteInfo routeInfo = routeHelper.getRouteInfo(msg);
		businessExexutor.execute(runnable, routeInfo);

		MsgPrintUtil.printMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX,routeInfo.toString());
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
