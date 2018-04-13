package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.territory.entity.Territory;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.event.MonsterDropeLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.DropIdType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.export.DropExpShuaiJianConfig;
import com.junyou.gameconfig.export.DropExpShuaiJianConfigExportService;
import com.junyou.gameconfig.export.DropRule;
import com.junyou.gameconfig.export.ZuBaoConfigExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.StageOutputWrapper;
import com.junyou.stage.configure.SkillFirePathType;
import com.junyou.stage.configure.TerritoryConfig;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.configure.export.impl.TerritoryConfigExportService;
import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.heartbeat.AiHeartBeatTimeCarlc;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementProduceTeam;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateEventType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.goods.Goods;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.monster.XianGongXunLuoMonster;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.fight.SkillProcessHelper;
import com.junyou.stage.model.fight.SkillProcessHelper.SkillTargetFilter;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.CampStage;
import com.junyou.stage.model.stage.fuben.DayFubenStage;
import com.junyou.stage.model.stage.fuben.SingleFbStage;
import com.junyou.stage.model.state.AiBackState;
import com.junyou.stage.model.state.AiFightState;
import com.junyou.stage.model.state.AiMoveState;
import com.junyou.stage.model.state.AiXunLuoState;
import com.junyou.stage.tafang.stage.TaFangMonster;
import com.junyou.stage.tafang.stage.TaFangNpc;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.BStarUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;

@Service
public class MonsterService{
	
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ZuBaoConfigExportService zuBaoConfigExportService;
	@Autowired
	private DropExpShuaiJianConfigExportService dropExpShuaiJianConfigExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private TerritoryExportService territoryExportService;
	@Autowired
	private TerritoryConfigExportService territoryConfigExportService;
	
	public void defaultHandle(String stageId, Long monsterId, Integer elementType) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IMonster monster = (IMonster)stage.getElement(monsterId, ElementType.convert(elementType));
		if(monster == null){
			return;
		}
		
		MonsterConfig monsterConfig = monsterExportService.load(monster.getMonsterId());
		Point oldPoint = monster.getPosition();
		try{
			//回血处理
			monster.monsterHuiFuHp();
			
			refreshState(stage,monster);
			if(monster.getStateManager().contains(StateType.FIGHT) && !monster.isNoAttack()){
				IHatred hatred = monster.getHatredManager().getHatredest();
				IFighter target = (IFighter)stage.getElement(hatred.getId(),hatred.getElementType());
				if(target == null){
					return;
				}else if(target.getStateManager().isDead()){
					monster.getHatredManager().clearHatred(target.getId());
					return;
				}
				
				//目标在隐身状态时,怪物不能攻击
				if(target.getStateManager().isYinShen() && hatred.getVal() < 10){
					return;
				}
				
				//过滤镖车
				if(ElementType.isBiaoChe(target.getElementType())){
					return;
				}
				
				List<String> skills = monsterConfig.getSkillList();
				if(null != skills){
					ISkill chooseSkill = null;
					boolean isScope = false;
					for(String skillId : skills){
						ISkill skill = SkillManager.getManager().getSkill(skillId);
						if(skill == null){
							ChuanQiLog.error("怪物：{}-{}技能配置错误，技能id：{}",monsterConfig.getId(),monsterConfig.getName(),skillId);
							continue;
						}
						
						
						SkillConfig skillConfig = skill.getSkillConfig();
						
						//判定范围
						if(!stage.inScope(target.getPosition(), monster.getPosition(), skillConfig.getRange(),ScopeType.PIXEL)){
							continue;
						}
						isScope = true;
						
						//判定技能cd
						if(!SkillProcessHelper.skillReadyFireCheck(stage, monster, skill)){
							continue;
						}
						
						chooseSkill = skill;
						break;
					}
					
					if(null != chooseSkill){
						skillExecute(monster,target,chooseSkill,stage);
					}
					if(chooseSkill == null && !isScope){
						move(monster,target,stage);
					}
					
//					if(chooseSkill == null){//没有释放技能 且 没有目标  则移动
//						move(monster,target,stage);
//					}else{
//						
//					}
					
//					if(chooseSkill == null){//没有释放技能则移动
//						move(monster,target,stage);
//					}else{
//						skillExecute(monster,target,chooseSkill,stage);
//					}
				}
				
			}else{
				
				if(monster.getStateManager().contains(StateType.XUNLUO)){
					xunluo(monster,stage);
				}else if(monster.getStateManager().contains(StateType.BACK)){
					back(monster,stage);
				}
			}
		}catch(Exception e){
			ChuanQiLog.error("ai handle error!", e);
		}finally{
			if(monster.isStopAi()){
				monster.getAi().stop();
			}else{
				//计算下次心跳间隔,不在(战斗，巡逻，返回)状态则无心跳
				int delay = calNextOptDelay(monster,oldPoint);//ms
				if(delay > 0){
					monster.getAi().schedule(delay,TimeUnit.MILLISECONDS);
				}else{
					ChuanQiLog.error("心跳异常：{}",delay);
				}
			}
		}
	}
	
	
	/**
	 * 处理怪物仇恨
	 * @param fighter
	 * @param stage
	 */
	private void hatredMonterHandle(IMonster mFighter, IStage stage) {
		if(!ElementType.isMonster(mFighter.getElementType())){
			return;
		}
		
		MonsterConfig config = monsterExportService.load(mFighter.getMonsterId());
		//不是主动怪不处理仇恨
		if(config.getIfactive()){
			return;
		}
		if(config.isAttriackRedRold()){
			//只攻击红名玩家怪物
			redRoleMonsterHatred(mFighter, config, stage);
		}else{
			nomalMansterHatred(mFighter, config, stage);
		}
	}
	
		
	/**
	 * 只攻击红名玩家
	 * @param mFighter
	 * @param config
	 * @param stage
	 */
	private void redRoleMonsterHatred(IMonster mFighter,MonsterConfig config, IStage stage){
		int eyeshot = config.getEyeshot();// 视野
		
		Collection<IStageElement> aroundRoles = stage.getAroundRedRoles(mFighter);
		if (null != aroundRoles && aroundRoles.size() > 0) {
			
			for (IStageElement stageElement : aroundRoles) {
				
				if (stage.inScope(stageElement.getPosition(),mFighter.getPosition(), eyeshot, ScopeType.GRID)) {
					
					//判断目标是否在仇恨列表内
					if(!mFighter.getHatredManager().checkTargetHatred((IFighter)stageElement)){
						mFighter.getHatredManager().addActiveHatred((IFighter)stageElement, 1);
						mFighter.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
					}
				}
			}
		}
	}
		
		
		
	/**
	 * 正常怪物移动仇恨
	 * @param mFighter
	 * @param config
	 * @param stage
	 */
	private void nomalMansterHatred(IMonster mFighter,MonsterConfig config, IStage stage){
		if(config.getIfactive()){
			return;
		}
		
		int eyeshot = config.getEyeshot();// 视野
		
		// 移动到主动怪物的视野内，会触发主动怪物仇恨增加
		Collection<IStageElement> aroundMonsters = stage.getAroundEnemies(mFighter);
		if (null != aroundMonsters && aroundMonsters.size() > 0) {
			
			for (IStageElement tmp : aroundMonsters) {
				
				if(ElementType.isMonster(tmp.getElementType())){
					IMonster monster = (IMonster) tmp;
					
					if (stage.inScope(monster.getPosition(),mFighter.getPosition(), eyeshot, ScopeType.GRID)) {
						
						//判断目标是否在仇恨列表内
						if(!monster.getHatredManager().checkTargetHatred(mFighter)){
							monster.getHatredManager().addActiveHatred(mFighter, 1);
						}
						//图腾增加额外仇恨
						if(ElementType.isTuTeng(monster.getElementType())){
							monster.getHatredManager().addActiveHatred(mFighter, 10);
						}
						
						monster.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
							
					}
				}else{
					
					if (stage.inScope(tmp.getPosition(),mFighter.getPosition(), eyeshot, ScopeType.GRID)) {
						IFighter fighter = (IFighter)tmp;
						if(!fighter.getStateManager().isDead()){
							mFighter.getHatredManager().addActiveHatred(fighter, 1);
							mFighter.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
						}
					}
					
				}
				
			}
		}
		
	}

	/**
	 * 计算下次心跳间隔
	 */
	private int calNextOptDelay(IMonster monster,MonsterConfig monsterConfig) {
		return AiHeartBeatTimeCarlc.calcMonsterHeartBeatTime(monster,monsterConfig);
	}
	/**
	 * 计算下次心跳间隔
	 */
	private int calNextOptDelay(IMonster monster,Point oldPoint) {
		return AiHeartBeatTimeCarlc.calcHeartBeatTime(monster, oldPoint.getX(), oldPoint.getY());
	}


	/**
	 * 返回操作
	 */
	private void back(IMonster monster, IStage stage) {
		
		if(monster.getStateManager().isForbidden(StateEventType.MOVE)){
			return;
		}
		//不可移动的怪物
		if(monster.isNoMove()){
			return;
		}
		
		int[] moveXy = getBstarFind(monster.getPosition(), monster.getBornPosition(), stage);
		stage.moveTo(monster, moveXy[0], moveXy[1]);
		
		//确认是否是真实移动
		if(monster.getPosition().isReallyMove()){
			moveHandle(stage,monster);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(monster.getPosition()), ClientCmdType.BEHAVIOR_MOVE, monster.getMoveData());
		}
	}

	private void moveHandle(IStage stage, IMonster monster) {
		//怪物仇恨处理
		hatredMonterHandle(monster, stage);
	}

	private int[] getBstarFind(Point from,Point target,IStage stage){
		Point[] points = BStarUtil.findPints(from, target);
		
		int[] finalPint = new int[]{from.getX(),from.getY()};
		for (Point point : points) {
			int faX = from.getX() + point.getX();
			int faY = from.getY()+point.getY();
			
			if(stage.checkCanUseStagePoint(faX ,faY, PointTakeupType.BEING)){
				finalPint = new int[]{faX,faY};
				break;
			}
		}
		
		return finalPint;
	}
	
	/**
	 * 巡逻操作
	 */
	private void xunluo(IMonster monster, IStage stage) {
		MonsterConfig config = monsterExportService.load(monster.getMonsterId());
		if(config != null && config.isAttriackRedRold()){
			//只攻击红名玩家怪物
			redRoleMonsterHatred(monster, config, stage);
		}
		
		//不可移动的怪物
		if(monster.isNoMove()){
			return;
		}
		Point targetPoint = new Point(monster.getBornPosition().getX() - 1 + Lottery.roll(3),monster.getBornPosition().getY() - 1 + Lottery.roll(3));
		
		int[] moveXy = getBstarFind(monster.getPosition(), targetPoint, stage);
		stage.moveTo(monster, moveXy[0], moveXy[1]);
		//确认是否是真实移动
		if(monster.getPosition().isReallyMove()){
			moveHandle(stage,monster);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(monster.getPosition()), ClientCmdType.BEHAVIOR_MOVE, monster.getMoveData());
		}
	}
	
	
	/**
	 * 移动操作
	 */
	private void move(IMonster monster, IFighter target, IStage stage) {
		if(monster.getStateManager().isForbidden(StateEventType.MOVE)){
			return;
		}
		//不可移动的怪物
		if(monster.isNoMove()){
			return;
		}
		if(stage.inScope(monster.getPosition(), target.getPosition(), 1, ScopeType.GRID)){
			return;//已经靠近目标了
		}
		
		int[] moveXy = getBstarFind(monster.getPosition(), target.getPosition(), stage);
		stage.moveTo(monster, moveXy[0], moveXy[1]);
		//确认是否是真实移动	
		if(monster.getPosition().isReallyMove()){
			moveHandle(stage,monster);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(monster.getPosition()), ClientCmdType.BEHAVIOR_MOVE, monster.getMoveData());
		}
		monster.getStateManager().add(new AiMoveState());//设为移动状态
	}

	/**
	 * 技能释放操作
	 */
	private void skillExecute(IMonster monster, IFighter target, ISkill skill,IStage stage) {
			
		SkillProcessHelper.skillReadyFireConsume(stage, monster, skill);
		
		IMsgWriter msgWriter = BufferedMsgWriter.getInstance();
		
		//目标确认
		Object[] helpParams = skill.getSkillFirePath().createHelpParams(stage,skill,monster,target);
		Collection<IFighter> targets = SkillProcessHelper.chooseTargets(stage, monster, skill, helpParams);
		
		//伤害计算
		List<Object[]> harmMsgs	 = SkillProcessHelper.fight(monster, skill, targets, msgWriter);
		if(null != harmMsgs && harmMsgs.size() > 0){
			SkillConfig skillConfig = skill.getSkillConfig();
			
			if(skillConfig.getSkillFirePathType().equals(SkillFirePathType.DEFAULT) || skillConfig.getSkillFirePathType().equals(SkillFirePathType.TARGET_CIRCLE)){
				
				Object[] targetParams = helpParams;
				if(targets != null && targets.size() > 0){
					IFighter tFighter = targets.iterator().next();
					
					targetParams = new Object[]{tFighter.getPosition().getX(),tFighter.getPosition().getY()};
				}
				
				Object clentData = StageOutputWrapper.skillHarm(monster,skill,harmMsgs.toArray(),targetParams);
				StageMsgSender.send2Many(stage.getSurroundRoleIds(monster.getPosition()), ClientCmdType.SKILL_FIRE,clentData);
			}else{
				Object clentData = StageOutputWrapper.skillHarm(monster,skill,harmMsgs.toArray(),helpParams);
				StageMsgSender.send2Many(stage.getSurroundRoleIds(monster.getPosition()), ClientCmdType.SKILL_FIRE,clentData);
			}
			
		}
		
		//消息刷出
		monster.getFightStatistic().flushChanges(msgWriter);
		msgWriter.flush();
		
	}
	
	
	/**
	 * 刷新状态
	 */
	private IFighter refreshState(IStage stage, IMonster monster) {
		
		//仇恨刷新
		monster.getHatredManager().refreshHatred();
		IHatred hatredest = monster.getHatredManager().getHatredest();
		
		if(monster.getStateManager().contains(StateType.XUNLUO)){
			//巡逻状态下，有仇恨过来，则转换状态到战斗
			
			if(null != hatredest){
				monster.getStateManager().add(new AiFightState());
//				monster.getFightStatistic().flushChanges( DirectMsgWriter.getInstance() );
			}
			
		}else if(monster.getStateManager().contains(StateType.MOVE)){
			//移动状态下，有仇恨，则转换状态到战斗,没有则转为返回状态
			
			if(null != hatredest){
				monster.getStateManager().add(new AiFightState());
			}else{
				monster.getStateManager().add(new AiBackState());
			}
			
		}else if(monster.getStateManager().contains(StateType.FIGHT)){
			//战斗状态下，仇恨消失，则转换状态到返回
			
			if(null == hatredest){
				monster.getStateManager().add(new AiBackState());
			}
			
		}else if(monster.getStateManager().contains(StateType.BACK)){
			//返回状态下，无视任何仇恨
			//除非已经返回成功，则重置仇恨，更改状态为巡逻
			
			if(monster.getStage().isScopeGeZi(monster.getBornPosition(), monster.getPosition(), 2)){
				monster.getStateManager().add(new AiXunLuoState());
				monster.getHatredManager().clear();
			}
		}
		
		//刷新巡逻状态
		monster.refreshXunLouState();
		
		return null;
	}

	public void retrieve(String stageId, Long id, Integer elementType,
			String teamId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IElement element = stage.getElement(id, ElementType.convert(elementType));
		if(element == null){
			return;
		}
		
		IElementProduceTeam produceTeam = stage.getStageProduceManager().getElementProduceTeam(ElementType.convert(elementType), teamId);
		if(produceTeam == null){
			stage.leave(element);
			return;
		}
		produceTeam.retrieve(element);
		if (stage.getStageType().equals(StageType.CAMP)){
			CampStage campStage = (CampStage)stage;
			campStage.setNextRefreshTime(GameSystemTime.getSystemMillTime()+produceTeam.getDelay());
			campStage.noticeMonsterStatus();
		}
	}

	public void produce(String stageId, String teamId, Integer elementType) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IElementProduceTeam produceTeam = stage.getStageProduceManager().getElementProduceTeam(ElementType.convert(elementType), teamId);
		
		produceTeam.produceAll();
	}
	
	
	public void monsterDeadDrop(String stageId,String monsterId,Long monsterGuid, Integer x, Integer y,Long hatredestRoleId, Integer level, Integer teamId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(stage.getMapId());
		if(diTuConfig != null){
			
			boolean isNoDrop = getHongDongMapDrop(diTuConfig);
			//活动期间内是否不掉落
			if(isNoDrop){
				return;
		
			}
		}
		
		MonsterConfig monsterConfig = monsterExportService.load(monsterId);
	
		List<Goods> dropGoodsSet = new ArrayList<>();
		
		List<DropRule> dropRules = monsterConfig.getDropRule();
		if(dropRules != null && dropRules.size() > 0){
			for(DropRule dropRule : dropRules){
				List<Goods> goods = dropCal(dropRule, level, monsterId,stage.getMapId());
				for(Goods g : goods){
				    g.setOnlySelfPickup(monsterConfig.isOnlySelfPickup());
				}
				dropGoodsSet.addAll(goods);
			}
		}
		
		
		if(monsterConfig.getRank() > 1){
			//广播怪物掉落物品
			notifyMonsterDrop(dropGoodsSet, monsterId);
			//打印怪物掉落日志
			printMonsterDropLog(dropGoodsSet, monsterId, hatredestRoleId, stage.getMapId());
		}
		
		//金币掉落（金币不参与广播和打印日志）//金币掉落不单独计算  --by刘愚
//		List<MoneyDropRule> moneyDropRules = monsterConfig.getMoneyDropRules();
//		if(null != moneyDropRules){
//			for(MoneyDropRule tmp : moneyDropRules){
//				List<Object[]> result = moneyDropCal(tmp, level, monsterId);
//				if(result != null){
//					dropGoodsSet.addAll(result);
//				}
//			}
//		}
		
		if(dropGoodsSet.size() > 0){
			if(monsterConfig.isNoOwner()){//没有归属
				hatredestRoleId = GameConstants.DEFAULT_ROLE_ID;
				teamId = null;
			}
			Object[] data = new Object[]{hatredestRoleId, teamId, x, y, dropGoodsSet.toArray(), monsterConfig.getDropProtectDuration(), monsterConfig.getDropGoodsDuration(), monsterId, monsterGuid};
			StageMsgSender.send2StageInner(hatredestRoleId,stageId,InnerCmdType.DROP_GOODS, data);
		}
	}
	
	
	/**
	 * 打印怪物掉落日志
	 * @param goods
	 * @param monsterId
	 * @param ownerId
	 * @param mapId
	 */
	private void printMonsterDropLog(List<Goods>  dropGoods,String monsterId,Long ownerId,Integer mapId){
		try{
			
			if(dropGoods == null || dropGoods.size() == 0 || monsterId == null){
				return;
			}
			
			JSONArray goodsList = null;
			for (Goods dropGood : dropGoods) {
				String goodsId = dropGood.getGoodsId();
				Integer count = dropGood.getCount();
				GoodsConfig config = goodsConfigExportService.loadById(goodsId);
				if(config.isDaily()){
					//怪物掉落日志中物品
					setGoodsInfo(goodsList, goodsId, count);
				}
			}
			String name = "-";
			if(!KuafuConfigPropUtil.isKuafuServer() && publicRoleStateExportService.isPublicOnline(ownerId)){
				//怪物掉落日志
				RoleWrapper role = roleExportService.getLoginRole(ownerId);
				name = role.getName();
			}
			GamePublishEvent.publishEvent(new MonsterDropeLogEvent(ownerId, name, mapId, monsterId, goodsList));
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	public void elementAiXunluo(String stageId, String teamId, Integer elementType) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IElementProduceTeam produceTeam = stage.getStageProduceManager().getElementProduceTeam(ElementType.convert(elementType), teamId);
		if(produceTeam != null){
			Long elementId = produceTeam.getRandomOneElementId();
			if(elementId != null){
				IStageElement stageElement = stage.getElement(elementId, ElementType.MONSTER);
				if(stageElement != null){
					Monster monster = (Monster)stageElement;
					if(monster.getAi() != null){
						monster.getAi().interruptSchedule(IAi.NOMAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
					}
				}
				
				//开始下一个定时
				produceTeam.randomXunlouSchedule();
			}
		}
	}
	
	private static JSONArray setGoodsInfo(JSONArray goodsList, String goodsId, Integer count){
		if(goodsList == null || goodsList.isEmpty()){
			goodsList = new JSONArray();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(goodsId != null){
			map.put("goodsId", goodsId);
		}
		if(count != null){
			map.put("count", count);
		}
		
		goodsList.add(map);
		return goodsList;
	}
	
	
	/**
	 * 广播怪物掉落物品
	 * @param dropGoodsSet
	 * @param monsterId
	 */
	private void notifyMonsterDrop(List<Goods> dropGoodsSet,String monsterId){
		if(dropGoodsSet == null || dropGoodsSet.size() == 0){
			return;
		}
		try {
			List<Object[]> notifyGoods = null;
			for (Goods dropGoods : dropGoodsSet) {
				String goodsId = dropGoods.getGoodsId();
				Integer count = dropGoods.getCount();
				
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
				if(goodsConfig != null && goodsConfig.isNotify()){
					
					if(notifyGoods == null){
						notifyGoods = new ArrayList<Object[]>();
					}
					
					notifyGoods.add(new Object[]{goodsId,count});
				}
			}
			
			if(notifyGoods != null && notifyGoods.size() > 0){
				//TODO
//				Object[] monsterData = new Object[]{0,monsterId};
				
//				Object notifyData = new Object[]{BossErrorCode.BOSS_DEAD ,new Object[]{monsterData,new Object[]{4,notifyGoods.toArray()}}};
//				StageMsgSender.send2All(StageCommands.NOTIFY_GOODS_CMD, notifyData);
			}
		} catch (Exception e) {
			ChuanQiLog.error("stage monster drop notify error",e);
		}
	}
	
//	private List<Goods> moneyDropCal(MoneyDropRule tmp,
//			Integer level, String monsterId) {//金币掉落不单独计算  --by刘愚
//		
//		boolean isDrop = Lottery.roll(tmp.getOdds(), Lottery.HUNDRED);
//		if(isDrop){
//			List<Goods> result = new ArrayList<>();
//			
//			int count = tmp.getDropCount();
//			while(count-- > 0){
//				Goods goods = new Goods(IdFactory.getInstance().generateNonPersistentId(), GoodsCategory., count, GameSystemTime.getSystemMillTime());
//				result.add(goods);
//			}
//			
//			return result;
//		}
//		
//		return null;
//	}

	private List<Goods> dropCal(DropRule dropRule,Integer level,String monsterId,Integer mapId){
		
		List<Goods> dropGoodsSet = new ArrayList<>();
		
		Goods generateGoods = null;
		
		//掉落规则
		int dropCount = dropRule.getDropCount() > 0 ? dropRule.getDropCount() : 1;
		for(int i = 0 ; i < dropCount ; i++){
			generateGoods = calDropRule(monsterId,dropRule,mapId);
			if(null != generateGoods){
				dropGoodsSet.add(generateGoods);
			}
		}
		return dropGoodsSet;
		
	}
	/**
	 * 基本掉落规则
	 */
	private Goods calDropRule(String monsterId,DropRule dropRule,Integer mapId) {
		String goodsId = null;
		int goodsCount = 1;
		
		//随机几率掉落算法
		boolean mingZhong = Lottery.roll(dropRule.getDropRate(), Lottery.TENTHOUSAND);
		if(mingZhong){
			//掉落计算
			if(DropIdType.GOODSID.equals(dropRule.getDropIdType())){
				//物品编号
				
				goodsId = dropRule.getDropId();
				
			}else{
				//组包编号
				Object[] goods = zuBaoConfigExportService.componentRoll(dropRule.getComponentDropId());
				if (goods != null) {					
					goodsId = (String) goods[0];
					goodsCount = (Integer)goods[1];
				}
			}
			
			if(null != goodsId){
				
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
				if(null != goodsConfig){
					//TODO 创建物品
					Goods goods = new Goods(IdFactory.getInstance().generateNonPersistentId(), goodsId, goodsCount, GameSystemTime.getSystemMillTime());
					return goods;
				}
			}
		}
		return null;
	}

	
	/**
	 * 计算角色打怪获得经验
	 * @param role
	 * @param teamId 队伍ID
	 * @param teamCount 队伍人数
	 * @param monsterLevel 怪物等级
	 * @param baseExp 怪物基础经验
	 * @param A 
	 * @param dingShiMapConfig
	 * @return
	 */
	private Long calcMonsterExpValue(IRole role,String teamId,int monsterLevel,int baseExp,float A,IStage stage){
		Long  result = 0L;
		/**
		 * 
			公式：	EXP =baseExp * min(1, dmg/hp) * min（ 10，  lvlFactor * (1 + itemBuff + VIPBuff + mapBuff + teamBuff + X41+...)） * (1+serverBuff)	
		 */
		
		float E = 1f;
		
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(stage.getMapId());
		if(diTuConfig != null){
			E = getHongDongExpBeiLv(diTuConfig,E);
		}
		
		
		//等级差
		int levelDiff = monsterLevel - role.getLevel();
		DropExpShuaiJianConfig dropExpShuaiJianConfig = dropExpShuaiJianConfigExportService.loadByLevel(levelDiff);
		
		//等级差系数(S)  S=subtractfactor  （jingyanshuaijianbiao中的等级差衰减系数）
		float S = 0f;
		if(dropExpShuaiJianConfig != null){
			S = dropExpShuaiJianConfig.getLevelXishu();
		}
		
		//C = 道具多倍经验 
		Float C = role.getPropModel().getExpPropManyBie();
		
		
		//BUFF = BUFF经验倍率
		long BUFF = role.getFightAttribute().getEffectAttribute(EffectType.x41.name());
		
		float guildMapBuff = 0f;
		//帮派领地 杀怪经验加成
		Long guildId = role.getBusinessData().getGuildId();
		if(guildId != null){
			Territory territory = territoryExportService.loadTerritoryByMapId(stage.getMapId());
			if(territory != null && territory.getGuildId() != null && territory.getGuildId().longValue() != 0){
				if(territory.getGuildId().longValue() == guildId.longValue()){
					TerritoryConfig config = territoryConfigExportService.getConfigByMapId(stage.getMapId());
					if(config != null){
						guildMapBuff = config.getAddexp();
					}
				}
			}
		}
		
		
		Float ratio = ((E + C + guildMapBuff)*EffectType.getAttBase() + BUFF) / EffectType.getAttBase();
		ratio = A * ratio * S;
		if(ratio > 20){
			ratio = 20f;
		}
		//当前活动多倍奖励
		int D = ActiveUtil.getKillBei();
		ratio *= 1 + D;
		
		result = (long)(baseExp * ratio);
		
		return result;
	}
	
	public void monsterExpDrop(String stageId, String monsterId,
			float monsterMaxHp, Long hatredestRoleId, String teamId, Map<Long, Integer> harms) {
		
		IStage stage = StageManager.getStage(stageId);	
		if(stage == null){
			return;
		}
		if(harms == null){
			return;
		}
		
		
		for (Long roleId : harms.keySet()) {
			
			IRole role = (IRole) stage.getElement(roleId, ElementType.ROLE);
			if (role != null) {
				MonsterConfig monsterConfig = monsterExportService.load(monsterId);
				//怪物基础经验
				int monsterBaseExp = monsterConfig.getBasicExp();
				
				//伤害怪物血量百分比
				int roleHarms = harms.get(roleId);
				
				//A = 攻击方对被攻击方造成的伤害血量/被攻击方的总血量
				Float bilvA = roleHarms / monsterMaxHp;
				if(bilvA > 1){
					bilvA = 1f;
				}
				
				//公式：	经验=B*A*S*(C+D+F+BUFF)*(1+T)*E	
				Long result = calcMonsterExpValue(role, teamId, monsterConfig.getLevel(), monsterBaseExp, bilvA,stage);
				if(!KuafuConfigPropUtil.isKuafuServer()){
//					IncrRoleResp incrExpResp = roleExportService.incrExp(roleId, result);
					BusMsgSender.send2BusInner(roleId, InnerCmdType.INNER_ADD_EXP, new Object[]{result,null});
					BusMsgSender.send2BusInner(roleId, InnerCmdType.CHONGWU_ADD_EXP, new Object[]{result,null});
//					//客户推送怪物经验   [0:String(怪物ID),1:int(怪物经验)]
//					StageMsgSender.send2One(roleId, ClientCmdType.MONSETER_DEAD_ADD_EXP, new Object[]{monsterId,incrExpResp.getRealIncr()});
				}else{
					KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.INNER_ADD_EXP, new Object[]{result,null});
					KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.CHONGWU_ADD_EXP, new Object[]{result,null});
//					StageMsgSender.send2One(roleId, StageCommands.MONSETER_DEAD_ADD_EXP, new Object[]{monsterId,result});
				}
				

//				try {
//					Long jingyanyuExp = calcMonsterByJingYanYuExpValue(role, teamId, monsterConfig.getLevel(), monsterBaseExp, bilvA);
//					if(!KuafuConfigPropUtil.isKuafuServer()){
//						StageMsgSender.send2Bus(roleId, InnerCmdType.TUISONG_EXPEXP, jingyanyuExp);
//					}else{
//						KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.TUISONG_EXPEXP, jingyanyuExp);
//					}
//				} catch (Exception e) {
//					ChuanQiLog.error("",e);
//				}
			}
		}
		
	}
	
	
	/**
	 * 活动经验倍率
	 * @param diTuConfig
	 * @return
	 */
	private Float getHongDongExpBeiLv(DiTuConfig diTuConfig,Float hdBeiv){
		
		try {
			//是否不在活动期间
			if(!diTuConfig.isInHuodongTime()){
				//活动倍率
				hdBeiv = diTuConfig.getExpNoBv();
			}
			
		} catch (Exception e) {
			ChuanQiLog.error("MonsterServiceImpl getHongDongExpBeiLv()",e);
		}
		
		return hdBeiv;
	}
	
	
	/**
	 * 获取掉落
	 * @param diTuConfig
	 * @return true:不掉落
	 */
	private boolean getHongDongMapDrop(DiTuConfig diTuConfig){
		boolean isDrop = true;
		
		try {
			if(diTuConfig.isInHuodongTime()){
				isDrop = false;
			}else{
				isDrop = diTuConfig.isNoDrop();
			}
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		return isDrop;
	}
	
	
	public void gaojiAiSkill(String stageId, Long fighterId, Long targetId, String skillId, int delay, boolean repeat) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		Monster monster = stage.getElement(fighterId, ElementType.FIGHTER);
		
		IFighter target = stage.getElement(targetId, ElementType.FIGHTER);
		
		if( target == null || monster == null) return;
		
		ISkill skill = monster.getSkill(skillId);
		
		skillExecute(monster, target, skill, stage);
		
		//是否间隔固定周期发送
		if( repeat ){
//			StageTokenRunable tokenRunnable = new StageTokenRunable(fighterId.toString(), stageId, StageCommands.INNER_AI_ADVANCED_1, new Object[]{targetId, skillId, delay, repeat});
//			monster.getScheduler().schedule(fighterId.toString(), StageConstants.COMPONENT_ADVANCED_AI_FIRE, tokenRunnable, delay, TimeUnit.SECONDS);
		}
	}

	public void gaojiAiTelport(Long roleId, String mapId, int x, int y) {
		
//		StageMsgSender.send2StageControl(roleId, StageCommands.INNER_APPLY_CHANGE_MAP, new Object[]{mapId, x, y});
		
		
	}

	public void monsterSpeak(String stageId, Long monsterId, String words) {
		
		IStage stage = StageManager.getStage(stageId);
		IFighter fighter = stage.getElement(monsterId, ElementType.FIGHTER);
		
		if(fighter != null){
			Object[] roleIds = stage.getSurroundRoleIds(fighter.getPosition());
			
			Integer code = Integer.parseInt(words);
//			StageMsgSender.send2Many(roleIds, StageCommands.MONSTER_AI_TALK, new Object[]{fighter.getId(), code});
		}
		
	}



	public static class AiSkillTargetFilter extends SkillTargetFilter{
		
		
		
		public AiSkillTargetFilter(IStage stage, IFighter attacker, ISkill skill, Object[] helpParams) {
			super(stage, attacker, skill, helpParams);
		}


		@Override
		public boolean check(IStageElement target) {
			
			//验证规则:
			//1、战斗元素
			//2、技能最大人数
			//3、其他可攻击验证
			//4、技能施法路径
			//5、施法距离
			if(
					ElementType.isFighter(target.getElementType()) &&
					!counter.max() && 
					SkillProcessHelper.getAttackedAblecheck().check(skill, attacker, (IFighter)target, stage) &&
					skill.getSkillFirePath().inRange(skill, attacker, target, helpParams, stage, draft) &&
					stage.inScope(attacker.getPosition(), target.getPosition(), scope, ScopeType.PIXEL)
					){
				
				counter.incr();
				
				return true;
			}
			
			return false;
			
		}


		@Override
		public boolean isEnough() {
			return counter.max();
		}
		
	}
	
	public void disapear(String stageId, Long monsterGuid, Integer elementType) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			if(stage.getDeadHiddenState() == DeadDisplay.NOEXIT){
				return;
			} 
			IElement element = stage.getElement(monsterGuid, ElementType.convert(elementType));
			if(element != null){
				stage.leave(element);
			}
		}
	}
	
	public void xunLuoAi(String stageId,Long id, Integer elementType){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IElement element = stage.getElement(id, ElementType.convert(elementType));
		if(element == null){
			return;
		}
		
		XianGongXunLuoMonster monster = (XianGongXunLuoMonster)element;
		Point oldPoint = monster.getPosition();
		monster.move();
		//计算下次心跳间隔,不在(战斗，巡逻，返回)状态则无心跳
		int delay = calNextOptDelay(monster,oldPoint);//
		if(delay > 0){
			monster.getAi().schedule(delay,TimeUnit.MILLISECONDS);
		}
	}
	
	public void tafangMonsterAi(String stageId,Long id, Integer elementType){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(!StageType.TAFANG_FUBEN.equals(stage.getStageType())){
			return;
		}
		
		IElement element = stage.getElement(id, ElementType.convert(elementType));
		if(element == null){
			return;
		}
		TaFangMonster monster = (TaFangMonster)element;
		Point oldPoint = monster.getPosition();
		
		if(monster.move()){
			int delay = calNextOptDelay(monster,oldPoint);//ms
			if(delay > 0){
				monster.getAi().schedule(delay,TimeUnit.MILLISECONDS);
			}else{
				ChuanQiLog.error("心跳异常：{}",delay);
			}
		}
	}
	public void taFangNPCAi(String stageId,Long id, Integer elementType){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(!StageType.TAFANG_FUBEN.equals(stage.getStageType())){
			return;
		}
		
		IElement element = stage.getElement(id, ElementType.convert(elementType));
		if(element == null){
			return;
		}
		TaFangNpc monster = (TaFangNpc)element;
		ISkill chooseSkill = null;
		for (ISkill skill : monster.getSkills()) {
			if(skill == null){
				continue;
			}
			if(!SkillProcessHelper.skillReadyFireCheck(stage, monster, skill)){
				continue;
			}
			chooseSkill = skill;
			break;
		}
		if(chooseSkill != null){//没有释放技能则移动
			skillExecute(monster,monster,chooseSkill,stage);
		}
		
		int delay = monster.getHeartTime();//ms
		if(delay > 200){
			monster.getAi().schedule(delay,TimeUnit.MILLISECONDS);
		}else{
			ChuanQiLog.error("塔防NPC心跳异常：{}",delay);
		}
	}

}
