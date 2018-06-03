package com.kernel.gen.id;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.bus.serverinfo.entity.ServerInfo;
import com.junyou.event.StopTimeEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.utils.exception.GameCustomException;

/**
 * 生成唯一id的服务 time为服务器第n次重启，seqId 在重启后会重新清零累加
 * 前52位给需持久化的ID使用，52位~53位 给非持久化的使用
 * AS3 number 整形精度可以达到53位
 * +---------------------------------------------+
 * |-worldId-|--times-|---objectType---|--seqId-| 
 * |-16bits--|10bits--|-------4bits----|-22bits-|
 * +---------------------------------------------+
 * 
 * seqId最多可达4194303 ，objectType 可达15 ，times可达1023,
 * serverId可达65535，如果达到MAX_SEQ_ID的话，times人为+1
 * 
 * @author Allen Jiang
 * @author ydz
 */
public class IdFactory {
	private static final Logger logger = LogManager.getLogger(IdFactory.class);

	private static final int MAX_STAGE_LINE_ID = 0x1FF;
//	private static final int STAGE_ELEMENT_ID = 0x7FFFFFF;
//	private static final long MAX_ID = 0xFFFFFFFFFFFFFl;
	private static final long MAX_SEQ_ID = 0x3FFFFF;
	private static final long MAX_SERVER_ID = 0xFFFFl;
	private static final long MAX_TIMES = 0x3FF;
	private static final long MAX_TYPE = 0xF;
	private Map<Byte, AtomicInteger> map = new ConcurrentHashMap<Byte, AtomicInteger>();
	private static AtomicInteger sequenceNonPers = new AtomicInteger(1); // ConcurrentHashMap?
	private static AtomicInteger teamAtomic = new AtomicInteger(1); // 队伍id自增器
	private static AtomicInteger moreFubenTeamAtomic = new AtomicInteger(1); // 多人副本队伍id自增器
	private int worldId;
	private int times;// 服务器重启次数
	private static long ID_PREFIX;// id前缀
	private static final IdFactory INSTANCE = new IdFactory();
	private final ReentrantLock lock = new ReentrantLock();
	private ServerInfo info;
	
	private IdFactory(){}
	
	public static IdFactory getInstance() {
		return INSTANCE;
	}
	
	public void init(ServerInfo info) throws GameCustomException {
		if(info != null){
			this.info = info;
			int serverId = info.getId();
			this.init(serverId, info.getStopTimes());
		}
	}


	private void init(int worldId, int times) throws GameCustomException {
		logger.info("IdFactory初始化 开始", worldId);
		if (worldId > MAX_SERVER_ID) {
			throw new GameCustomException(String.format("serverId 超出最大值: %d", worldId));
		}
		if (times > MAX_TIMES) {
			throw new GameCustomException(String.format("times 超出最大值: %d", times));
		}

		this.worldId = worldId;
		this.times = times;
		ID_PREFIX = this.buildPrefix();
		int num = this.register();
		logger.info("IdFactory初始化 完成，共有{}个模块ID", num);
	}

	private int register() {
		int ret = 0;
		for (Field field : ServerIdType.class.getDeclaredFields()) {
			try {
				byte type = field.getByte(null);
				if (type > MAX_TYPE) {
					throw new GameCustomException(String.format("type 超出最大值: %d", type));
				}
				map.put(type, new AtomicInteger(1));
				ret++;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new GameCustomException(e.getMessage(), e);
			}

		}
		return ret;
	}

	private long buildPrefix() {
		return ((long) worldId << 36) | ((long) times << 26);
	}

	/** 根据模块ID,生成ID */
	public long generateId(byte objectType) {
		AtomicInteger sequence = map.get(objectType);
		int seqId = sequence.getAndIncrement();
		long id = ID_PREFIX | ((long) objectType << 22) | seqId;
		// logger.debug("objectType={},seqId={},id={}", objectType, seqId,
		// Long.toHexString(id));
		
		if (seqId >= MAX_SEQ_ID) {
				
			try {
				lock.lock();
				logger.warn("超过流水ID最大值 objectType={}", objectType);
				// 防止其他线程进入时再次修改这个值
				sequence = map.get(objectType);
				seqId = sequence.get();
				id = ID_PREFIX | ((long) objectType << 22) | seqId;
				if (seqId >= MAX_SEQ_ID) {
					times++;
					ID_PREFIX = this.buildPrefix();
					
					//更新停机次数
					info.addOneStopTimes((int)objectType);
					this.register();
					
					//发布停机次数改变事件 
					GamePublishEvent.publishEvent(new StopTimeEvent(objectType));
				}
			} finally {
				lock.unlock();
			}
		}
		return id;
	}

	
	/**非 持久化的主键生成器 (取得的值是一个永远的负数)**/
	public long generateNonPersistentId() {
//		return MAX_ID + sequence.getAndIncrement();
		return ~(sequenceNonPers.getAndIncrement() & Integer.MAX_VALUE);
	}
	/**获取队伍id*/
	public int getTeamId(){
		return teamAtomic.getAndIncrement();
	}
	
	/**
	 * 获取多人副本队伍ID
	 * @return
	 */
	public int getMoreFubenTeamId(){
		return moreFubenTeamAtomic.getAndIncrement();
	}
	

	/*** 专用于公共场景的ID生成，mapID要小于200W,line要小于512，做这个ID是为了通过mapid和line能反向找到这个ID */
//	public int createStageId(int mapId, int line) {//@ly-2015-02-03暂不使用int的stageId
//		return (mapId << 10) | line;
//	}
	
	/** 获取场景ID的线的值**/	
//	public int getMapLineByStageId(int stageId){//@ly-2015-02-03暂不使用int的stageId
//		return stageId & MAX_STAGE_LINE_ID;
//	}

	public static void main(String[] args) throws GameCustomException {
//		IdFactory.getInstance().init(1, 20);
//		for (int i = 1; i < 100; i++) {
//			System.out.println(IdFactory.getInstance().generateId(ServerIdType.GOODS));
////			System.out.println(IdFactory.getInstance().generateNonPersistentId());
//		}
	}
	
	/**
	 * 是否是持久化的主键一般背包内的就是持久化,怪物死亡的道具就是非持久化
	 * @param id
	 * @return true:是持久化的主键
	 */
	public boolean isGenerateId(long id){
		if(id > 0){
			return true;
		}else{
			return false;
		}
	}
}
