package com.kernel.data.write.async_1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.junyou.cmd.InnerCmdType;
import com.junyou.session.SessionConstants;
import com.junyou.utils.KuafuConfigPropUtil;
import com.kernel.pool.executor.JunYouThreadFactory;
import com.kernel.pool.executor.MsgStatistics;
import com.kernel.spring.SpringApplicationContext;
import com.kernel.sync.annotation.Sync;

public class AsyncWriteManager {

	private static final Log LOG = LogFactory.getLog("server_status_logger");
	
	private static final String COMPONENT_NAME = "!@#$async_write";
	
	private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new JunYouThreadFactory("AsyncWrite1-exc-"));
	
	private long writePeriod = 5 * 60 * 1000;
	
	private String name = "asyncWriteManager";
	
	private int writerSize = 1;
	
	private SqlMapClient sqlMapClient;

	private MsgStatistics msgStatistics;
	
	private Writer[] writers;
	
	private ConcurrentMap<Long, AsyncWriteDataContainer> dataContainers = new ConcurrentHashMap<Long, AsyncWriteDataContainer>();
	
	public void setName(String name) {
		this.name = name;
	}

	public void setWritePeriod(long writePeriod) {
		this.writePeriod = writePeriod * 60;
	}

	public void setWriterSize(int writerSize) {
		this.writerSize = writerSize;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	
	public void setMsgStatistics(MsgStatistics msgStatistics) {
		this.msgStatistics = msgStatistics;
	}

	private AsyncWriteManager getSpringAsyncWriteManager(){
		return (AsyncWriteManager) SpringApplicationContext.getApplicationContext().getBean(name);
	}
	
	public void init(){
		//跨服不初始化
		if(KuafuConfigPropUtil.isKuafuServer()){
			return;
		}
		
		// 校验必要变量
		if(writePeriod < 1 * 60 ) throw new RuntimeException("clean gap must more than 1 minute,[cleanGap > 1].");
		if(writerSize <=0) throw new RuntimeException("size of writer must more than 0,[writerSize > 0].");
		if(null == sqlMapClient) throw new RuntimeException("ibatis SqlMapClient can't be null.");
		
		// 启动回写并发实例
		writers = new Writer[writerSize];
		for(int i=0;i<writerSize;i++){
			if(msgStatistics.isNeedRecorded()){
				writers[i] =  new StatisticWriter("AsyncWriteManager[" + name + "]-Writer-"+i);
			}else{
				writers[i] =  new Writer("AsyncWriteManager[" + name + "]-Writer-"+i);
			}
		}
		
		// 启动清理线程
		Thread cleanThread = new Thread(new Runnable() {
			
			@Override
			public void run() {

				for(;;){
					
					try {
						
						Thread.sleep(getCleanGap());
						
						int cleanedCount = 0;
						AsyncWriteManager asyncWriteManager = getSpringAsyncWriteManager();
						for(AsyncWriteDataContainer dataContainer : dataContainers.values()){
							if( asyncWriteManager.cleanDataContainer(dataContainer.getIdentity(), dataContainer) ){
								cleanedCount ++;
							}
						}
						LOG.error("AsyncWriteManager[" + name + "]:cleaned "+ cleanedCount +",remain " + dataContainers.size());
						
						//临时打印 
						if(SessionConstants.TMP_STATIC_MSG){
							msgStatistics.flush2File();
						}
						
					} catch (Exception e) {
						LOG.error("",e);
					}
					
				}
				
			}
		},"AsyncWriteManager[" + name + "]-Cleaner");
		cleanThread.setDaemon(true);
		cleanThread.start();
		
	}
	
	public void close(){
		
		// close all workers 
		LOG.error("server stop info:data container close...");
		for(AsyncWriteDataContainer dataContainer: dataContainers.values()){
			try{
				dataContainer.close();
			}catch (Exception e) {
				LOG.error("error on close worker["+dataContainer.getIdentity()+"]",e);
			}
		}
		
		// wait data sync until finished.
		LOG.error("server stop info:start to data sync...");
		
		for(;;){
			
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
			}
		
			long syncSize = getSyncSize();
			if(syncSize <=0){
				
				boolean writing = false;
				for(Writer writer : writers){
					if(writer.isWriting()){
						writing = true;
						break;
					}
				}
				
				if(!writing) break;
			}

			LOG.error("server stop info:data sync remain "+syncSize);
		
		}

		LOG.error("server stop info:data sync finished.");
		

		
	}
	
	private long getSyncSize(){
		long syncSize = 0;
		for(Writer writer : writers){
			syncSize += writer.getBalance();
		}
		return syncSize;
	}
	
	/**
	 * 根据指定id获取一个{@link AsyncWriteDataContainer}
	 * @param identity
	 * @return
	 */
	@Sync(component=COMPONENT_NAME,indexes = {0})
	public AsyncWriteDataContainer getDataContainer(Long identity){
		AsyncWriteDataContainer dataContainer = dataContainers.get(identity);
		if(null == dataContainer){
			dataContainer = new AsyncWriteDataContainer(identity,this);
			dataContainers.put(identity, dataContainer);
		}
//		dataContainer.updateLastOperateTime();
		return dataContainer;
	}
	
	/**
	 * 关闭指定id的{@link AsyncWriteDataContainer}
	 * @param identity
	 */
	@Sync(component=COMPONENT_NAME,indexes = {0})
	public void flushDataContainer(Long identity){
		AsyncWriteDataContainer dataContainer = dataContainers.get(identity);
		if(null != dataContainer){
			dataContainer.flush();
		}
	}
	
	/**
	 * 移除指定id的{@link AsyncWriteDataContainer}}
	 * @param identity
	 * @param dataContainer
	 */
	@Sync(component=COMPONENT_NAME,indexes = {0})
	public boolean cleanDataContainer(Long identity,AsyncWriteDataContainer dataContainer){
		boolean cleaned = false;
		if(null != dataContainer && dataContainer.canClean()){
			dataContainer.flush();
			dataContainer.sync();
			dataContainers.remove(identity);
			cleaned = true;
		}
		return cleaned;
	}
	
	/**
	 * 同步指定id的所有数据变化并关闭关联的{@link AsyncWriteDataContainer}
	 * @param identity
	 */
	@Sync(component=COMPONENT_NAME,indexes = {0})
	public void syncAllDataChange(Long identity){
		AsyncWriteDataContainer dataContainer = dataContainers.get(identity);
		if(null != dataContainer){
			dataContainer.flush();
			dataContainer.sync();
		}
	}
	
	private Writer getWriter(){
		Writer lowest = null;
		long prebalance = 0;
		for(Writer writer : writers){
			if( null == lowest){
				lowest = writer;
				prebalance = writer.getBalance();
			}else{
				long balance = writer.getBalance();
				if(balance < prebalance){
					lowest = writer;
					prebalance = balance;
				}
			}
		}
		return lowest;
	}
	
	/**
	 * 分发一个数据回写请求
	 * @param dataContainer
	 */
	public void accept2write(AsyncWriteDataContainer dataContainer){
		getWriter().accept(dataContainer);
	}
	
	/**
	 * 获取定时调度器
	 * @return
	 */
	public ScheduledExecutorService getScheduledExecutor() {
		return this.scheduledExecutor;
	}


	/**
	 * 数据回写周期
	 * @return
	 */
	public long getWritePeriod() {
		return this.writePeriod;
	}

	/**
	 * 清理gap
	 * @return
	 */
	public long getCleanGap() {
//		return 10 * 1000;
		return this.writePeriod * 1000 * 3;
	}

	public SqlMapClient getSqlMapClient(){
		return this.sqlMapClient;
	}
	
	private class StatisticWriter extends Writer{

		public StatisticWriter(String name) {
			super(name);
		}
		
		@Override
		protected void sync(AsyncWriteDataContainer dataContainer) {
			long start = System.nanoTime();
			super.sync(dataContainer);
			msgStatistics.record(InnerCmdType.SYNC_CMD, (System.nanoTime() - start));
		}
	}
	
	/**
	 * @description 回写对象
	 * @author hehj
	 * 2011-11-8 下午3:47:42
	 */
	private class Writer implements Runnable{
		
		private LinkedBlockingQueue<AsyncWriteDataContainer> writeQueue =  new LinkedBlockingQueue<AsyncWriteDataContainer>();
		
		private volatile boolean writing = false;
		
		public Writer(String name) {
			new Thread(this,name).start();
		}
		
		public long getBalance(){
			return writeQueue.size();
		}
		
		public boolean isWriting() {
			return writing;
		}

		public void accept(AsyncWriteDataContainer dataContainer){
			this.writeQueue.add(dataContainer);
		}
		
		@Override
		public void run() {

			while(true){
				try {
					
					AsyncWriteDataContainer dataContainer = writeQueue.take();
				
					writing = true;
					sync(dataContainer);

				} catch (Exception e) {
					LOG.error("",e);
				}finally{
					writing = false;
				}
			}
			
		}
		
		protected void sync(AsyncWriteDataContainer dataContainer){
			dataContainer.sync();
		}
	}
}
