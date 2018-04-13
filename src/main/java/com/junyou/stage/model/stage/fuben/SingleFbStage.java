package com.junyou.stage.model.stage.fuben;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.RoleBasePublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.help.GongGongServiceHelper;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 单人副本基础抽象类
 * @author DaoZheng Yuan
 * 2015年3月3日 下午4:27:33
 */
public abstract class SingleFbStage  extends AoiStage{

	/**
	 * 场景定时器
	 */
	private StageScheduleExecutor scheduleExecutor;
	/**
	 * 挑战者
	 */
	private IRole challenger;
	/**
	 * 场景内所有的怪物
	 */
	private List<IMonster> monsters;
	/**
	 * 需击杀名单
	 */
	private Map<String,Integer> wantedMap;
	/**
	 * 副本开始时间
	 */
	private long startTime;
	
	/**
	 * 是否已开启定时 true:是,false:没有
	 */
	private boolean isStartDs = false;
	
	/**
	 * 是否可以复活
	 */
	private boolean fuhuo;
	
	/**
	 * 单人副本基础抽象类
	 * @param id
	 * @param mapId
	 * @param aoiManager
	 * @param pathInfoConfig
	 * @param stageType 场景类型
	 * @param isKillAllMonster  是否要全部击杀完场景怪物
	 * @param needKillMap 需要击杀的怪物Map列表  key:怪物配置id,value:怪物数量 
	 */
	public SingleFbStage(String id, Integer mapId,AOIManager aoiManager, PathInfoConfig pathInfoConfig,StageType stageType,ReadOnlyMap<String, Integer> needKillMap) {
		super(id, mapId, 1, aoiManager, pathInfoConfig,stageType);
		
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		//副本创建时间
		this.startTime = GameSystemTime.getSystemMillTime();
		
		if(needKillMap != null){
			this.wantedMap = new HashMap<>(needKillMap);
		}
	}
	
	/**
	 * 一定要在怪物全部创建完成后调用
	 */
	public void initWantedMap(){
		if(wantedMap != null){
			return;//如果已有名单，则不需要初始化
		}
		if(monsters == null){
			ChuanQiLog.error("fuben create error.stage no monster.fubenId is " + getId() + ". type is " + getStageType());
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
	
	public void changeWantedMap(ReadOnlyMap<String, Integer> needKillMap){
		this.wantedMap = new HashMap<>(needKillMap);
	}
	
	/**
	 * @Description    重置击杀名单
	 */
	public void resertWantedMap(){
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
	 * 获取单人副本的场景定时器
	 * @return
	 */
	protected StageScheduleExecutor getScheduleExecutor() {
		return scheduleExecutor;
	}

	/**
	 * 开启副本的过期定时
	 */
	public void startScheduleExpireCheck(Long roleId){
		if(getExitCmd() != 0){
			BusTokenRunable runable = new BusTokenRunable(roleId, getExitCmd(), getId());
			getScheduleExecutor().schedule(getId(), GameConstants.COMPONENT_FUBEN_FORCED_LEAVE, runable, getExpireDelay(), TimeUnit.MILLISECONDS);
		}
	}
	
	public abstract int getExpireDelay();
	public abstract short getExitCmd();
	/**
	 * 取消副本的过期定时
	 */
	public void cancleScheduleExpire(){
		getScheduleExecutor().cancelSchedule(getId(), GameConstants.COMPONENT_FUBEN_FORCED_LEAVE);
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
			this.challenger = (IRole)element;
			
			if(!isStartDs){
				//开启强制踢出定时
				startScheduleExpireCheck(element.getId());
				isStartDs = true;
			}
		}else if(ElementType.isMonster(element.getElementType())){
			if(null == monsters){
				this.monsters = new ArrayList<IMonster>();
			}
			
			IMonster monster = (IMonster)element;
			this.monsters.add(monster);
			
		}
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
			IRole role = (IRole)element;
			if(role.getStateManager().remove(StateType.DEAD)){
				RoleBasePublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_ROLE_BASE);
				Float curHp = role.getFightAttribute().getMaxHp() * publicConfig.getPtHpBv();
				role.getFightAttribute().setCurHp(curHp.intValue());
				
				IMsgWriter writer = BufferedMsgWriter.getInstance();
				role.getFightStatistic().flushChanges(writer);
				writer.flush();
				
				StageMsgSender.send2One(role.getId(), ClientCmdType.TOWN_REVIVE, role.getId());
			}
			this.challenger = null;
			cancleScheduleExpire();
			noticeClientExit(role.getId());
		}else if(ElementType.isMonster(element.getElementType())){
			this.monsters.remove(element);
		}
	}
	
	/**
	 * 获取挑战者
	 * @return
	 */
	public IRole getChallenger() {
		return challenger;
	}
	
	/**
	 * 清场副本中的怪物
	 */
	public void clearFubenMonster(){
		if(monsters != null && monsters.size() > 0){
			List<IMonster> clearMonster = new ArrayList<IMonster>();
			
			clearMonster.addAll(monsters);
			for (IMonster monster : clearMonster) {
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
	 * @Description 获取场景中所有初始化的怪物集合                      
	 */
	public Map<String, Integer> getWantedMap() {
	    return wantedMap;
	}
	
	/**
	 * 通知前端击杀怪物信息
	 * @return
	 */
	public abstract void noticeClientKillInfo(Long roleId);
	
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
		
		//停止之前的副本过期强踢定时
		cancleScheduleExpire();
		//清怪
		clearFubenMonster();
		
		IRole role = getChallenger();
		if(role != null && getExitCmd() != 0){
			//3.开启通关后的倒计时强踢
			BusTokenRunable runable = new BusTokenRunable(role.getId(), getExitCmd(), getId());
			long outTime = getExpireCheckInterval();
			getScheduleExecutor().schedule(getId(), GameConstants.COMPONENT_FUBEN_FORCED_LEAVE, runable, outTime, TimeUnit.MILLISECONDS);
			
			//同步强制退出时间错
			StageMsgSender.send2One(role.getId(),ClientCmdType.EXIT_FUBEN_TIME, System.currentTimeMillis()+outTime);
		}
	}

	public abstract int getExpireCheckInterval();
	
	public abstract short getFinishCmd();
	
	public abstract short getFinishNoticeBusCmd();
	
	/**
	 * 通知前端退出副本
	 */
	public abstract void noticeClientExit(Long userRoleId);

	@Override
	public boolean canFuhuo() {
		return fuhuo;
	}

	public void setFuhuo(boolean fuhuo){
	    this.fuhuo = fuhuo;
	}

	@Override
	public boolean isCanRemove() {
		return challenger == null;
	}
	
}
