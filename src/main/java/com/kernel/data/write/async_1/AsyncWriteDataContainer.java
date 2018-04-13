package com.kernel.data.write.async_1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.EntityMetaData;
import com.kernel.data.accessor.IbatisStatementMapper;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

public class AsyncWriteDataContainer {

	private Long identity;

	private AsyncWriteManager asyncWriteManager;
	
	private Runnable triggerTask = new Runnable() {
		
		@Override
		public void run() {
			flush();
			reschedule();
		}
	};
	
	/**
	 * 已变更需要回写的数据
	 */
	private LinkedHashMap<String, EntityOperate> entityOperates = new LinkedHashMap<String, EntityOperate>();
	private Object dataUpdateLock = new Object();
	private Object dataSyncLock = new Object();
	
	private long lastOperateTime = GameSystemTime.getSystemMillTime();
	private boolean closed = false;
	
	/**
	 * 最终需要回写的数据
	 */
	private LinkedHashMap<String, EntityOperate> saveOperates = null;
	
	public AsyncWriteDataContainer(Long identity,AsyncWriteManager asyncWriteManager) {
		this.identity = identity;
		this.asyncWriteManager = asyncWriteManager;
		reschedule();
	}
	
	public Long getIdentity(){
		return this.identity;
	}
	
	public void updateLastOperateTime(){
//		synchronized (dataUpdateLock) {
			this.lastOperateTime = GameSystemTime.getSystemMillTime();
//		}
	}
	
	/**
	 * 保存一个新增的数据
	 * @param entity
	 */
	public void insert(IEntity entity){
		
		synchronized (dataUpdateLock) {
			
			if(closed) return;
			
			((AbsVersion)entity).updateVersion();
			
			EntityOperate entityOperate = getEntityOperate(getKey(entity),entityOperates);
			entityOperate.insert(entity,true);
			
			updateLastOperateTime();
		}
		
	}
	
	/**
	 * 保存一个修改过的数据
	 * @param entity
	 */
	public void update(IEntity entity){
		
		synchronized (dataUpdateLock) {

			if(closed) return;

			EntityOperate entityOperate = getEntityOperate(getKey(entity),entityOperates);
			entityOperate.update(entity,true);
			
			updateLastOperateTime();
		}
	}
	
	/**
	 * 删除一个数据
	 * @param entity
	 */
	public void delete(IEntity entity){
		
		synchronized (dataUpdateLock) {

			if(closed) return;

			EntityOperate entityOperate = getEntityOperate(getKey(entity),entityOperates);
			boolean deleted = entityOperate.delete(entity,true);
			if(deleted){
				entityOperates.remove(entityOperate.getId());
			}
			
			updateLastOperateTime();
		}
		
	}
	
	public void flush(){
		
		LinkedHashMap<String, EntityOperate> flushOperates = null;
		synchronized (dataUpdateLock) {
			if(entityOperates.size() > 0){
				
				flushOperates = entityOperates;
				entityOperates = new LinkedHashMap<String, EntityOperate>();
				
			}
		}

		if(null != flushOperates){
			
			synchronized (this) {
				if(null == saveOperates){
					saveOperates = flushOperates;
				}else{
					// 合并要保存的数据
					for(EntityOperate entityOperate : flushOperates.values()){
						
						try{
							
							EntityOperate existOperate = null;
							IEntity entity = null;
							
							// 新增
							entity = entityOperate.getInsert();
							if(null != entity){
								existOperate = getEntityOperate(entityOperate.getId(), saveOperates);
								existOperate.insert(entity,false);
							}
							// 修改
							entity = entityOperate.getUpdate();
							if(null != entity){
								existOperate = getEntityOperate(entityOperate.getId(), saveOperates);
								existOperate.update(entity,false);
							}
							// 删除
							entity = entityOperate.getDelete();
							if(null != entity){
								existOperate = getEntityOperate(entityOperate.getId(), saveOperates);
								boolean deleted = existOperate.delete(entity,false);
								if(deleted){
									saveOperates.remove(existOperate.getId());
								}
							}
							
						}catch (Exception e) {
							ChuanQiLog.error("flush error,entity info: "+entityOperate.getEntityInfo(),e);
						}

					}
				}

				asyncWriteManager.accept2write(this);

			}

		}
		
	}

	/**
	 * 开始一个新的回写调度
	 */
	private void reschedule(){
		synchronized (dataUpdateLock) {
			if(!closed){
				asyncWriteManager.getScheduledExecutor().schedule(triggerTask, asyncWriteManager.getWritePeriod(), TimeUnit.SECONDS);
			}
		}
	}

	/**
	 * 判定是否可以丢弃
	 * @return
	 */
	public boolean canClean(){
		synchronized (this) {
			synchronized (dataUpdateLock) {
				if(!this.closed){
					this.closed = (null == saveOperates) && ( (GameSystemTime.getSystemMillTime() - lastOperateTime) > asyncWriteManager.getCleanGap() );
				}
			}
		}
		return this.closed;
	}

	public void close(){
		synchronized (dataUpdateLock) {
			this.closed = true;
		}
		flush();
	}
	
	public void sync(){
		
		// fast switch
		LinkedHashMap<String, EntityOperate> syncOperates = null;
		synchronized (this) {
			if(null != saveOperates){
				syncOperates = saveOperates;
				saveOperates = null;
			}
		}
		
		// sync to db
		synchronized (dataSyncLock) {
			
			if(null != syncOperates){
				
				// group first
				Map<String, List<EntityOperate>> groups = new HashMap<String, List<EntityOperate>>();
				for(EntityOperate operate : syncOperates.values()){
					
					List<EntityOperate> group = groups.get(operate.getEntityClassName());
					if(null == group){
						group = new ArrayList<EntityOperate>();
						groups.put(operate.getEntityClassName(), group);
					}
					group.add(operate);
				}
				
				// save groups
				SqlMapClient sqlMapClient = asyncWriteManager.getSqlMapClient();
				try{
					sqlMapClient.startTransaction();
					
					for(List<EntityOperate> operates : groups.values()){
						
						try{
							sqlMapClient.startBatch();
							
							for(EntityOperate operate : operates){
								execute(operate, sqlMapClient);
							}
							
							sqlMapClient.executeBatch();
							sqlMapClient.commitTransaction();
							
						}catch (Exception e) {
							
							ChuanQiLog.error("batch2Db error",e);
							// 批量同步失败时的处理逻辑 1.单独处理每一个实体变更 2.丢弃产生失败的那个实体变更（主要是insert）
							for(EntityOperate operate : operates){
								
								try {
									
									execute(operate, sqlMapClient);
									
									sqlMapClient.commitTransaction();
									
								} catch (Exception e2) {
									ChuanQiLog.error("single2Db error,entity info: "+operate.getEntityInfo(),e);
								}
								
							}
							
						}
						
					}
					
				}catch (Exception e) {
					
					ChuanQiLog.error("sqlMapClient.startTransaction()", e);
					
				}finally{
					try {
						sqlMapClient.endTransaction();
					} catch (SQLException e) {
						ChuanQiLog.error("sqlMapClient.endTransaction()",e);
					}
				}
				
			}
		}

	}
		
	private void execute(EntityOperate operate,SqlMapClient sqlMapClient) throws SQLException{
		

		IEntity entity = operate.getInsert();
		if(null != entity){
			sqlMapClient.insert(IbatisStatementMapper.createStatement(IbatisStatementMapper.INSERT_OP, EntityMetaData.getEntityName(entity)),entity);
		}
		
		entity = operate.getUpdate();
		if(null != entity){
			sqlMapClient.update(IbatisStatementMapper.createStatement(IbatisStatementMapper.UPDATE_OP, EntityMetaData.getEntityName(entity)),entity);
		}
		
		entity = operate.getDelete();
		if(null != entity){
			sqlMapClient.delete(IbatisStatementMapper.createStatement(IbatisStatementMapper.DELETE_OP, EntityMetaData.getEntityName(entity)),entity);
		}
		
	}

	private EntityOperate getEntityOperate(String entityId,LinkedHashMap<String, EntityOperate> operates){
		EntityOperate entityOperate = operates.get(entityId);
		if(null == entityOperate){
			entityOperate = new EntityOperate(entityId);
			operates.put(entityId, entityOperate);
		}
		return entityOperate;
	}
	
	/**
	 * 构造回写数据的唯一标记
	 * @param entity
	 * @return
	 */
	private String getKey(IEntity entity){
		return (new StringBuilder(((AbsVersion)entity).getVersion()).append(entity.getClass().getSimpleName()).append(entity.getPrimaryKeyValue())).toString();
	}

}
