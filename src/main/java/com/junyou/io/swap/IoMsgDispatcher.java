package com.junyou.io.swap;

import com.hehj.easyexecutor.front.IEasyFrontend;
import com.junyou.io.Message.IoMessage;
import com.kernel.pool.executor.EasyExecutorRunnablePool;
import com.kernel.pool.executor.IBusinessExexutor;
import com.kernel.pool.executor.IRunnablePool;
import com.kernel.pool.executor.Message;
import com.kernel.pool.executor.MsgStatistics;

/**
 * @description 客户端接入消息分发器
 * @author hehj
 * 2011-11-4 下午3:04:30
 */
public class IoMsgDispatcher {

	private ThreadLocal<IRunnablePool> runnableLocal = new ThreadLocal<IRunnablePool>();
	
	private IoRouteHelper routeHelper = new IoRouteHelper();
	
	private IBusinessExexutor businessExexutor;
	
	private IEasyFrontend easyFrontend;
	
	private MsgStatistics msgStatistics;
	
	public IoMsgDispatcher(IBusinessExexutor businessExexutor,IEasyFrontend easyFrontend,MsgStatistics msgStatistics) {
		this.businessExexutor = businessExexutor;
		this.easyFrontend = easyFrontend;
		this.msgStatistics = msgStatistics;
	}

	public void in(Object message) {
		
		Object[] sourceMsg = (Object[])message;
		Message msg = new IoMessage(sourceMsg);
		
		Runnable runnable = getRunnablePool().getRunnable(msg);
		businessExexutor.execute(runnable, routeHelper.getRouteInfo(msg,(Integer) sourceMsg[3]));
		
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
