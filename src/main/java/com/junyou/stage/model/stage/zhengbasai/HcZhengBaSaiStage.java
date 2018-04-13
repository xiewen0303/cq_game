package com.junyou.stage.model.stage.zhengbasai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.publicconfig.configure.export.HcZBSPublicConfig;
import com.junyou.stage.configure.HcZhengBaSaiConfig;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.datetime.GameSystemTime;

public class HcZhengBaSaiStage extends PublicFubenStage{
	
	private DingShiActiveConfig dingShiActiveConfig;
	private HcZBSPublicConfig hcZBSPublicConfig;
	private HcZhengBaSaiConfig  hcZhengBaSaiConfig;
	/**领地战管理器*/
	private HcZhengBaSaiManager hcZhengBaSaiManager;
	/**场景定时器*/
	private StageScheduleExecutor scheduleExecutor;
	/**占领公会id*/
	private Long ownerGuildId = null;
	/**
	 * 旗帜拥有玩家
	 */
	private IRole flagOwnerRole = null;
	
	private IBuff flagOwnerBuff = null;
	
	private IStageElement longt = null;
	
	private boolean hasFlag = false;
	
	private boolean flagOccupyEnd =false; //旗帜成功占领30分钟，活动结束 旗帜被永久占领
	
	public boolean isFlagOccupyEnd() {
		return flagOccupyEnd;
	}
	public void setFlagOccupyEnd(boolean flagOccupyEnd) {
		this.flagOccupyEnd = flagOccupyEnd;
	}
	private Map<Long,Long> roleFlagMap = new HashMap<Long,Long>();
	
	private ReentrantLock lock = new ReentrantLock();
	public void lock(){
		try {
			lock.tryLock(1,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
	}
	public void unlock(){
		if(lock.isHeldByCurrentThread()){
			lock.unlock();
		}
	}
	
	public void setFlag(Long userRoleId,Long flagGuid){
		roleFlagMap.put(userRoleId, flagGuid);
	}
	public void clearFlagMap(){
		roleFlagMap.clear();
	}
	public Long getFlag(Long userRoleId){
		return roleFlagMap.get(userRoleId);
	}
	
	public HcZhengBaSaiManager getHcZhengBaSaiManager() {
		return hcZhengBaSaiManager;
	}
	public void setHcZhengBaSaiManager(HcZhengBaSaiManager hcZhengBaSaiManager) {
		this.hcZhengBaSaiManager = hcZhengBaSaiManager;
	}
	public StageScheduleExecutor getScheduleExecutor() {
		return scheduleExecutor;
	}
	public HcZhengBaSaiStage(String id, Integer mapId,Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,StageType.HCZBS_WAR);
		this.scheduleExecutor = new StageScheduleExecutor(getId());
	}
	/**
	 * 初始化，把原场景中的玩家加入管理器(如果已有所属，启动定时)
	 * @param delay	需坚守多久后结束战斗
	 */
	public void init(int delay){
		List<IStageElement> roles = this.getAllElements(ElementType.ROLE);
		if(roles.size() > 0){
			for (IStageElement element : roles) {
				IRole role = (IRole)element;
				hcZhengBaSaiManager.enter(role);
			}
		}
		if(ownerGuildId != null){
			//领地已有所属公会，坚守30分钟获胜
			startTobeWinnerSchedule(delay);
		}
	}
	public void startSynFlagPositionSchedule(){
		cancelSynFlagPositionSchedule();
		StageTokenRunable runable = new StageTokenRunable(null, getId(), InnerCmdType.HCZBS_SYN_FLAG, getId());
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_HCZBS_FLAG_SYN, runable, 1, TimeUnit.SECONDS);
	}
	public void cancelSynFlagPositionSchedule(){
		scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_HCZBS_FLAG_SYN);
	}
	public void setScheduleExecutor(StageScheduleExecutor scheduleExecutor) {
		this.scheduleExecutor = scheduleExecutor;
	}


	public Long getOwnerGuildId() {
		return ownerGuildId;
	}


	public void setOwnerGuildId(Long ownerGuildId) {
		this.ownerGuildId = ownerGuildId;
	}


	public IRole getFlagOwnerRole() {
		return flagOwnerRole;
	}


	public void setFlagOwnerRole(IRole flagOwnerRole) {
		this.flagOwnerRole = flagOwnerRole;
	}


	public IBuff getFlagOwnerBuff() {
		return flagOwnerBuff;
	}


	public void setFlagOwnerBuff(IBuff flagOwnerBuff) {
		this.flagOwnerBuff = flagOwnerBuff;
	}


	public IStageElement getLongt() {
		return longt;
	}


	public void setLongt(IStageElement longt) {
		this.longt = longt;
	}


	public boolean isHasFlag() {
		return hasFlag;
	}


	public void setHasFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
	}


	public DingShiActiveConfig getDingShiActiveConfig() {
		return dingShiActiveConfig;
	}


	public void setDingShiActiveConfig(DingShiActiveConfig dingShiActiveConfig) {
		this.dingShiActiveConfig = dingShiActiveConfig;
	}
	
	public HcZBSPublicConfig getHcZBSPublicConfig() {
		return hcZBSPublicConfig;
	}
	public void setHcZBSPublicConfig(HcZBSPublicConfig hcZBSPublicConfig) {
		this.hcZBSPublicConfig = hcZBSPublicConfig;
	}

	public HcZhengBaSaiConfig getHcZhengBaSaiConfig() {
		return hcZhengBaSaiConfig;
	}
	public void setHcZhengBaSaiConfig(HcZhengBaSaiConfig hcZhengBaSaiConfig) {
		this.hcZhengBaSaiConfig = hcZhengBaSaiConfig;
	}
	public void cancelTobeWinnerSchedule(){
		scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_HCZBS_HAS_WINNER);
	}

	public void startTobeWinnerSchedule(long delay){
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.HCZBS_HAS_WINNER, null);
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_HCZBS_HAS_WINNER, runable, delay, TimeUnit.SECONDS);
	}
	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ActiveUtil.isHcZBS() && ElementType.isRole(element.getElementType())){
			IRole role = (IRole)element;
			hcZhengBaSaiManager.enter(role);
			int[] zuobiao = null;
			if(flagOwnerRole != null){
				if(flagOccupyEnd){//夺旗结束 有胜利者
					zuobiao = hcZhengBaSaiConfig.getZuobiao();
					BusMsgSender.send2One(role.getId(), ClientCmdType.HC_ZBS_FLAG_CHANGE, new Object[]{flagOwnerRole.getBusinessData().getGuildId(),flagOwnerRole.getBusinessData().getName()});
				}else{
					zuobiao = new int[2];
					Point p = flagOwnerRole.getPosition();
					zuobiao[0] = p.getX();
					zuobiao[1] = p.getY();
				}
			}else{
				if(getOwnerGuildId()==null){
					BusMsgSender.send2One(role.getId(), ClientCmdType.HC_ZBS_FLAG_CHANGE, new Object[]{hcZhengBaSaiConfig.getMap(),null});
				}else{
					BusMsgSender.send2One(role.getId(), ClientCmdType.HC_ZBS_FLAG_CHANGE, new Object[]{hcZhengBaSaiConfig.getMap(),getOwnerGuildId().longValue()});
				}
				zuobiao = hcZhengBaSaiConfig.getZuobiao();
			}
				BusMsgSender.send2One(role.getId(),  ClientCmdType.HC_ZBS_SYN_FLAG, zuobiao);
		}
	}
	public void leave(IStageElement element) {
		super.leave(element);
		if(ActiveUtil.isHcZBS() && ElementType.isRole(element.getElementType())){
			IRole iRole=(IRole) element;
			if(flagOwnerRole != null && flagOwnerRole.getId().longValue() == iRole.getId().longValue()){
				if(!this.isFlagOccupyEnd()){
					StageMsgSender.send2StageInner(iRole.getId(),getId(), InnerCmdType.HCZBS_FLAG_OWNER_DEAD,null);
				}
			}
			hcZhengBaSaiManager.leave(iRole);
		}
		
	}
	 
	@Override
	public boolean canFuhuo() {
		return false;
	};
	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public boolean isCanRemove(){
		return !isOpen() && (getAllRoleIds() == null || getAllRoleIds().length == 0);
	}

	public void enterNotice(Long userRoleId) {
		Object[] data = new Object[] { 1, 0, null,ownerGuildId};
		if(this.getOwnerGuildId()!=null){
			long time =0;
			if(flagOccupyEnd){
				time = hcZBSPublicConfig.getNeedTime()*1000;
			}else{
				time =  GameSystemTime.getSystemMillTime()- HcZhengBaSaiManager.getManager().getZhanglingTime();
			}
			data[1] = time;
			data[2] = this.flagOwnerRole.getName();
		}
		StageMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_ENTER, data);
		if(flagOccupyEnd){
			//皇城争霸地图提前结束了
			StageMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_END, null);
		}
	}
	
	public void exitNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.HC_ZBS_EXIT, AppErrorCode.OK);
	}
	@Override
	public boolean isFubenMonster() {
		return false;
	}
	/**
	 * 设置旗子
	 */
	public void setFlag(boolean has){
		hasFlag = has;
	}
	/**
	 * 是否有旗子
	 * @return
	 */
	public boolean hasFlag(){
		return hasFlag;
	}

}