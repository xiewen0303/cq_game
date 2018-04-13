package com.junyou.stage.model.element.monster;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.kuafu_yungong.constants.KuafuYunGongConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xinmo.constants.XinmoConstants;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.publicconfig.configure.export.NuqiPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.StageFightOutputWrapper;
import com.junyou.stage.model.attribute.monster.MonsterFightAttribute;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.AbsFighter;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.fight.statistic.IMonsterFightStatistic;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.harm.HarmUtils;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.JianzhongFubenStage;
import com.junyou.stage.model.stage.fuben.WuxingShilianFubenStage;
import com.junyou.stage.model.stage.fuben.XinmoDouchangFubenStage;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuafuQunXianYanStage;
import com.junyou.stage.model.state.AiXunLuoState;
import com.junyou.stage.model.state.DeadState;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.shenmo.entity.ShenMoStage;
import com.junyou.stage.tafang.stage.TaFangStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 
 * @description 怪物
 *
 * @author LiuJuan
 * @created 2011-12-15 上午10:40:47
 */
public class Monster extends AbsFighter implements IMonster {

//	private static final Integer AI_RETRIEVE_TIME = 20;//s
//	public static final Integer AI_PREPARE_DEAD_TIME = 10000;
	
	private String monsterId;
	private int level;
	private int faceTo;
	private IMonsterFightStatistic fightStatistic;
	private MonsterFightAttribute monsterFightAttribute;
	private IStateManager stateManager;
	private String teamId;
	private IBuffManager buffManager;
	private IAi ai;
	private IHatredManager hatredManager;
	private PublicCdManager cdManager = new PublicCdManager();
	private Integer camp;
	
	private Object[] moveData;//移动坐标数据
	
	private long deadTime;
	protected int disappearDuration;//死亡消失时间 s
	private int refreshDuration;//怪物刷新时间 ms
	
	protected Object[] msgData;//怪物数据
	private Object[] sameMsgData;//同模怪物数据
	
	private boolean isNoMove;
	private boolean isNoAttract;
	private boolean isNoBeiAttract;
	private boolean isAttractRed;
	
	
	private int monsterType;
	private int huiFuHp;
	private Long lastHuiFuTime = 0L;
	
	//连续巡逻次数
	private int continuouXunLuoTes = 0;
	
	private boolean isXunluo;
	
	private int heartTime;
	
	private int rank;
	
	public Monster(Long id,String teamId,MonsterConfig monsterConfig){
		super(id,monsterConfig.getName());
		this.teamId = teamId;
		this.monsterId = monsterConfig.getId();
		this.rank = monsterConfig.getRank();
		this.disappearDuration = monsterConfig.getDisappearDuration();
		
		this.isNoMove = monsterConfig.isNoMove();
		this.isNoAttract = monsterConfig.isNoAttrack();
		this.isNoBeiAttract = monsterConfig.isNoBeiAttrack();
		this.isAttractRed = monsterConfig.isAttriackRedRold();
		
		this.monsterType = monsterConfig.getMonsterType();
		
		this.huiFuHp = monsterConfig.getHuiFuHp();
		this.isXunluo = monsterConfig.isXunluo();
		this.heartTime = monsterConfig.getHeartTime();
	}
	
	public Monster(Long id,String name,String teamId,String monsterId) {
		super(id,name);
		this.teamId = teamId;
		this.monsterId = monsterId;
	}

	public boolean isXunluo() {
		return isXunluo;
	}
	
	/**
	 * 刷新怪物巡逻AI(连续3次心跳都是巡逻状态就停止怪物AI)
	 */
	public void refreshXunLouState(){
		if(getStateManager().contains(StateType.XUNLUO)){
			continuouXunLuoTes = continuouXunLuoTes + 1;
		}else{
			continuouXunLuoTes = 0;
		}
		
	}
	
	/**
	 * 是否停止怪物AI
	 * @return true:停止
	 */
	public boolean isStopAi(){
		if(ai == null){
			return false;
		}
		boolean isStop = continuouXunLuoTes >= GameConstants.MONSTER_LX_XL_TIMES;
		if(isStop){
			continuouXunLuoTes = 0;
		}
		return isStop;
	}

	/**
	 * 怪物回血
	 */
	public void monsterHuiFuHp(){
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
	
	/**
	 * 怪物类型
	 * 0 或者不填 普通怪物，不做特殊处理<br/>
	 * 1 副本炮台
	 * 2 阵营战雕像
	 * @return
	 */
	public int getMonsterType() {
		return monsterType;
	}
	
	
	/**
	 * 不可移动
	 * @return true:不可移动
	 */
	public boolean isNoMove(){
		return isNoMove;
	}
	
	/**
	 * 是否是不可攻击其它人
	 * @return true:不可攻击其它人
	 */
	public boolean isNoAttack() {
		return isNoAttract;
	}

	/**
	 * 是否是不可被攻击
	 * @return true:不可被攻击
	 */
	public boolean isNoBeiAttrack() {
		return isNoBeiAttract;
	}
	
	
	/**
	 * 是否只攻击红名玩家
	 * @return
	 */
	public boolean isAttractRed() {
		return isAttractRed;
	}

	/**
	 * 获取怪物刷新时间 ms
	 * @return
	 */
	public int getRefreshDuration() {
		return refreshDuration;
	}

	/**
	 * 设置怪物刷新时间 ms
	 * @param refreshDuration
	 */
	public void setRefreshDuration(int refreshDuration) {
		this.refreshDuration = refreshDuration;
	}

	@Override
	public IFightAttribute getFightAttribute() {
		
		return monsterFightAttribute;
	}

	public PathNodeSize getPathNodeSize(){
		return PathNodeSize._1X;
	}
	
	@Override
	public IMonsterFightStatistic getFightStatistic() {
		
		return fightStatistic;
	}

	@Override
	public int getLevel() {
		
		return level;
	}
	
	@Override
	public ElementType getElementType() {
		return ElementType.MONSTER;
	}
	
	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_MONSTERS_ENTER;
	}

	@Override
	public Object getStageData() {
		return null;
	}
	
	public int getFaceTo() {
		return faceTo;
	}

	/**
	 * 设置面部朝向
	 * @param faceTo
	 */
	public void setFaceTo(int faceTo) {
		this.faceTo = faceTo;
	}

	public String getMonsterId() {
		return monsterId;
	}

	/**
	 * 设置怪物编号
	 * @param name
	 */
	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}

	/**
	 * 设置怪物等级
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * 获取怪物战斗属性
	 * @return
	 */
	public MonsterFightAttribute getMonsterFightAttribute() {
		return monsterFightAttribute;
	}

	/**
	 * 设置怪物战斗属性
	 * @param monsterFightAttribute
	 */
	public void setMonsterFightAttribute(MonsterFightAttribute monsterFightAttribute) {
		this.monsterFightAttribute = monsterFightAttribute;
	}

	/**
	 * 设置战斗统计
	 * @param fightStatistic
	 */
	public void setFightStatistic(IMonsterFightStatistic fightStatistic) {
		this.fightStatistic = fightStatistic;
	}

	@Override
	public Object getMsgData() {
		
//		Object[] buffList = null;
//		Collection<IBuff> buffs = getBuffManager().getBuffs();
//		if(buffs != null && buffs.size() > 0){
//			buffList = new Object[buffs.size()];
//			
//			int index = 0;
//			for (IBuff iBuff : buffs) {
//				buffList[index++] = iBuff.getClientMsg();
//			}
//		}
		if(msgData == null){
			msgData = new Object[]{
					getMonsterId(),//0 String 怪物配置标识 
					getId(),//1 Number 唯一标识Guid 
					getMonsterFightAttribute().getCurHp(),//2 int 当前血量 
					getMonsterFightAttribute().getMaxHp(),//3 int 最大血值 
					getPosition().getX(),//4 int x坐标 
					getPosition().getY(),//5 int y坐标 
					getMonsterFightAttribute().getSpeed()//6 int 移动速度
			};
		}else{
			msgData[2] = getMonsterFightAttribute().getCurHp();
			msgData[4] = getPosition().getX();
			msgData[5] = getPosition().getY();
			msgData[6] = getMonsterFightAttribute().getSpeed();
		}
		
		return msgData;
	}
	/**
	 * 获取同模数据
	 * @return
	 */
	public Object getSameMsgData(Short cmd){
		if(cmd == null){
			return null;
		}else if(cmd.equals(ClientCmdType.AOI_HUNDUN_SAME_CMD)){
			if(sameMsgData == null){
				sameMsgData = new Object[]{
						getId()//0 String Guid 
						,0//1 int 角色配置ID 
						,getFightAttribute().getCurHp()//2 int 当前血量 
						,getFightAttribute().getMaxHp()//3 int 最大血值 
						,getPosition().getX()//4 int x坐标 
						,getPosition().getY()//5 int y坐标 
						,getStateManager().getClientState()//6 int 状态 
						,getFightAttribute().getSpeed()//7 速度
						,getBuffClientMsg()//8buff
						
				};
			}else{
				int index = 2;
				sameMsgData[index++] = getFightAttribute().getCurHp();
				sameMsgData[index++] = getFightAttribute().getMaxHp();
				sameMsgData[index++] = getPosition().getX();
				sameMsgData[index++] = getPosition().getY();
				sameMsgData[index++] = getStateManager().getClientState();
				sameMsgData[index++] = getFightAttribute().getSpeed();
				sameMsgData[index++] = getBuffClientMsg();
			}
		}else{
			return null;
		}
		return sameMsgData;
	}
	
	private Object[] getBuffClientMsg(){
		Object[] buffList = null;
		Collection<IBuff> buffs = getBuffManager().getBuffs();
		if(buffs != null && buffs.size() > 0){
			buffList = new Object[buffs.size()];
			
			int index = 0;
			for (IBuff iBuff : buffs) {
				buffList[index++] = iBuff.getClientMsg();
			}
		}
		return buffList;
	}
	
	@Override
	public IStateManager getStateManager() {
		return stateManager;
	}

	/**
	 * @param
	 */
	public void setStateManager(IStateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Override
	public IBuffManager getBuffManager() {
		return buffManager;
	}

	@Override
	public void setBuffManager(IBuffManager buffManager) {
		this.buffManager = buffManager;
	}

	@Override
	public IHatredManager getHatredManager() {
		return hatredManager;
	}

	@Override
	public IAi getAi() {
		return ai;
	}

	public void setAi(IAi defaultAi) {
		this.ai = defaultAi;
	}

	@Override
	public void setHatredManger(IHatredManager hatredManager) {
		this.hatredManager = hatredManager;
	}

	@Override
	public Integer getCamp() {
		return camp;
	}

	@Override
	public void setCamp(Integer camp) {
		this.camp = camp;
	}

	@Override
	public void enterStageHandle(IStage stage) {
		
		getStateManager().add(new AiXunLuoState());
//		getAi().schedule(IAi.XUNLUO_PERIOD,TimeUnit.MILLISECONDS);
		
//		log.debug("monster enter Stage success!");
	}
	
	
	protected void scheduleDisappearHandle(IHarm harm){
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_RETRIEVE, new Object[]{getId(),getElementType().getVal(),teamId});
		this.getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
	}
	/**
	 * 判断是否为阵营战雕像怪物
	 * @return true:是
	 */
	public boolean isCampMonster(){
		return getMonsterType() == GameConstants.CAMP_TYPE;
	}
	
	/**
	 * 判断是否为领地战雕像怪物
	 * @return true:是
	 */
	public boolean isTerritoryMonster(){
		return getMonsterType() == GameConstants.MONSTER_TYPE_TERRITORY;
	}
	/**
	 * 判断是否为神魔战场水晶
	 * @return true:是
	 */
	public boolean isShenmoShuijing(){
		return getMonsterType() == GameConstants.MONSTER_TYPE_SHUIJING;
	}
	/**
	 * 判断是否为皇城争霸赛雕像怪物
	 * @return true:是
	 */
	public boolean isHcZBSMonster(){
		return getMonsterType() == GameConstants.MONSTER_TYPE_HCZBS;
	}
	/**
	 * 判断是否为神魔战场怪物
	 * @return true:是
	 */
	public boolean isShenMoMonster(){
		return StageType.SHENMO_FUBEN.equals(getStage().getStageType());
	}
	/**
	 * 判断是否为塔防的NPC
	 * @return true:是
	 */
	public boolean isTaFangNpc(){
		return false;
	}
	/**
	 * 判断是否为塔防的怪物
	 * @return true:是
	 */
	public boolean isTaFangMonster(){
		return false;
	}

    /**
     * 判断是否为增加心魔凝神心神力的怪物
     * 
     * @return true:是
     */
    public boolean isXinmoMonster() {
        return getMonsterType() == XinmoConstants.MONSTER_TYPE;
    }
    
    /**
     * 判断是否为跨服云宫之巅的图腾怪物
     * @return true:是
     */
    public boolean isKfYunGongMonster(){
        return getMonsterType() == KuafuYunGongConstants.TUTENG_MONSTER_TYPE;
    }
	
    /**
     * 判断是否为魔宫猎焰boss怪物
     * 
     * @return
     */
    public boolean isMglyBossMonster(){
        return getMonsterType() == GameConstants.MGLY_MONSTER_TYPE && getRank() == 2;
    }
	
    /**
     * 判断是否为魔宫猎焰普通怪物
     * 
     * @return
     */
    public boolean isMglyNormalMonster(){
        return getMonsterType() == GameConstants.MGLY_MONSTER_TYPE && getRank() == 0;
    }
    
	@Override
	public void deadHandle(IHarm harm) {
		
		this.deadTime = GameSystemTime.getSystemMillTime();
		scheduleDisappearHandle(harm);
		
		try{
			if(isTaFangMonster()){
				TaFangStage stage = (TaFangStage)getStage();
				Role role = stage.getChallenger();
				if(role != null){
					StageMsgSender.send2Bus(role.getId(), InnerCmdType.TAFANG_ADD_EXP, monsterId);
					//修炼任务
					BusMsgSender.send2BusInner(role.getId(), InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.FUBEN_TAFANG_KILL, null});
				}
			}else{
				//物品掉落
				//[怪物编号 |stageId| 怪物坐标x | 怪物坐标y  | 仇恨最高者id | 仇恨最高者level | 仇恨最高者teamId]
				getHatredManager().refreshHatred();
				
				//计算怪物死亡，受益玩家
				
				IFighter originalBenefiter = null;//原始受益者
				IHatred hatred = getHatredManager().getHatredest();
				if(null != hatred){
					originalBenefiter = (IFighter)getStage().getElement(hatred.getId(), hatred.getElementType());
				}
				
				IRole benefitRole = null;
				if(null != originalBenefiter && ElementType.isRole(originalBenefiter.getElementType())){
					benefitRole = (IRole)originalBenefiter;
				}else if(HarmUtils.belong2Role(harm.getAttacker())){
					benefitRole = HarmUtils.getBelongRole(harm.getAttacker());
				}
				if(null != benefitRole){
					if(isCampMonster()){
						StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.S_CAMP_DEAD,new Object[]{ GameConstants.MONSTER_DEAD});
					}else if(isTerritoryMonster()){
						StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.TERRITORY_MONSTER_DEAD,null);
					}else if(isHcZBSMonster()){
						StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.HCZBS_MONSTER_DEAD,null);
					}else if(isShenMoMonster()){
						ShenMoStage stage = (ShenMoStage)getStage();
						stage.killMonster(this, benefitRole.getId());
					}else if(isKfYunGongMonster()){
                        StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.I_KUAFU_YUNGONG_OWNER_UPDATE,null);
                    }else{
						//物品掉落
						StageMsgSender.send2StageInner(getId(),getStage().getId(), InnerCmdType.AI_GOODS_DROP, StageFightOutputWrapper.monsterGoodDrop(benefitRole,this));
						Map<Long, Integer> harmMap = getFightStatistic().pullHarmStatistic();
						//经验掉落
						StageMsgSender.send2StageInner(benefitRole.getId(), getStage().getId(), InnerCmdType.AI_EXP_DROP,StageFightOutputWrapper.monsterExpDrop(benefitRole,this,harmMap));
						
						//玩家杀怪统计
						StageMsgSender.send2Bus(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KILL_MONSTOR,new Object[]{ getMonsterId(), GameConstants.TASK_TYPE_KILL,harmMap.keySet().toArray()});
						
						if(benefitRole.getPet() == null){
							if(KuafuConfigPropUtil.isKuafuServer()){
								KuafuMsgSender.send2KuafuSource(benefitRole.getId(), InnerCmdType.PET_ADD_PROGRESS,null);
							}else{
								StageMsgSender.send2Bus(benefitRole.getId(), InnerCmdType.PET_ADD_PROGRESS,null);
							}
						}
						
                        // 心魔系统特殊怪物处理
                        if (isXinmoMonster()) {
                            StageMsgSender.send2Bus(benefitRole.getId(), InnerCmdType.INNER_ADD_XINMO_EXP, null);
                        }
                        else if(isMglyBossMonster() || isMglyNormalMonster()){
                            StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.I_MGLY_MONSTER_DEAD, new Object[] { getMonsterId(), getRank() });
                        }
						
						NuqiPublicConfig config = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_NUQI);
						if(config != null){//杀怪加怒气
							int nuqi = benefitRole.getNuqi();
							if(nuqi < config.getMaxNq()){
								nuqi += config.getKillAdd();
								if(nuqi > config.getMaxNq()){
									benefitRole.setNuqiNotice(config.getMaxNq());
								}else{
									benefitRole.setNuqiNotice(nuqi);
								}
							}
							
						}
						IStage stage  = StageManager.getStage(getStage().getId());
						if(stage instanceof JianzhongFubenStage){
							//if(!KuafuConfigPropUtil.isKuafuServer()){
							JianzhongFubenStage jianzhongFubenStage  =(JianzhongFubenStage)stage;
							jianzhongFubenStage.addKillMonsterNum(1);//云浮剑冢副本统计杀怪数
						}else if(stage instanceof KuafuQunXianYanStage){
							//群仙宴 杀怪加积分
							StageMsgSender.send2StageInner(benefitRole.getId(), stage.getId(), InnerCmdType.KUAFUQUNXIANYAN_ADD_JIFEN, this.getMonsterId());
    					}else if(stage instanceof WuxingShilianFubenStage){
    					    WuxingShilianFubenStage shilianStage  =(WuxingShilianFubenStage)stage;
    					    shilianStage.addKillMonsterNum(1);
    					}else if(stage instanceof XinmoDouchangFubenStage){
    					    StageMsgSender.send2Bus(benefitRole.getId(), InnerCmdType.I_XM_DOUCHANG_ADD_BUFF, this.getMonsterId());
                        }
					}
				}
				
				//死亡广播 
				bossManage(benefitRole);
				
				try {
					//成就推送
					bossChengJiu(benefitRole);
					//修炼任务推送
					xiulianTask(benefitRole);
				} catch (Exception e) {
					ChuanQiLog.error("", e);
				}
			}
		}catch(Exception e){
			ChuanQiLog.error("monter dead error", e);
		}
		if(getAi() != null){
			getAi().stop();
		}
		getHatredManager().clear();
		getBuffManager().clearBuffsByDead();
		getStateManager().add(new DeadState(harm));
	}
	
	private void xiulianTask(IRole role){
		if(getRank() == 0){
			BusMsgSender.send2BusInner(role.getId(), InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.KILL_PT_MONSTER, null});
		}else if(getRank() == 1 ){
			BusMsgSender.send2BusInner(role.getId(), InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.KILL_JY_MONSTER, monsterId});
		}
		
	}
	private void bossChengJiu(IRole role){
//		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(monsterId);
//		//怪物类型为BOSS 推送成就
//		if(monsterConfig != null && monsterConfig.getRank() == 2){
//			StageMsgSender.send2Bus(role.getId(), StageCommands.MONSTER_CHENGJIU, role.getId());
//		}
	}
	
	
	private void bossManage(IRole role){
		
	}
	
	@Override
	public void leaveStageHandle(IStage stage) {
		if(getAi() != null){
			getAi().stop();
		}
		
		getHatredManager().clear();
		getBuffManager().clearAll();
		getStateManager().clear();
		
		getFightAttribute().resetHp();
		
		this.getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE);
		
	}

	@Override
	public Integer getLeftBianShiTime() {
		
		if(!getStateManager().isDead()){
			return 0;
		}
		
		Integer deadDuration = (int)(GameSystemTime.getSystemMillTime() - getDeadTime());
		Integer leftTime = deadDuration;
		if(leftTime < 0){
			leftTime = 0;
		}
		
		return leftTime;
	}

	private long getDeadTime() {
		return deadTime;
	}

	protected void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	@Override
	public PointTakeupType getTakeupType() {
		return PointTakeupType.BEING;
	}
	
	@Override
	public Object getMoveData(){
		/**
		 0:Number(单位guid),
		 1:int(单位目标坐标x),
		 2:int(单位目标坐标y),
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

	@Override
	public PublicCdManager getPublicCdManager() {
		return cdManager;
	}

	@Override
	public IFighter getOwner() {
		return this;
	}

	@Override
	public int getHeartTime() {
		return heartTime;
	}

	public int getRank() {
		return rank;
	}
	
	
}
