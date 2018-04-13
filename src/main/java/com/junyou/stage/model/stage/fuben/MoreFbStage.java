package com.junyou.stage.model.stage.fuben;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 多人副本类
 * @author chenjianye
 * 2015年04月28日
 */
public class MoreFbStage  extends AoiStage{

	private StageScheduleExecutor scheduleExecutor;
	private List<IRole> challengers;
	private List<IMonster> monsters;
	private Map<String,Integer> wantedMap;
	private long startTime;
	
	private int expireDelay;
	private short exitCmd;
	private int fubenId;
	
	//private Map<Long,Integer> hurts;
	
	/**
	 * 多人副本基础抽象类
	 * @param id
	 * @param mapId
	 * @param aoiManager
	 * @param pathInfoConfig
	 * @param stageType 场景类型
	 * @param isKillAllMonster  是否要全部击杀完场景怪物
	 * @param needKillMap 需要击杀的怪物Map列表  key:怪物配置id,value:怪物数量 
	 */
	public MoreFbStage(String id, Integer mapId,AOIManager aoiManager, PathInfoConfig pathInfoConfig,StageType stageType,
			ReadOnlyMap<String, Integer> needKillMap,int expireDelay,short exitCmd,int fubenId) {
		super(id, mapId, 1, aoiManager, pathInfoConfig,stageType);
		
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		//副本创建时间
		this.startTime = GameSystemTime.getSystemMillTime();
		
		this.challengers = new ArrayList<>();
		
		//this.hurts = new HashMap<>();
		
		if(needKillMap != null){
			this.wantedMap = new HashMap<>(needKillMap);
		}
	
		this.expireDelay = expireDelay * 1000;
		this.exitCmd = exitCmd;
		
		this.fubenId = fubenId;
	}
	
	/**
	 * 一定要在怪物全部创建完成后调用
	 */
	public void initWantedMap(){
		if(wantedMap != null){
			return;//如果已有名单，则不需要初始化
		}
		if(monsters == null){
			ChuanQiLog.error("more fuben create error.stage no monster.fubenId is " + getId() + ". type is " + getStageType());
			return;//场景内无创建怪物
		}
		wantedMap = new HashMap<>();
		for (IMonster monster : monsters) {
			String monsterId = monster.getMonsterId();
			Integer count = wantedMap.get(monsterId);
			if(count == null){
				count = 1;
			}else{
				count += 1;
			}
			wantedMap.put(monsterId, count);
		}
	}
	
	/**
	 * 获取多人副本的场景定时器
	 * @return
	 */
	protected StageScheduleExecutor getScheduleExecutor() {
		return scheduleExecutor;
	}

	/**
	 * 开启副本的过期定时
	 */
	public void startScheduleExpireCheck(Long userRoleId){
		BusTokenRunable runable = new BusTokenRunable(userRoleId, getExitCmd(), getId());
		getScheduleExecutor().schedule(userRoleId.toString(), GameConstants.COMPONENT_MORE_FUBEN_FORCED_LEAVE, runable, getExpireDelay(), TimeUnit.MILLISECONDS);
	}
	
	public int getExpireDelay(){
		return expireDelay;
	}
	public short getExitCmd(){
		return exitCmd;
	}
	/**
	 * 取消副本的过期定时
	 */
	public void cancleScheduleExpire(Long userRoleId){
		getScheduleExecutor().cancelSchedule(userRoleId.toString(), GameConstants.COMPONENT_MORE_FUBEN_FORCED_LEAVE);
	}
	
	/**
	 * 获取副本开始时间
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 挑战者进入场景
	 */
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(element == null){
			return;
		}
		
		if(ElementType.isRole(element.getElementType())){
			this.challengers.add((IRole)element);
			
			startScheduleExpireCheck(element.getId());
			//启动向源码同步状态的定时
			startSyncToSourceServer(element.getId());
		}else if(ElementType.isMonster(element.getElementType())){
			if(null == monsters){
				this.monsters = new ArrayList<IMonster>();
			}
			
			IMonster monster = (IMonster)element;
			this.monsters.add(monster);
			
		}
	}
	
	/**
	 * 开启与源服状态同步
	 */
	public void startSyncToSourceServer(Long userRoleId){
		StageTokenRunable runable = new StageTokenRunable(userRoleId, getId(),InnerCmdType.MORE_FUBEN_START_SYNC, getId());
		getScheduleExecutor().schedule(userRoleId.toString(), GameConstants.COMPONENT_MORE_FUBEN_SYNC, runable, 15, TimeUnit.SECONDS);
	}	
	/**
	 * 取消与源服状态同步
	 */
	public void cancelSyncToSourceServer(Long userRoleId){
		getScheduleExecutor().cancelSchedule(userRoleId.toString(), GameConstants.COMPONENT_MORE_FUBEN_SYNC);
	}	
	/**
	 * 挑战者离开场景
	 */
	public void leave(IStageElement element) {
		
		super.leave(element);
		if(element == null){
			return;
		}
		
		if(ElementType.ROLE.equals(element.getElementType())){
			this.challengers.remove((IRole)element);
			cancleScheduleExpire(element.getId());
			noticeClientExit(element.getId());
			cancelSyncToSourceServer(element.getId());
		}else if(ElementType.isMonster(element.getElementType())){
			this.monsters.remove(element);
		}
	}
	
	
	/**
	 * 清场副本中的怪物
	 */
	public void clearFubenMonster(){
		if(monsters != null && monsters.size() > 0){
			List<IMonster> clearMonster = new ArrayList<IMonster>();
			
			clearMonster.addAll(monsters);
			for (IMonster monster : clearMonster) {
				if(monster.getStateManager().isDead()){
					continue;
				}
				//停止定时
				monster.getScheduler().clear();
				//从副本地图中移除
				this.leave(monster);
			}
		}
	}
	
	/**
	 * 击杀名单检测并同步副本通关还需要的条件
	 * @return true:是需要检测的怪物ID
	 */
	public boolean wantedListCheck(String killedMonsterId) {
		if(null != wantedMap){
			boolean flag = wantedMap.containsKey(killedMonsterId);
			if(flag){
				
				Integer count = wantedMap.get(killedMonsterId);
				if(count > 1){
					count--;
					wantedMap.put(killedMonsterId, count);
				}else{
					wantedMap.remove(killedMonsterId);
				}
			}
			
			return flag;
		}
		
		return false;
	}
	
	/**
	 * 获取击杀怪物的总数
	 * @return
	 */
	public int getWantedCounts(){
		int count = 0;
		if(null != wantedMap){
			for (Integer value : wantedMap.values()) {
				count += value;
			}
		}
		return count;
	}
	
	/**
	 * 是否已经击杀完毕
	 */
	public boolean isWantedListComplete() {
		return null == wantedMap || wantedMap.size() == 0;
	}
	
	/**
	 * 通关业务,先标记副本结束,再清除场景内的其它怪物,最后再开启强踢倒计时
	 */
	public void tongGuanHandle(){
		
		//清怪
//		clearFubenMonster();
		
		for(IRole role : challengers){
			//停止之前的副本过期强踢定时
			cancleScheduleExpire(role.getId());
			//3.开启通关后的倒计时强踢
			BusTokenRunable runable = new BusTokenRunable(role.getId(), getExitCmd(), getId());
			getScheduleExecutor().schedule(role.getId().toString(), GameConstants.COMPONENT_MORE_FUBEN_FORCED_LEAVE, runable, getExpireCheckInterval(), TimeUnit.MILLISECONDS);
			
			//通知定时退出时间
			BusMsgSender.send2One(role.getId(), ClientCmdType.EXIT_FUBEN_TIME, getExpireCheckInterval());
		}
	}

	public int getExpireCheckInterval(){
		return GameConstants.EXPIRE_CHECK_INTERVAL;
	}

	public List<IRole> getChallengers() {
		return challengers;
	}

	public int getFubenId() {
		return fubenId;
	}

	public void setFubenId(int fubenId) {
		this.fubenId = fubenId;
	}

	@Override
	public boolean isCanRemove() {
		return challengers == null || challengers.size() < 1;
	}

//	public Map<Long, Integer> getHurts() {
//		return hurts;
//	}
//
//	public void setHurts(Map<Long, Integer> hurts) {
//		this.hurts = hurts;
//	}
	/**
	 * 通知前端退出副本
	 */
	public void noticeClientExit(Long userRoleId){
		KuafuMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_EXIT, AppErrorCode.OK);
	}
	
}
