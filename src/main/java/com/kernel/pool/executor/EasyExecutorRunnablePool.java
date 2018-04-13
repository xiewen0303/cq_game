package com.kernel.pool.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hehj.easyexecutor.front.IEasyFrontend;
import com.junyou.log.ChuanQiLog;

/**
 * @description
 * 请求可执行任务池，缓存{@link Runnable}对象供下次使用
 * @author hehj
 * @created 2010-2-8 下午04:46:29
 * @param <T>
 */
public class EasyExecutorRunnablePool implements IRunnablePool {
	
	private static final int RUNNABLE_INIT_SIZE = 6144;
	
	private int runnableCount = RUNNABLE_INIT_SIZE;
	
	private BlockingQueue<RunnableImpl> runnables = new LinkedBlockingQueue<RunnableImpl>();
	
	private IEasyFrontend easyFrontend;
	
	private MsgStatistics msgStatistics;

	public EasyExecutorRunnablePool(IEasyFrontend easyFrontend,MsgStatistics msgStatistics){
		if(null==easyFrontend) throw new NullPointerException("easyFrontend can't be null.");
		if(null==msgStatistics) throw new NullPointerException("msgStatistics can't be null.");
		this.easyFrontend = easyFrontend;
		this.msgStatistics = msgStatistics;
		init();
	}
	
	private void init(){

		for(int i=0;i<RUNNABLE_INIT_SIZE;i++){
		
			runnables.add(createRunnable());
		
		}
		
	}
	
	public Runnable getRunnable(Message command){
		
		RunnableImpl runnable = runnables.poll();
		if(null==runnable){
			runnable = createRunnable();
			ChuanQiLog.errorFrame("new pool object for thread[{},{}] {}-{} ,total size {}",Thread.currentThread().getId(),Thread.currentThread().getName(),command.getCommand(),command.getMsgSource()[0],++runnableCount);
//			ChuanQiLog.errorFrame(new StringBuilder("new pool object for thread[").append(Thread.currentThread().getId()).append(",").append(Thread.currentThread().getName()).append("] ").append(command.getCommand()).append(",total size ").append(++runnableCount).toString());
		}
		runnable.init(command);
		
		return runnable;
		
	}

	private RunnableImpl createRunnable(){
		if(msgStatistics.isNeedRecorded()){
			return new StatisticRunnableImpl();
		}else{
			return new RunnableImpl();
		}
	}
	
	private class StatisticRunnableImpl extends RunnableImpl{
		
		@Override
		public void run() {
			long start = System.nanoTime();
			Short cmd = msg.getCommand();
			super.run();
			msgStatistics.record(cmd, System.nanoTime() - start);
		}
		
	}
	
	/**
	 * @description
	 * 可执行对象
	 * <p>
	 * 将请求提交给{@link IMesaageExecutor}处理，并将处理结果输出
	 * @author hehj
	 * @created 2009-9-5 下午01:59:28
	 */
	private class RunnableImpl implements Runnable{
		
		protected Message msg = null;
		
		public RunnableImpl() {}
		
		private void init(Message message){
			if(null==message) throw new NullPointerException("command can't be null.");
			this.msg = message;
		}
		
		@Override
		public void run() {

			try{
				
				easyFrontend.execute(msg.getCommand(),msg);
			
			} catch (Exception e) {
				
				ChuanQiLog.errorFrame(msg.toString(),e);
				
			} finally {
				msg = null;
				if(runnables.size() < RUNNABLE_INIT_SIZE){
					runnables.add(this);
				}
			}
			
		}
		
	}

}
