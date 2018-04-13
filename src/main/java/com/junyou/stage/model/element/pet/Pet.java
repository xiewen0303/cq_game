package com.junyou.stage.model.element.pet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.stage.model.core.attribute.BaseFightAttribute;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.AbsFighter;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.model.core.state.StateManager;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.fight.statistic.PetFightStatistic;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.buff.BuffManager;
import com.junyou.stage.model.state.AiXunLuoState;
import com.junyou.stage.model.state.DeadState;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 糖宝
 * @author DaoZheng Yuan
 * 2013-11-5 下午3:29:01
 */
public class Pet extends AbsFighter {

	private int petConfigId;
	
	private IFightStatistic fightStatistic;
	private IFightAttribute fightAttribute;
	private BuffManager buffManager;
	private	IHatredManager hatredManager;
	private IStateManager stateManager;
	private IAi ai;
	private PublicCdManager cdManager;
	private Map<Integer,ISkill> skills;
	private List<ISkill> canUseSkill;
	
	private Object[] moveData;//移动坐标数据
	
	private PetVo petVo;
	private int state;//状态：0跟随，1合体
	
	//主人
	private IRole owner;
	
	private int huiFuHp;
	private Long lastHuiFuTime = 0L;
	
	private int wuqiLevel;//武器等级
	private int zhanjiaLevel;//战甲等级
	
	private int heartTime;//战斗心跳
	
	private long harm;
	
	private Object[] msgData;
	
	public Pet(Long id,String name,IRole owner,PublicCdManager cdManager,int configId,int heartTime) {
		super(id, new StringBuffer().append(name).append("(").append(owner.getName()).append(")").toString());
	
		this.owner = owner;
		
		this.cdManager = cdManager;
		
		this.petConfigId = configId;
		this.heartTime = heartTime;
	}

	
	/**
	 * 宝宝回血
	 */
	public void petHuiFuHp(){
		//配置是可回复类的怪物
		if(huiFuHp > 0){
			
			long curHp = getFightAttribute().getCurHp();
			long maxHp = getFightAttribute().getMaxHp();
			
			//满血不处理
			if(curHp >= maxHp){
				return;
			}
			
			
			if(lastHuiFuTime == 0){
				getFightAttribute().setCurHp(curHp + huiFuHp);
				
				lastHuiFuTime = GameSystemTime.getSystemMillTime();
			}else{
				Long caTime = GameSystemTime.getSystemMillTime() - lastHuiFuTime;
				
				if(caTime > 5000){
					float bv = caTime / 1000f;
					getFightAttribute().setCurHp(curHp + (int)(huiFuHp * bv));
					
					lastHuiFuTime = GameSystemTime.getSystemMillTime();
				}
			}
		}
	}
	
	public PetVo getPetVo() {
		return petVo;
	}



	public void setPetVo(PetVo petVo) {
		this.petVo = petVo;
	}



	/**
	 * 获取配置ID
	 * @return
	 */
	public int getPetConfigId() {
		return petConfigId;
	}

	public void setAi(PetAi petAi) {
		this.ai = petAi;
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
		//立即执行心跳处理业务对死亡的处理
		getAi().stop();
		getHatredManager().clear();
		getBuffManager().clearBuffsByDead();
		
		if(state == GameConstants.PET_STATE_NOMAL){
			getFightAttribute().resetHp();
		}else{
			getStateManager().add(new DeadState(harm));
		}
		
//		IRole role = getOwner();
//		if(role != null){
//			StageMsgSender.send2StageInner(role.getId(), role.getStage().getId(), PetCommands.PET_CLEAR_HANDLE, new Object[]{role.getId(),role.getStage().getId(),getId()});
//		}
	}
	
	@Override
	public Object getMsgData() {
		
		Object[] buffList = null;
		Collection<IBuff> buffs = getBuffManager().getBuffs();
		if(buffs != null && buffs.size() > 0){
			buffList = new Object[buffs.size()];
			
			int index = 0;
			for (IBuff iBuff : buffs) {
				buffList[index++] = iBuff.getClientMsg();
			}
		}
		if(msgData == null){
			/**
			 * 	0	String	糖宝配置标识
			1	Number	唯一标识Guid
			2	int	当前血量
			3	int	最大血值
			4	int	x坐标
			5	int	y坐标
			6	Number	召唤物主人的GUID
			7	String	主人名字
			8	int	糖宝速度
			9	int	糖宝等级
			10	int	糖宝战甲ID(没有则不传)
			 */
			msgData = new Object[]{
					"tb001",
					getId(),
					getFightAttribute().getCurHp(),
					getFightAttribute().getMaxHp(),
					getPosition().getX(),
					getPosition().getY(),
					getOwner().getId(),
					getOwner().getName(),
					getFightAttribute().getSpeed(),
					getLevel(),
					getOwner().getBusinessData().getGuildId(),
					getOwner().getTeamId(),
					getOwner().getXianJianShowId()==null?-1:getOwner().getXianJianShowId(),
					getOwner().getZhanJiaShowId()==null?-1:getOwner().getZhanJiaShowId(),
					getOwner().getBusinessData().getTangbaoXinwenJie(),
					getOwner().getTianYuShowId()==null?-1:getOwner().getTianYuShowId(),
//				,
//				getStateManager().getClientState(),//状态
//				buffList,
//				getGuildId()
			};
		}else{
			int index = 2;
			msgData[index++] = getFightAttribute().getCurHp();
			msgData[index++] = getFightAttribute().getMaxHp();
			msgData[index++] = getPosition().getX();
			msgData[index++] = getPosition().getY();
			msgData[index++] = getOwner().getId();
			msgData[index++] = getOwner().getName();
			msgData[index++] = getFightAttribute().getSpeed();
			msgData[index++] = getLevel();
			msgData[index++] = getOwner().getBusinessData().getGuildId();
			msgData[index++] = getOwner().getTeamId();
			msgData[index++] = getOwner().getXianJianShowId()==null?-1:getOwner().getXianJianShowId();
			msgData[index++] = getOwner().getZhanJiaShowId()==null?-1:getOwner().getZhanJiaShowId();
			msgData[index++] = getOwner().getBusinessData().getTangbaoXinwenJie();
			msgData[index++] = getOwner().getTianYuShowId()==null?-1:getOwner().getTianYuShowId();
			
		}
		
		return msgData;
	}

	@Override
	public void leaveStageHandle(IStage stage) {
		getAi().stop();
	}

	@Override
	public void enterStageHandle(IStage stage) {
		getStateManager().add(new AiXunLuoState());
		getAi().schedule(300, TimeUnit.MILLISECONDS);
		
		this.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		
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

	@Override
	public int getLevel() {
		return owner.getLevel();
	}
	
	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	public void setFightStatistic(PetFightStatistic petFightStatistic) {
		this.fightStatistic = petFightStatistic;
	}
	public void setPetFightAttribute(BaseFightAttribute baseFightAttribute) {
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
		return ElementType.PET;
	}
	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_PET;
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
			moveData = new Object[3];
			moveData[0] = getId();
		}
		Point point = getPosition();
		moveData[1] = point.getX();
		moveData[2] = point.getY();
		
		return moveData;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public PublicCdManager getPublicCdManager() {
		return cdManager;
	}
	
	public void addSkill(Integer index,ISkill skill){
		if(skills == null){
			skills = new HashMap<>();
			canUseSkill = new ArrayList<>();
		}
		skills.put(index, skill);
	}
	
	public List<ISkill> getSkills(){
		if(skills == null){
			return null;
		}
		canUseSkill.clear();
		for (ISkill iSkill : skills.values()) {
			if(!iSkill.isCding(cdManager)){
				canUseSkill.add(iSkill);
			}
		}
		return canUseSkill;
	}


	@Override
	public int getHeartTime() {
		return heartTime;
	}


	public int getWuqiLevel() {
		return wuqiLevel;
	}


	public void setWuqiLevel(int wuqiLevel) {
		this.wuqiLevel = wuqiLevel;
	}

	public int getZhanjiaLevel() {
		return zhanjiaLevel;
	}

	public void setZhanjiaLevel(int zhanjiaLevel) {
		this.zhanjiaLevel = zhanjiaLevel;
	}

	public long getHarm() {
		return harm;
	}

	public void setHarm(long harm) {
		this.harm = harm;
	}
	
}
