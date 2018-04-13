package com.junyou.stage.model.element.biaoche;

import java.util.concurrent.TimeUnit;

import com.junyou.bus.yabiao.configure.export.YaBiaoConfig;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.stage.model.core.attribute.BaseFightAttribute;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.AbsFighter;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.model.core.state.StateManager;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.buff.BuffManager;
import com.junyou.stage.model.skill.harm.HarmUtils;
import com.junyou.stage.model.state.AiXunLuoState;
import com.junyou.stage.model.state.DeadState;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.token.ITokenRunnable;

/**
 * 押镖创建的镖车
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-16 下午3:24:50 
 */
public class Biaoche extends AbsFighter {


	private Integer bcConfigId;
	
	private IFightStatistic fightStatistic;
	private IFightAttribute fightAttribute;
	private BuffManager buffManager;
	private	IHatredManager hatredManager;
	private IStateManager stateManager;
	private IAi ai;
	private YaBiaoConfig ybConfig;
	private Integer bcSpeed;

	
	
	private Object[] moveData;//移动坐标数据
	private long expireTimes = 1800 * 1000;//过期时间戳（毫秒）
	private Long expireTime;//镖车过期时间
	//主人
	private IRole owner;
	//是否可以和主人切地图
	private boolean isChangeMap = false;
	
//	/**
//	 * 最后一次提醒时间
//	 */
//	private long lastWarningTime;

	/**
	 * 1:押镖中，2:镖车受到攻击，
	 */
	private int biaoceState;
	private boolean biaoceStateChange; //状态是否改变
	private long lastHurtTime; 		   //镖车最后受到的攻击时间

	//镖车是否远离
	private boolean isYuanLi; //是否处于远离状态

	public boolean isYuanLi() {
		return isYuanLi;
	}

	public void setYuanLi(boolean yuanLi) {
		isYuanLi = yuanLi;
	}

	public boolean isBiaoceStateChange() {
		return biaoceStateChange;
	}

	public void setBiaoceStateChange(boolean biaoceStateChange) {
		this.biaoceStateChange = biaoceStateChange;
	}

	public long getLastHurtTime() {
		return lastHurtTime;
	}

	public void setLastHurtTime(long lastHurtTime) {
		this.lastHurtTime = lastHurtTime;
	}

	public int getBiaoceState() {
		return biaoceState;
	}

	public void setBiaoceState(int biaoceState) {
		if(biaoceState != this.biaoceState){
			this.biaoceState = biaoceState;
			this.biaoceStateChange = true;
		}
	}

//	public long getLastWarningTime() {
//		return lastWarningTime;
//	}
//
//	public void setLastWarningTime(long lastWarningTime) {
//		this.lastWarningTime = lastWarningTime;
//	}

	/**
	 * 创建镖车
	 * @param ybConfig
	 * @param owner
	 * @param expireTimes 过期时间戳（毫秒）
	 */
	public Biaoche(YaBiaoConfig ybConfig,IRole owner, long expireTimes) {
		super(IdFactory.getInstance().generateNonPersistentId(), new StringBuffer().append(ybConfig.getName()).append("(").append(owner.getName()).append(")").toString());
	
		this.bcConfigId = ybConfig.getId();
		this.bcSpeed = ybConfig.getSpeed();
		this.ybConfig = ybConfig;
		this.owner = owner;
		if(expireTimes > 0){
			this.expireTimes = expireTimes;
		}
		this.expireTime = GameSystemTime.getSystemMillTime() + expireTimes;//镖车过期时间 = 当前时间 + 过期时间戳（毫秒）
	}

	/**
	 * 获取配置ID
	 * @return
	 */
	public Integer getBcConfigId() {
		return bcConfigId;
	}
	/**
	 * 是否可以和主人切地图
	 * @return true:是
	 */
	public boolean isChangeMap() {
		return isChangeMap;
	}

	public void setChangeMap(boolean isChangeMap) {
		this.isChangeMap = isChangeMap;
	}
	public void setAi(BiaoCheAi biaoCheAi) {
		this.ai = biaoCheAi;
	}
	
	public IAi getAi() {
		return ai;
	}
	
	@Override
	public BattleMode getBattleMode() {
		return getOwner().getBattleMode();
	}
	
	public Long getGuildId(){
		return getOwner().getBusinessData().getGuildId();
	}

	public IRole getOwner(){
		return owner;
	}
	
	public void setOwner(IRole owner) {
		this.owner = owner;
	}

	@Override
	public void deadHandle(IHarm harm) {
		//计算镖车死亡，受益玩家
		IRole benefitRole = null;
		if(ElementType.isRole(harm.getAttacker().getElementType())){
			benefitRole = HarmUtils.getBelongRole(harm.getAttacker());
		}else if(ElementType.isPet(harm.getAttacker().getElementType())){
			Pet pet = (Pet) harm.getAttacker();
			benefitRole = pet.getOwner();
		}
		
		if(null != benefitRole && getOwner() != null){
			StageMsgSender.send2Bus(getOwner().getId(), InnerCmdType.B_BIAOCHE_DEAD, new Object[]{getBcConfigId(), benefitRole.getId()});
		}
		
		//立即执行心跳处理业务对死亡的处理
		getAi().stop();
		getHatredManager().clear();
		getBuffManager().clearBuffsByDead();
		getStateManager().add(new DeadState(harm));
	}
	
	public Object getMsgData() {
		Object[] result = new Object[]{
				getBcConfigId(),//0 int 镖车配置标识id 
				getId(),//1 Number 唯一标识Guid 
				getFightAttribute().getCurHp(),//2 int 当前血量 
				getFightAttribute().getMaxHp(),//3 int 当前血量 
				getPosition().getX(),//4 int x坐标 
				getPosition().getY(),//5 int y坐标 
				getOwner().getId(),//6 Number 主人guid 
				getOwner().getName(),//7 String 主人名字 
				getOwner().getBusinessData().getTeamId(),//8 String 队伍ID
				getOwner().getBusinessData().getGuildId(),//9 String 公会ID
				getBcSpeed()//10 String 移动速度
				
		};
		return result;
	}

	@Override
	public void leaveStageHandle(IStage stage) {
		getAi().stop();
		
		this.getScheduler().cancelSchedule(getId()+"", GameConstants.COMPONENT_BIAOCHE);
	}

	@Override
	public void enterStageHandle(IStage stage) {
		
		getStateManager().add(new AiXunLuoState());
		getAi().schedule(10, TimeUnit.MILLISECONDS);
		
		this.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		
		//镖车过期
		ITokenRunnable task = new StageTokenRunable(getOwner().getId(), getStage().getId(), InnerCmdType.S_BIAOCHE_CLEAN, null);
		this.getScheduler().schedule(getId()+"", GameConstants.COMPONENT_BIAOCHE, task, (int)expireTimes, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public Object getStageData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Integer getCamp() {
		return owner.getCamp();
	}
	
	/**
	 * 获取现在镖车过期时间戳（毫秒）
	 * @return
	 */
	public long getExpireTimes(){
		return expireTime - GameSystemTime.getSystemMillTime();
	}

	private Integer getBcSpeed(){
		return bcSpeed;
	}
	
	@Override
	public int getLevel() {
		return ybConfig.getLevel();
	}
	
	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	public void setFightStatistic(BiaoCheFightStatistic bcFightStatistic) {
		this.fightStatistic = bcFightStatistic;
	}
	public void setFightAttribute(BaseFightAttribute baseFightAttribute) {
		this.fightAttribute = baseFightAttribute;
	}
	public void setHatredManager(IHatredManager standartHatredManager) {
		this.hatredManager = standartHatredManager;
	}
	public void setBuffManager(BuffManager buffManager) {
		this.buffManager = buffManager;
	}

	@Override
	public IFightStatistic getFightStatistic() {
		return fightStatistic;
	}

	@Override
	public IFightAttribute getFightAttribute() {
		return fightAttribute;
	}

	@Override
	public IBuffManager getBuffManager() {
		return buffManager;
	}


	@Override
	public IHatredManager getHatredManager() {
		return hatredManager;
	}

	@Override
	public IStateManager getStateManager() {
		return stateManager;
	}


	@Override
	public PathNodeSize getPathNodeSize() {
		return PathNodeSize._1X;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.BIAOCHE;
	}
	
	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_BIAO_CHE;
	}

	@Override
	public PointTakeupType getTakeupType() {
		return PointTakeupType.BEING;
	}

	@Override
	public Object getMoveData() {
		/**
		 0:Number(单位guid),
		 1:int(单位目标坐标x),
		 2:int(单位目标坐标y),
		 3:int(0 移动到目标点以后停止   1 移动到目标点后还会继续移动)   
		 */
		
		if(moveData == null){
			moveData = new Object[4];
			moveData[0] = getId();
		}
		Point point = getPosition();
		moveData[1] = point.getX();
		moveData[2] = point.getY();
		moveData[3] = 0;
		
		return moveData;
	}

	public IRole getIRole() {
		// TODO Auto-generated method stub
		return owner;
	}

	@Override
	public PublicCdManager getPublicCdManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 此次伤害
	 * @return
	 */
	public int getHarm(){
		if(ybConfig.getRollHarm() <= 1){
			return ybConfig.getMinHarm();
		}
		return ybConfig.getMinHarm() + Lottery.roll(ybConfig.getRollHarm());
	}
}
