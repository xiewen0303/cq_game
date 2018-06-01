package com.kernel.data.write.async;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.junyou.log.ChuanQiLog;
import com.kernel.pool.executor.ThreadNameFactory;

/**
 * @description 异步回写控制中心
 * @author hehj
 * 2011-11-8 下午3:04:26
 */
public class AsyncWriteCenter {

	private static ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadNameFactory("AsyncWrite-exc-"));
	
	private static LinkedBlockingQueue<AsyncWriteUnit>[] writeQueues;
	private static final ConcurrentMap<String, LinkedBlockingQueue<AsyncWriteUnit>> ruleMap = new ConcurrentHashMap<String, LinkedBlockingQueue<AsyncWriteUnit>>();
	private static final ConcurrentMap<String, AsyncWriteWorker> workers = new ConcurrentHashMap<String, AsyncWriteWorker>();
	
	private static int assignCount = 0;

	private static SqlMapClient sqlMapClient;
	
	/**
	 * 初始化中心
	 * @param writerSize 要启动的回写并发数
	 */
	public static void start(int writerSize,SqlMapClient ibatisSqlMapClient){
		
		if(writerSize <=0) throw new RuntimeException("size of writer must more than 0,[writerSize > 0].");
		if(null == ibatisSqlMapClient) throw new RuntimeException("ibatisSqlMapClient can't be null.");
		
		sqlMapClient = ibatisSqlMapClient;
		writeQueues = new LinkedBlockingQueue[writerSize];
		
		// init writers
		for(int i=0;i<writerSize;i++){
			
			String writerName = "AsyncWriteCenter-Writer-"+i;
			final LinkedBlockingQueue<AsyncWriteUnit> writeQueue = new LinkedBlockingQueue<AsyncWriteUnit>(); 

			new Thread(new Writer(writeQueue),writerName).start();
			writeQueues[i] =  writeQueue;
		}

		// init worker cleaner
		Thread cleanThread = new Thread(new Runnable() {
			
			@Override
			public void run() {

				for(;;){
					
					try {
						
						Thread.sleep(AsyncWriteWorker.CleanedGap);
						
						for(AsyncWriteWorker worker : workers.values()){
							
							if(worker.canCleaned()){
								synchronized (workers) {
									workers.remove(worker.getId());
								}
								ChuanQiLog.error(worker.getId() + " is cleaned.");
							}
							
						}
						
					} catch (Exception e) {
					}
					
				}
				
			}
		},"AsyncWriteCenter-Cleaner");
		cleanThread.setDaemon(true);
		cleanThread.start();
		
	}
	
	public static SqlMapClient getSqlMapClient(){
		return sqlMapClient;
	}
	
	/**
	 * 关闭中心 
	 */
	public static void stop(){
		
		// close all workers 
		for(AsyncWriteWorker worker : workers.values()){
			try{
				worker.close();
			}catch (Exception e) {
				ChuanQiLog.error("error on close worker["+worker.getId()+"]",e);
			}
		}
		
		// wait data sync until finished.
		ChuanQiLog.error("server stop info:start to data sync...");
		
		for(;;){
			
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
			}
		
			if(getEntityOperateSize() <=0){
				break;
			}
		
		}

		ChuanQiLog.error("server stop info:data sync finished.");
		
	}

	private static long getEntityOperateSize(){
		int size = 0;
		for(LinkedBlockingQueue<AsyncWriteUnit> queue : writeQueues){
			size += queue.size();
		}
		return size;

	}
	
	/**
	 * 从回写中心获取定时调度对象{@link ScheduledExecutorService}
	 * @return
	 */
	public static ScheduledExecutorService getScheduledExecutor(){
		return scheduledExecutor;
	}
	
	/**
	 * 从回写中心获取一个{@link AsyncWriteWorker}
	 * @param workerid {@link AsyncWriteWorker}的id标识
	 */
	public static AsyncWriteWorker getAsyncWriteWorker(String workerid){
		synchronized (workers) {
			return workers.get(workerid);
		}
	}
	
	/**
	 * 在回写中心初始化一个{@link AsyncWriteWorker}
	 * @param workerid {@link AsyncWriteWorker}的id标识
	 * @param period 数据回写的时间周期（单位：秒）
	 */
	public static AsyncWriteWorker openAsyncWriteWorker(String workerid,int period){
		
		if(period > 15 * 60 * 1000) throw new RuntimeException("param[period] can't be more than 15 minutes.");
		
		synchronized (workers) {
			AsyncWriteWorker worker = workers.get(workerid);
			if(null == worker){
				worker = new AsyncWriteWorker(workerid,period);
				workers.put(workerid, worker);
			}
			worker.open();
			return worker;
		}
	}
	
	/**
	 * 关闭一个{@link AsyncWriteWorker}
	 * @param workerid {@link AsyncWriteWorker}的id标识
	 */
	public static void closeAsyncWriteWorker(String workerid){
		AsyncWriteWorker worker = workers.get(workerid);
		if(null != worker){
			worker.close();
		}
	}
	
	/**
	 * 触发一次回写动作
	 * @param unitId {@link AsyncWriteWorker}中要回写的数据集标志
	 * @param worker {@link AsyncWriteWorker}
	 */
	public static void addAsyncWirteUnit(String unitId,AsyncWriteWorker worker){
		getWriteQueue(worker).add(new AsyncWriteUnit(unitId,worker));
	}
	
	/**
	 * 回写分发
	 * @param worker {@link AsyncWriteWorker}
	 * @return
	 */
	private static LinkedBlockingQueue<AsyncWriteUnit> getWriteQueue(AsyncWriteWorker worker){
		
		String workerId = worker.getId();
		
		LinkedBlockingQueue<AsyncWriteUnit> queue = ruleMap.get(workerId);
		if(null == queue){
			synchronized (ruleMap) {
				queue = ruleMap.get(workerId);
				if(null == queue){
					queue = writeQueues[ assignCount ];
					ruleMap.put(workerId, queue);
					assignCount++;
					if(assignCount == writeQueues.length){
						assignCount = 0;
					}
				}
			}
		}
		return queue;

	}
	
	/**
	 * @description 回写对象
	 * @author hehj
	 * 2011-11-8 下午3:47:42
	 */
	private static class Writer implements Runnable{
		
		private LinkedBlockingQueue<AsyncWriteUnit> writeQueue;
		
		public Writer(LinkedBlockingQueue<AsyncWriteUnit> writeQueue) {
			this.writeQueue = writeQueue;
		}
		
		@Override
		public void run() {

			while(true){
				try {
					
					AsyncWriteUnit asyncWriteUnit = writeQueue.take();
					asyncWriteUnit.flush();

				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			}

			
		}
		
	}
}
