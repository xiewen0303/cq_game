package com.kernel.data.write.async;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.EntityMetaData;
import com.kernel.data.accessor.IbatisStatementMapper;
import com.kernel.data.dao.IEntity;

/**
 * @description 数据回写工作对象
 * @author hehj
 * 2011-11-8 下午4:14:21
 */
public class AsyncWriteWorker {

	private String id; 
	private int period; // 回写周期
	private TimeUnit timeunit; // 回写周期的时间单位
	private Runnable triggerTask = new Runnable() {
		
		@Override
		public void run() {
			flush();
		}
	};
	
	private boolean closed = false;
	private long operateTimeStamp = GameSystemTime.getSystemMillTime();
	public static final long CleanedGap = 30 * 60 * 1000;
	
	public AsyncWriteWorker(String workerid,int period) {
		this.id = workerid;
		this.period = period;
		this.timeunit = TimeUnit.SECONDS;
	}
	
	/**
	 * 获取{@link AsyncWriteWorker}的id标识
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * 打开回写对象、进入回写状态
	 */
	public void open(){
		synchronized (dataUpdateLock) {
			if(!closed) return;
			closed = false;
		}
		reschedule();
	}
	
	/**
	 * 开始一个新的回写调度
	 */
	private void reschedule(){
		synchronized (dataUpdateLock) {
			if(closed) return;
		}
		AsyncWriteCenter.getScheduledExecutor().schedule(triggerTask, period, timeunit);
	}
	
	/**
	 * 更新操作时间戳
	 */
	private void updateOperateTimeStamp(){
		this.operateTimeStamp = GameSystemTime.getSystemMillTime();
	}
	
	/**
	 * 关闭回写对象
	 */
	public void close(){
		
		synchronized (dataUpdateLock) {
			closed = true;
		}

		flush();
		
	}
	
	/**
	 * 判定是否可清除该回写对象
	 * @return
	 */
	public boolean canCleaned(){
		
		synchronized (dataUpdateLock) {
			return closed && writeQueues.size() <= 0 && ( (GameSystemTime.getSystemMillTime() - operateTimeStamp) > CleanedGap);
		}
		
	}
	
	private final Object dataUpdateLock = new Object(); 
	private final Object syncLock = new Object();
	/**
	 * 已变更需要回写的数据
	 */
	private LinkedHashMap<String, AsyncEntityOperate> entityOperates = new LinkedHashMap<String, AsyncEntityOperate>();
	/**
	 * 需要回写的数据队列
	 */
	private ConcurrentMap<String, LinkedHashMap<String, AsyncEntityOperate>> writeQueues = new ConcurrentHashMap<String, LinkedHashMap<String,AsyncEntityOperate>>();
	
	private UnitId unitId = new UnitId(); 
	
	/**
	 * 将需要回写的数据刷出到回写队列
	 */
	private void flush(){
		
		synchronized (dataUpdateLock) {

			try{
				
				if(entityOperates.size() > 0){
				
					String unitid = unitId.nextId();
					
					writeQueues.put(unitid, entityOperates);
					entityOperates = new LinkedHashMap<String, AsyncEntityOperate>();
					AsyncWriteCenter.addAsyncWirteUnit(unitid, this);
					
				}
				
			}catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			
		}

		updateOperateTimeStamp();
		// another schedule
		reschedule();
		
	}
	
	/**
	 * 同步所有数据到目标数据源
	 */
	public void syncAll(){
		synchronized (syncLock) {
		
			for(String id : writeQueues.keySet()){
				sync(id);
			}
			
		}
	}
	
	/**
	 * 将指定回写队列的数据同步到目标数据源
	 * @param id
	 */
	public void sync(String id){
		
		synchronized (syncLock) {
			LinkedHashMap<String,AsyncEntityOperate> dataOperates = writeQueues.get(id);
			try{
				if(null != dataOperates && dataOperates.size() > 0){
					
					SqlMapClient sqlMapClient = AsyncWriteCenter.getSqlMapClient();
					
					try {
						
						sqlMapClient.startTransaction();
						sqlMapClient.startBatch();
						
						for(AsyncEntityOperate entityOperate : dataOperates.values()){
							
							try{
								
								execute(entityOperate,sqlMapClient);
								
							} catch (Exception e) {
								ChuanQiLog.error("add2Batch(sqlMapClient)",e);
							}

						}
						
						sqlMapClient.executeBatch();
						sqlMapClient.commitTransaction();
						
					} catch (Exception e1) {

						ChuanQiLog.error("batchSync2Db error:",e1);
						
						for(AsyncEntityOperate entityOperate : dataOperates.values()){
							try{

								execute(entityOperate,sqlMapClient);
								
							} catch (Exception e) {
								ChuanQiLog.error(entityOperate.getEntity().getClass().getSimpleName(),e);
							}

						}
						
					}finally{
						try {
							sqlMapClient.endTransaction();
						} catch (SQLException e) {
							ChuanQiLog.error("sqlMapClient.endTransaction()",e);
						}
					}
				}

			}finally{
				writeQueues.remove(id);
			}
			
		}
	}

	private static void execute(AsyncEntityOperate entityOperate,SqlMapClient sqlMapClient) throws SQLException{
		IEntity entity = entityOperate.getInsert();
		if(null != entity){
			sqlMapClient.insert(IbatisStatementMapper.createStatement(IbatisStatementMapper.INSERT_OP, EntityMetaData.getEntityName(entity)),entity);
		}
		
		entity = entityOperate.getUpdate();
		if(null != entity){
			sqlMapClient.update(IbatisStatementMapper.createStatement(IbatisStatementMapper.UPDATE_OP, EntityMetaData.getEntityName(entity)),entity);
		}
		
		entity = entityOperate.getDelete();
		if(null != entity){
			sqlMapClient.delete(IbatisStatementMapper.createStatement(IbatisStatementMapper.DELETE_OP, EntityMetaData.getEntityName(entity)),entity);
		}
	}
	
	/**
	 * 插入一个实体数据
	 * @param entity
	 */
	public void insert(IEntity entity) {
		
		synchronized (dataUpdateLock) {
			
			if(closed) return;
			
			String entityId = getKey(entity);
			AsyncEntityOperate entityOperate = entityOperates.get(entityId);
			if(null == entityOperate){
				entityOperate = new AsyncEntityOperate(entityId);
				entityOperates.put(entityId, entityOperate);
			}

			entityOperate.insert(entity);
			updateOperateTimeStamp();
		}
	}

	/**
	 * 删除一个实体数据
	 * @param entity
	 */
	public void delete(IEntity entity) {
		
		synchronized (dataUpdateLock) {

			if(closed) return;

			String entityId = getKey(entity);
			AsyncEntityOperate entityOperate = entityOperates.get(entityId);
			if(null == entityOperate){
				entityOperate = new AsyncEntityOperate(entityId);
				entityOperates.put(entityId, entityOperate);
			}

			boolean deleted = entityOperate.delete(entity);
			if(deleted) entityOperates.remove(entityId);
			updateOperateTimeStamp();
		}
	}

	/**
	 * 修改实体数据
	 * @param entity
	 */
	public void update(IEntity entity) {
		
		synchronized (dataUpdateLock) {
			
			if(closed) return;
			
			String entityId = getKey(entity);
			AsyncEntityOperate entityOperate = entityOperates.get(entityId);
			if(null == entityOperate){
				entityOperate = new AsyncEntityOperate(entityId);
				entityOperates.put(entityId, entityOperate);
			}

			entityOperate.update(entity);
			updateOperateTimeStamp();
		}
		
	}

	/**
	 * 构造回写数据的唯一标记
	 * @param entity
	 * @return
	 */
	private String getKey(IEntity entity){
		return (new StringBuilder(entity.getClass().getSimpleName()).append(entity.getPrimaryKeyValue())).toString();
	}

	/**
	 * @description 数据同步unitid管理对象
	 * @author hehj
	 * 2011-11-8 下午5:55:41
	 */
	private class UnitId{
		
		private long seed = 0l;
		
		public String nextId(){
			return String.valueOf(++seed);
		}
		
	}
}
