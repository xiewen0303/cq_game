/**
 * 
 */
package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.bus.rolestage.export.RoleStageExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.BaguaPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.CampPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuYunGongPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.MaiguPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.NuqiPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.RoleBasePublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.SkillPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.HcZhengBaSaiConfig;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.configure.TerritoryConfig;
import com.junyou.stage.configure.export.impl.HcZhengBaSaiConfigExportService;
import com.junyou.stage.configure.export.impl.RoleBehaviourExportService;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.configure.export.impl.TerritoryConfigExportService;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.NuqiBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.fight.SkillProcessHelper;
import com.junyou.stage.model.hatred.IRoleHatredManager;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.kuafuboss.KuafuBossStage;
import com.junyou.stage.model.stage.kuafuluandou.KuafuLuanDouStage;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuafuQunXianYanStage;
import com.junyou.stage.model.stage.kuafuyungong.KuafuYunGongStage;
import com.junyou.stage.model.stage.territory.TerritoryStage;
import com.junyou.stage.model.stage.zhengbasai.HcZhengBaSaiStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.shenmo.entity.ShenMoStage;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.lottery.RandomUtil;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-5下午4:35:36
 */
@Service
public class FightService{
		
	@Autowired
	private RoleStageExportService roleStageExportService;
	@Autowired
	private StageService stageService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBehaviourExportService roleBeHaviourExportServcie;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private TerritoryConfigExportService territoryConfigExportService;
	@Autowired
	private HcZhengBaSaiConfigExportService hcZhengBaSaiConfigExportService;
	
	public void skillFire(String stageId, Long attackerId,String skillCategory,Object[] targetIds,Object[] inMsgData) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			StageMsgSender.send2One(attackerId, ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skillCategory,1,KuafuConfigPropUtil.isKuafuServer()});
			return;
		}
		
		IFighter attacker = (IFighter)stage.getElement(attackerId, ElementType.FIGHTER);
		if(attacker == null){
			StageMsgSender.send2One(attackerId, ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skillCategory,2,KuafuConfigPropUtil.isKuafuServer()});
			return;
		}
		
		ISkill skill = attacker.getSkill(skillCategory);
		if(skill == null){
			StageMsgSender.send2One(attackerId, ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skillCategory,3,KuafuConfigPropUtil.isKuafuServer()});
			return;
		}
		
		Object[] helpParams = null;
		if(null != inMsgData && !(inMsgData[2] instanceof Object[])){
			helpParams = new Object[]{inMsgData[2]};
		}else{
			helpParams = (Object[])inMsgData[2];
		}
		
		try{
			//验证约束
			boolean checkFlag = SkillProcessHelper.skillReadyFireCheck(stage, attacker, skill);
			if(!checkFlag){
				StageMsgSender.send2One(attackerId, ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skillCategory,4,KuafuConfigPropUtil.isKuafuServer()});
				return;
			}
			
			SkillConfig skillConfig = skill.getSkillConfig();
			switch (skillConfig.getSkillFireType()) {
			case NUQI:
				nuqiSkillFire(attacker, skill);
				break;
			default:
				normalSkillFire(stage, attacker, skill, targetIds, helpParams, inMsgData);
				break;
			}
			
			SkillProcessHelper.skillReadyFireConsume(stage, attacker, skill);
			
			//成功释放进技能后，取消打坐
			if(ElementType.isRole(attacker.getElementType())){
				Role role = (Role)attacker;
				roleBeHaviourExportServcie.daZuoCancelHandle(role);
				//取消坐骑状态
				//role.getStateManager().remove(StateType.ZUOQI);
				ZuoQi zuoqi = role.getZuoQi();
				//下坐骑
				if(zuoqi!=null && role.getStateManager().contains(StateType.ZUOQI)){
					//取消坐骑状态
					zuoqi.setGetOn(false);
					BusMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_DOWN, role.getId());
					role.getStateManager().remove(StateType.ZUOQI);
				}
//				if(role.getNoticeSkills() == null || role.getNoticeSkills().contains(skillCategory)){
//					//技能释放成功，增加熟练度
//					if(KuafuConfigPropUtil.isKuafuServer()){
//						KuafuMsgSender.send2KuafuSource(attackerId, InnerCmdType.SKILL_USE_SKILL, skillCategory);
//					}else{
//						StageMsgSender.send2Bus(attackerId, InnerCmdType.SKILL_USE_SKILL, skillCategory);
//					}
//				}
			}
			
			
			
		}catch(Exception e){
			StageMsgSender.send2One(attackerId, ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skillCategory,5,KuafuConfigPropUtil.isKuafuServer()});
			ChuanQiLog.error("error exist when skillFire ",e);
		}
	}
	
	
	public void innerSkillFire(String stageId, Long attackerId,ElementType  attackerType,String skillCategory) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IFighter attacker = stage.getElement(attackerId, attackerType);
		
		ISkill skill = attacker.getSkill(skillCategory);
		
		try {
			Object[] inMsgData = new Object[4];
			inMsgData[0] = skillCategory;
			inMsgData[3] = attackerId;
			
			//验证约束
			boolean checkFlag = SkillProcessHelper.skillReadyFireCheck(stage, attacker, skill);
			if(!checkFlag){
				StageMsgSender.send2One(attackerId, ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skillCategory,6,KuafuConfigPropUtil.isKuafuServer()});
				return;
			}
			
			SkillProcessHelper.skillReadyFireConsume(stage, attacker, skill);
			
			Object[] targetIds = SkillProcessHelper.innerConfirmTargets(skill, attacker, null, stage);
			
			SkillConfig skillConfig = skill.getSkillConfig();
			switch (skillConfig.getSkillFireType()) {
			case NORMAL:
				normalSkillFire(stage, attacker, skill, targetIds, null, inMsgData);
			default:
				break;
			}
			
		} catch (Exception e) {
			
			StageMsgSender.send2One(attackerId, ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skillCategory,7,KuafuConfigPropUtil.isKuafuServer()});
			
			throw new RuntimeException("",e);
		}
		
	}
	
	
	public void innerSkillSingleFire(String stageId, Long targetId,Long attackerId,ElementType  attackerType,String skillCategory,ElementType  targetType) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IFighter attacker = (IFighter)stage.getElement(attackerId, attackerType);
		if(attacker == null){
			return;
		}
		ISkill skill = attacker.getSkill(skillCategory);
		if(skill == null){
			return;
		}
		
		IFighter target = (IFighter)stage.getElement(targetId, targetType);
		if(target == null){
			return;
		}
		
		innerSkillSingleFire(stage, attacker, skill, target);
	}
	
	public void innerBuffSkillFire(String stageId, Long targetId,Long attackerId,ElementType  attackerType,ElementType  targetType,String buffSkillId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IFighter attacker = (IFighter)stage.getElement(attackerId, attackerType);
		if(attacker == null){
			return;
		}
		ISkill skill = SkillManager.getManager().getSkill(buffSkillId);
		if(skill == null || !skill.getSkillConfig().getSkillFireType().equals(SkillFireType.BUFF)){
			return;
		}
		
		IFighter target = (IFighter)stage.getElement(targetId, targetType);
		if(target == null){
			return;
		}
		
		innerSkillSingleFire(stage, attacker, skill, target);
	}
	
	private void innerSkillSingleFire(IStage stage, IFighter attacker,ISkill skill,IFighter target){
		try {
			Object[] inMsgData = new Object[4];
			inMsgData[0] = skill.getCategory();
			
			//验证约束
			boolean checkFlag = SkillProcessHelper.skillInnerReadyFireCheck(stage, attacker, skill);
			if(!checkFlag){
				StageMsgSender.send2One(attacker.getId(), ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skill.getCategory(),8,KuafuConfigPropUtil.isKuafuServer()});
				return;
			}
			
			SkillProcessHelper.skillReadyFireConsume(stage, attacker, skill);
			
			Object[] targetIds = new Object[]{target.getId()};
			
			SkillConfig skillConfig = skill.getSkillConfig();
			switch (skillConfig.getSkillFireType()) {
			case NORMAL:
				nomalSkillSingleFire(stage, attacker, skill, targetIds, null, inMsgData,true);
				break;
			case NUQI:
				nuqiSkillFire(attacker, skill);
				break;
			case XIANGONG:
				nomalSkillSingleFire(stage, attacker, skill, targetIds, null, inMsgData,true);
				break;
			case SHENQI:
				shenqiSkillSingleFire(stage, attacker, skill,target, targetIds, null, inMsgData);
				break;
			case BUFF:
				nomalSkillSingleFire(stage, attacker, skill, targetIds, null, inMsgData,true);
				break;
			default:
				break;
			}
			
		} catch (Exception e) {
			
			StageMsgSender.send2One(attacker.getId(), ClientCmdType.SKILL_FIRE_ERROR, new Object[]{skill.getCategory(),9,KuafuConfigPropUtil.isKuafuServer()});
			ChuanQiLog.error("",e);
		}
	}
	
	private void shenqiSkillSingleFire(IStage stage, IFighter attacker,ISkill skill,IFighter target,Object[] targetIds,Object[] helpParams, Object[] inMsgData){
		boolean flag = true;
		if(skill.getSkillConfig().getSkillFireType().equals(SkillFireType.SHENQI)){
			if(target==null){
				flag = false;
			}
			if(!stage.inScope(attacker.getPosition(), target.getPosition(), skill.getSkillConfig().getRange(), ScopeType.PIXEL)){
				flag = false;
			}
		}
		IRole role = (IRole) attacker;
		if(!flag){
			role.checkShenqiAttackSchedule();
			return;
		}
		
		nomalSkillSingleFire(stage, attacker, skill, targetIds, null, inMsgData,false);
		if(inMsgData[1] != null){
			inMsgData[3] = attacker.getId();
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.NOTICE_SHENQI_ATTACK, inMsgData);//new Object[]{attacker.getId(),targetIds[0]});
		}
		
		role.checkShenqiAttackSchedule();
	}
	/**
	 * 传送前加一个无敌状态
	 * @param role
	 */
	private void changeMapAddWuDiState(IRole role){
		//传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
	}
	
	
	/**
	 * 内部效果伤害技能
	 * @param stage
	 * @param attacker
	 * @param skill
	 * @param target
	 * @param helpParams
	 * @param inMsgData
	 * @param flagSend 是否发送600指令
	 */
	private void nomalSkillSingleFire(IStage stage, IFighter attacker,ISkill skill,Object[] targetIds,Object[] helpParams, Object[] inMsgData,boolean flagSend){
		/**
		 * 施法结果计算
		 * */
		IMsgWriter msgWriter = BufferedMsgWriter.getInstance();
		IMsgWriter directWriter = DirectMsgWriter.getInstance();
		
		
		//验证并且确认目标
		List<IFighter> targets = SkillProcessHelper.confirmTargets(skill, attacker, targetIds,helpParams, stage);
		if(null != targets && targets.size() > 0){
			//计算战斗结果
			List<Object[]> harmMsgs = SkillProcessHelper.fight(attacker, skill, targets, msgWriter);
			if(null != harmMsgs && harmMsgs.size() > 0){
				inMsgData[1] = harmMsgs.toArray();
			}else{
				if(targets != null && targets.size() > 0){
					Object[] damages = new Object[targets.size()];
					
					int index = 0;
					for (IFighter fighter : targets) {
						damages[index++] = new Object[]{fighter.getId()};
					}
					
					inMsgData[1] = damages;
				}
			}

			
			
			//位移处理
			inMsgData = skillWeiYiHandle(stage, attacker, skill, targetIds,targets,inMsgData);
			
		}else{
			inMsgData[1] = null;
		}
		
		if(flagSend){
			if(targets != null && targets.size() > 0){
				IFighter target = targets.get(0);
				directWriter.writeMsg(stage.getSurroundRoleIds(target.getPosition()), ClientCmdType.SKILL_FIRE, inMsgData);
			}else{
				directWriter.writeMsg(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.SKILL_FIRE, inMsgData);
			}
		}
		
		attacker.getFightStatistic().flushChanges(msgWriter);
		msgWriter.flush();
	}
	
	
	/**
	 * 普通技能释放
	 */
	private void normalSkillFire(IStage stage, IFighter attacker,ISkill skill, Object[] targetIds,Object[] helpParams, Object[] inMsgData){
		/**
		 * 施法结果计算
		 * */
		IMsgWriter msgWriter = BufferedMsgWriter.getInstance();
		
		IMsgWriter directWriter = DirectMsgWriter.getInstance();
		
		//验证并且确认目标
		List<IFighter> targets = SkillProcessHelper.confirmTargets(skill, attacker, targetIds,helpParams, stage);
		if(null != targets && targets.size() > 0){
			//计算战斗结果
			List<Object[]> harmMsgs = SkillProcessHelper.fight(attacker, skill, targets, msgWriter);
			if(null != harmMsgs && harmMsgs.size() > 0){
				inMsgData[1] = harmMsgs.toArray();
			}else{
				if(targets != null && targets.size() > 0){
					Object[] damages = new Object[targets.size()];
					
					int index = 0;
					for (IFighter fighter : targets) {
						damages[index++] = new Object[]{fighter.getId()};
					}
					
					inMsgData[1] = damages;
				}
			}

			
			
			//位移处理
			inMsgData = skillWeiYiHandle(stage, attacker, skill, targetIds,targets,inMsgData);
			
		}else{
			inMsgData[1] = null;
		}
		
		
		directWriter.writeMsg(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.SKILL_FIRE, inMsgData);
		
		attacker.getFightStatistic().flushChanges(msgWriter);
		msgWriter.flush();
		
	}
	
	/**
	 * 技能位移处理
	 * @param stage
	 * @param attacker
	 * @param skill
	 * @param inMsgData
	 * @return
	 */
	private Object[] skillWeiYiHandle(IStage stage, IFighter attacker,ISkill skill,Object[] targetIds,List<IFighter> targets,Object[] inMsgData){
		
		Object[] msgData = inMsgData;
		
		try {
			
			//位移处理
			SkillConfig skillConfig = skill.getSkillConfig();
			if(skillConfig.getWeiyi() > 0 && targets != null){
				Object[] targetXys = null;
				if(inMsgData.length <= 3){
					return inMsgData;
				}else{
					targetXys = (Object[]) inMsgData[4];
				}
				if(targetXys == null){
					return inMsgData;
				}
				
				//预处理目标
				Map<Long,Object[]> targetPoints = new HashMap<>();
				for (int i = 0; i < targetIds.length; i++) {
					Long id = CovertObjectUtil.object2Long(targetIds[i]);
					
					if(targetXys.length > i){
						//坐标
						Object[] tmpxy = (Object[]) targetXys[i];
						
						targetPoints.put(id, tmpxy);
					}
				}
				
				
				Map<Long,IFighter> noTelMap = new HashMap<Long,IFighter>();
				//位移
				for (IFighter fighter : targets) {
					Object[] tmpXy = targetPoints.get(fighter.getId());
					
					if(tmpXy != null){
						int x = Integer.parseInt(tmpXy[0].toString());
						int y = Integer.parseInt(tmpXy[1].toString());;
						
						//能否位移
						boolean canWeiYi = checkWeiYi(attacker, fighter, skillConfig, x, y);
						if(canWeiYi){
							stage.teleportTo(fighter, x, y);
						}else{
							noTelMap.put(fighter.getId(),fighter);
						}
						
					}else{
						noTelMap.put(fighter.getId(),fighter);
					}
				}
				
				Object[] points = new Object[targets.size()];
				
				Object[] tmpIds = (Object[]) inMsgData[1];
				//按目标顺序输出数据
				for (int i = 0; i < tmpIds.length; i++) {
					Object[] damageInfo = (Object[]) tmpIds[i];
					Long targetRoleId = (Long)damageInfo[0];
					
					if(noTelMap.containsKey(targetRoleId)){
						IFighter target = noTelMap.get(targetRoleId);
						points[i] = new Object[]{target.getPosition().getX(),target.getPosition().getY()};
					}else{
						points[i] = targetPoints.get(targetRoleId);
					}
				}
			
				msgData[4] = points;
			}
			
		} catch (Exception e) {
			ChuanQiLog.error(" skillId:"+skill.getId(),e);
		}
		
		return msgData;
	}
	
	
	/**
	 * 战士冲撞自己坐标验证
	 * @param attacker
	 * @param skillConfig
	 * @param moveX
	 * @param moveY
	 * @return
	 */
	private boolean checkAttackCanMove(IFighter attacker,int moveX,int moveY){
		//不能超过格子数
		boolean inScope = attacker.getStage().isScopeGeZi(attacker.getPosition(), moveX,moveY, 6);
		if(!inScope){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 验证是否能位移
	 * @param attacker
	 * @param target
	 * @param skillConfig
	 * @param moveX
	 * @param moveY
	 * @return true:能
	 */
	private boolean checkWeiYi(IFighter attacker,IFighter target,SkillConfig skillConfig,int moveX,int moveY){
		
		//等级差
		SkillPublicConfig skillPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_SKILL);
		int levelCha = attacker.getLevel() - target.getLevel();
		if(skillPublicConfig == null || levelCha < skillPublicConfig.getLevel()){
			return false;
		}
		
		Point movePoint = new Point(moveX,moveY);
		
		//地图可走点
		boolean canMove = target.getStage().checkCanUseStagePoint(movePoint, PointTakeupType.BEING);
		if(!canMove){
			return false;
		}
		
		//不能超过格子数
		boolean inScope = target.getStage().isScopeGeZi(target.getPosition(), movePoint, skillConfig.getWeiyi());
		if(!inScope){
			return false;
		}
		
		
		return true;
	}
	
	private RoleBasePublicConfig getRoleBasePublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ROLE_BASE);
	}
	
	/**
	 * 穿越技能释放
	 */
	private void passThroughHarmFire(IStage stage, IFighter attacker,ISkill skill, Object[] targetIds,Object[] helpParams, Object[] inMsgData){
		
		/**
		 * 施法结果计算
		 * */
		IMsgWriter msgWriter = BufferedMsgWriter.getInstance();
		IMsgWriter directWriter = DirectMsgWriter.getInstance();
		inMsgData[1] = null;
		
		//验证并且确认目标
		List<IFighter> targets = SkillProcessHelper.confirmTargets(skill, attacker, targetIds,helpParams, stage);
		if(null != targets && targets.size() > 0){
			//计算战斗结果
			List<Object[]> harmMsgs = SkillProcessHelper.fight(attacker, skill, targets, msgWriter);
			if(null != harmMsgs && harmMsgs.size() > 0){
				inMsgData[1] = harmMsgs.toArray();
			}
		}
		directWriter.writeMsg(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.SKILL_FIRE, inMsgData);
		
		attacker.getFightStatistic().flushChanges(msgWriter);
		msgWriter.flush();
	}
	
	

	

	/**
	 * 初始陷阱验证坐标
	 */
	private List<int[]> getCheckPoint(List<int[]> others,int trapX,int trapY){
		if(others == null || others.size() == 0){
			return null;
		}
		
		List<int[]> checkPoints = new ArrayList<int[]>();
		
		checkPoints.add(new int[]{trapX,trapY});
		for (int[] configXy : others) {
			checkPoints.add(new int[]{trapX+configXy[0] ,trapY+configXy[1]});
		}
		
		return checkPoints;
	}

	
	/**
	 * 道具复活
	 * @param stageId
	 * @param roleId
	 */
	public void propRevive(String stageId, Long roleId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		if(!stage.canFuhuo()){
			return;
		}
		IRole role = stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(KuafuConfigPropUtil.isKuafuServer()){//跨服不执行本方法
			return;
		}else if(!KuafuManager.kuafuIng(roleId) && !role.getStateManager().isDead()){
			return;
		}
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(stage.getMapId());
		if(config == null){
			ChuanQiLog.error("ybRevive no revive config mapId:" + stage.getMapId());
			//异常地图 复活（在死亡地图找不到配置 用的复活地图配置）
			config = diTuConfigExportService.loadFirstMainDiTu();
		}
		
//		if(!config.isCanPropFuhuo()){
//			return;//本地图不可以道具复活
//		}
		
		List<String> ids = goodsConfigExportService.loadIdsById1(ModulePropIdConstant.SPOT_REVIVE_ITEM_ID);
		if(ids == null || ids.size() < 1){
			return;//游戏没有复活道具
		}
		String goodsId = null;
		for (String id : ids) {
			Object[] obj = roleBagExportService.checkRemoveBagItemByGoodsId(id, 1, roleId);
			if(obj == null){//有复活石
				goodsId = id;
				break;
			}
		}
		
		if(goodsId == null){
			return;//没有复活石
		}
		//消耗复活石
		roleBagExportService.removeBagItemByGoodsId(goodsId, 1, roleId, GoodsSource.PROP_REVIVE, true, true);
		
		if(KuafuManager.kuafuIng(roleId)){
			//跨服中转发到跨服处理复活
			KuafuMsgSender.send2KuafuServer(roleId,roleId, InnerCmdType.KF_PROP_REVIVE, null);
		}else{
			//去除死亡状态
			role.getStateManager().remove(StateType.DEAD);
			role.getFightAttribute().resetHp();
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
		}
	}
	
	/**
	 * 道具复活（通知到跨服执行复活操作）
	 * @param stageId
	 * @param roleId
	 */
	public void kfPropRevive(String stageId, Long roleId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(!role.getStateManager().isDead()){
			return;
		}
		//如果是在跨服大乱斗，则加一个无敌buff  5秒后取消
		if(StageType.KUAFUDALUANDOU.equals(stage.getStageType())){
			ChuanQiLog.error("跨服乱斗普通复活");
			changeMapAddWuDiState(role);
			KuafuLuanDouStage kstage = (KuafuLuanDouStage)stage;
			kstage.schedulerQuXiaoFuHou(roleId);
		}
		role.getStateManager().remove(StateType.DEAD);
		role.getFightAttribute().resetHp();
		IMsgWriter writer = BufferedMsgWriter.getInstance();
		role.getFightStatistic().flushChanges(writer);
		writer.flush();
		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
	}

	/**
	 * 普通复活
	 * @param stageId
	 * @param roleId
	 */
	public void townRevive(String stageId, Long roleId) {
		if(KuafuConfigPropUtil.isKuafuServer()){
			return;//跨服不支持普通复活
		}
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole attacker = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(attacker == null){
			return;
		}
		
		//取消强制复活定时
		attacker.cancelBackFuhuoSchedule();
		
		//复活前加一个无敌状态
		changeMapAddWuDiState(attacker);
		
		if(attacker.getStateManager().remove(StateType.DEAD)){
			
			RoleStage roleStage = roleStageExportService.getRoleStage(roleId);
			
			DiTuConfig config = diTuConfigExportService.loadDiTu(roleStage.getLastMainMap());
			if(config == null){
				ChuanQiLog.error("townRevive no revive config mapId:" + stage.getMapId());
				
				//异常地图 复活（在死亡地图找不到配置 用的复活地图配置）
				config = diTuConfigExportService.loadFirstMainDiTu();
			}
			
			//红名处理
			if(attacker.isRedRole()){
//				PKPublicConfig pKPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_PK);
//				DiTuConfig pkConfig = diTuConfigExportService.loadDiTu(pKPublicConfig.getPkMapId());
//				if(pkConfig != null){
//					config = pkConfig;
//				}
			}
			
			//按比率加血和蓝
			RoleBasePublicConfig publicConfig = getRoleBasePublicConfig();
			
			Float curHp = attacker.getFightAttribute().getMaxHp() * publicConfig.getPtHpBv();
			attacker.getFightAttribute().setCurHp(curHp.intValue());
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			attacker.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
			
			int[] birthXy = config.getRandomBirth();
			int x = birthXy[0];
			int y = birthXy[1];
			
			//通知移动到指定地图及坐标
			if(config.getId() == stage.getMapId()){
				
//				if(attacker.getPropModel() != null){
//					attacker.getPropModel().start();
//				}
				
				stage.teleportTo(attacker, x,y);
				//复活后取消无敌状态
				attacker.getStateManager().remove(StateType.NO_ATTACKED);
				StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,attacker.getPosition().getX(),attacker.getPosition().getY()});
			}else{
				
				//异常处理 [当客户端发出回城复活后,崩溃不会再回发2100,最后人物会在原地复活]
				stageService.saveExceptionStageData(roleId, config.getId());
				StageMsgSender.send2StageControl(roleId, InnerCmdType.S_APPLY_CHANGE_STAGE, new Object[]{config.getId(),x,y});
			}
		}else{
			//复活失败，取消复活前的无敌
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
		}
		
	}
	
	
	
	public Object getOtherRoleMp(String stageId, Long roleId,Long otherRoleId){
		return null;
	}
	
	/**
	 * 清除自己的其它的监听
	 * @param stage
	 * @param roleId
	 * @param otherRoleId
	 */
	private void clearJtRoleId(IStage stage,Long roleId){
		Long selfAttentionRoleId = null;
		
		IRole selfRole = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(selfRole != null){
			//获取自己当前关注的人roleId
			selfAttentionRoleId = selfRole.getBusinessData().getSelfAttentionRoleId();
			
			if(selfAttentionRoleId != null){
				IRole otherRole = (IRole)stage.getElement(selfAttentionRoleId, ElementType.ROLE);
				if(otherRole != null){
					//把自己从他的被关注列表中删除
					otherRole.getBusinessData().removeAttentionRoleId(roleId);
				}
			}
			
			//清空自己关注的人
			selfRole.getBusinessData().setSelfAttentionRoleId(null);
		}
	}

	public void setBattleMode(String stageId, Long roleId, int mode) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		Integer mapId = stage.getMapId();
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(mapId);
		if(diTuConfig != null && diTuConfig.getMode() > 0){
			return;
		}
		
		
		IFighter attacker = (IFighter)stage.getElement(roleId, ElementType.FIGHTER);
		if(attacker == null){
			return;
		}
		
		attacker.setBattleMode(BattleMode.convert(mode));
		
	}
	
	public void fightStateCheck(String stageId, Long roleId) {
		IStage stage = StageManager.getStage(stageId);
		if(null != stage){
			
			IRole role = (IRole)stage.getElement(roleId, ElementType.FIGHTER);
			if(role == null){
				return;
			}
			IRoleHatredManager manager = (IRoleHatredManager)role.getHatredManager();
			manager.refreshHatred();
			if(null != manager.getHatredest()){
				manager.scheduleFightStateCheck();
			}else{
				if(role.getStateManager().remove(StateType.FIGHT)){
					//通知战斗状态结束
//					StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), StageCommands.FIGHT_STATE_END, StageOutputWrapper.fightEnd(role));
				}
			}
		}
	}
	
	private void nuqiSkillFire(IFighter fighter, ISkill skill){
		if(!ElementType.isRole(fighter.getElementType())){
			return;
		}
		IRole role = (IRole) fighter;
		NuqiPublicConfig config = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_NUQI);
		if(config == null){
			return;
		}
		if(config.getMaxNq() > role.getNuqi()){
			return;
		}
		role.setNuqiNotice(0);
		role.startNuqiSchedule(true);
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2KuafuSource(role.getId(), InnerCmdType.INNER_CLEAR_NUQI, null);
		}
		NuqiBuff buff = new NuqiBuff(role,skill.getCategory());
		role.getBuffManager().addBuff(buff);
	}
	/**
	 * 瞬移后删除无敌状态
	 * @param role
	 */
	private void removeMapAddWuDiState(IRole role){
		role.getStateManager().remove(StateType.NO_ATTACKED);
	}
	/**
	 * 获取阵营战公共配置
	 * @return
	 */
	private CampPublicConfig getCampPubligConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_CAMP);
	}
	
	/**
	 * 阵营战复活
	 * @param stageId
	 * @param userRoleId
	 */
	public void campFuhuo(String stageId, Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(role.getStateManager().remove(StateType.DEAD)){
			//传送前加一个无敌状态
			changeMapAddWuDiState(role);
			
			CampPublicConfig campPublicConfig = getCampPubligConfig();
			int[] point = null;
			if(campPublicConfig != null && role.getZyCamp() != null){
				point = campPublicConfig.getPoints(role.getZyCamp());
			}
			
			if(point == null){
				RoleStage roleStage = roleStageExportService.getRoleStage(userRoleId);
				DiTuConfig config = diTuConfigExportService.loadDiTu(roleStage.getLastMainMap());
				if(config == null){
					ChuanQiLog.error("townRevive no revive config mapId:" + stage.getMapId());
					
					//异常地图 复活（在死亡地图找不到配置 用的复活地图配置）
					config = diTuConfigExportService.loadFirstMainDiTu();
				}
				
				point = config.getRandomBirth();
			}
			int x = point[0];
			int y = point[1];
			
			RoleBasePublicConfig publicConfig = getRoleBasePublicConfig();
			//取消强制复活定时
			role.cancelBackFuhuoSchedule();
			
			role.getFightAttribute().setCurHp((int)(role.getFightAttribute().getMaxHp() * publicConfig.getPtHpBv()));
//			role.getFightAttribute().setCurMp((int)(role.getFightAttribute().getMaxMp() * publicConfig.getPtMpBv()));
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, userRoleId);
			
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo(), GameConstants.IS_CLEAR_BUFF});
			
			stage.teleportTo(role, x,y);
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			//3.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId, role.getPosition().getX(),role.getPosition().getY()});
		}
	}
	/**
	 * 领地战复活
	 * @param stageId
	 * @param userRoleId
	 */
	public void territoryFuhuo(String stageId, Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(!stage.getStageType().equals(StageType.TERRITORY_WAR) || !ActiveUtil.isTerritory()){
			townRevive(stageId, userRoleId);
			return;
		}

		TerritoryConfig tConfig = territoryConfigExportService.getConfigByMapId(stage.getMapId());
		if(tConfig == null){
			townRevive(stageId, userRoleId);
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(role.getStateManager().remove(StateType.DEAD)){
			//传送前加一个无敌状态
			changeMapAddWuDiState(role);
			int[] point = null;
			if(role.getBusinessData().getGuildId() == null){
				point = tConfig.getFuhuo1();
			}else{
				TerritoryStage tStage = (TerritoryStage)stage;
				if(tStage.getOwnerGuildId() == null){
					point = tConfig.getFuhuo1();
				}else{
					if(role.getBusinessData().getGuildId().longValue() == tStage.getOwnerGuildId().longValue() ){
						point = tConfig.getFuhuo2();
					}else{
						point = tConfig.getFuhuo1();
					}
				}
			}
			int x = point[0];
			int y = point[1];
			
			RoleBasePublicConfig publicConfig = getRoleBasePublicConfig();
			//取消强制复活定时
			role.cancelBackFuhuoSchedule();
			
			role.getFightAttribute().setCurHp((int)(role.getFightAttribute().getMaxHp() * publicConfig.getPtHpBv()));
//			role.getFightAttribute().setCurMp((int)(role.getFightAttribute().getMaxMp() * publicConfig.getPtMpBv()));
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, userRoleId);
			
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo(), GameConstants.IS_CLEAR_BUFF});
			
			stage.teleportTo(role, x,y);
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			//3.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId, role.getPosition().getX(),role.getPosition().getY()});
		}
	}
	/**
	 * 八卦战复活
	 * @param stageId
	 * @param userRoleId
	 */
	public void baguaFuhuo(String stageId, Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(!stage.getStageType().equals(StageType.BAGUA_FUBEN)){
			townRevive(stageId, userRoleId);
			return;
		}
		BaguaPublicConfig baguaPublicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_BAGUA);
		if(baguaPublicConfig == null){
			townRevive(stageId, userRoleId);
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(role.getStateManager().remove(StateType.DEAD)){
			//传送前加一个无敌状态
			changeMapAddWuDiState(role);
			int[] point = baguaPublicConfig.getFuhuo();
			int x = point[0];
			int y = point[1];
			
			RoleBasePublicConfig publicConfig = getRoleBasePublicConfig();
			//取消强制复活定时
			role.cancelBackFuhuoSchedule();
			
			role.getFightAttribute().setCurHp((int)(role.getFightAttribute().getMaxHp() * publicConfig.getPtHpBv()));
//			role.getFightAttribute().setCurMp((int)(role.getFightAttribute().getMaxMp() * publicConfig.getPtMpBv()));
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, userRoleId);
			
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo(), GameConstants.IS_CLEAR_BUFF});
			
			stage.teleportTo(role, x,y);
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			//3.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId, role.getPosition().getX(),role.getPosition().getY()});
		}
	}
	/**
	 * 八卦战复活
	 * @param stageId
	 * @param userRoleId
	 */
	public void maiguFuhuo(String stageId, Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(!stage.getStageType().equals(StageType.MAIGU_FUBEN)){
			townRevive(stageId, userRoleId);
			return;
		}
		MaiguPublicConfig maiguPublicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_MAIGU);
		if(maiguPublicConfig == null){
			townRevive(stageId, userRoleId);
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(role.getStateManager().remove(StateType.DEAD)){
			//传送前加一个无敌状态
			changeMapAddWuDiState(role);
			int[] point = maiguPublicConfig.getShzuobiao();
			int x = point[0];
			int y = point[1];
			
			RoleBasePublicConfig publicConfig = getRoleBasePublicConfig();
			//取消强制复活定时
			role.cancelBackFuhuoSchedule();
			
			role.getFightAttribute().setCurHp((int)(role.getFightAttribute().getMaxHp() * publicConfig.getPtHpBv()));
//			role.getFightAttribute().setCurMp((int)(role.getFightAttribute().getMaxMp() * publicConfig.getPtMpBv()));
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, userRoleId);
			
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo(), GameConstants.IS_CLEAR_BUFF});
			
			stage.teleportTo(role, x,y);
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			//3.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId, role.getPosition().getX(),role.getPosition().getY()});
		}
	}
	/**
	 * 皇城争霸赛复活
	 * @param stageId
	 * @param userRoleId
	 */
	public void zbsFuhuo(String stageId, Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(!stage.getStageType().equals(StageType.HCZBS_WAR) || !ActiveUtil.isHcZBS()){
			townRevive(stageId, userRoleId);
			return;
		}
		HcZhengBaSaiConfig  tConfig =   hcZhengBaSaiConfigExportService.loadPublicConfig();
		
		if(tConfig == null){
			townRevive(stageId, userRoleId);
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(role.getStateManager().remove(StateType.DEAD)){
			//传送前加一个无敌状态
			changeMapAddWuDiState(role);
			int[] point = null;
			if(role.getBusinessData().getGuildId() == null){
				point = (int[])tConfig.getGongfuhuo()[new Random().nextInt(2)];
			}else{
				HcZhengBaSaiStage tStage = (HcZhengBaSaiStage)stage;
				if(tStage.getOwnerGuildId() == null){
					point = (int[])tConfig.getGongfuhuo()[new Random().nextInt(2)];
				}else{
					if(role.getBusinessData().getGuildId().longValue() == tStage.getOwnerGuildId().longValue() ){
						point = (int[])tConfig.getShoufuhuo()[new Random().nextInt(2)];
					}else{
						point =(int[])tConfig.getGongfuhuo()[new Random().nextInt(2)];
					}
				}
			}
			int x = point[0];
			int y = point[1];
			
			RoleBasePublicConfig publicConfig = getRoleBasePublicConfig();
			//取消强制复活定时
			role.cancelBackFuhuoSchedule();
			
			role.getFightAttribute().setCurHp((int)(role.getFightAttribute().getMaxHp() * publicConfig.getPtHpBv()));
//			role.getFightAttribute().setCurMp((int)(role.getFightAttribute().getMaxMp() * publicConfig.getPtMpBv()));
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, userRoleId);
			
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo(), GameConstants.IS_CLEAR_BUFF});
			
			stage.teleportTo(role, x,y);
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			//3.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId, role.getPosition().getX(),role.getPosition().getY()});
		}
	}
	
	/**
	 * 跨服云宫之巅复活
	 * @param stageId
	 * @param userRoleId
	 */
	public void kuafuYunGongFuhuo(String stageId, Long userRoleId){
	    IStage istage = StageManager.getStage(stageId);
	    if(istage == null || !istage.getStageType().equals(StageType.KUAFU_YUNGONG)){
	        return;
	    }
	    KuafuYunGongStage stage = (KuafuYunGongStage) istage;
	    if(!stage.isOpen()){
	        return;
	    }
	    IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
	    if(role == null){
	        return;
	    }
	    if(role.getStateManager().remove(StateType.DEAD)){
            // 取消强制复活定时
            role.cancelBackFuhuoSchedule();
            // 恢复血量
            role.getFightAttribute().setCurHp(role.getFightAttribute().getMaxHp());
            IMsgWriter writer = BufferedMsgWriter.getInstance();
            role.getFightStatistic().flushChanges(writer);
            writer.flush();
            StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, userRoleId);
            // 传送到复活坐标
            List<Integer[]> xyPointList = new ArrayList<Integer[]>();
            Long ownerGuildId = stage.getOwnerGuildId();
            KuafuYunGongPublicConfig config = stage.getConfig();
            if(null == ownerGuildId){
                xyPointList = config.getAllFuhuoPointList();
            }else if(ownerGuildId.equals(role.getBusinessData().getGuildId())){
                xyPointList = config.getGongfuhuo();
            }else{
                xyPointList = config.getShoufuhuo();
            }
            Integer[] xyPoint = xyPointList.get(RandomUtil.getIntRandomValue(xyPointList.size()));
            int x = xyPoint[0];
            int y = xyPoint[1];
            //传送前加一个无敌状态
            changeMapAddWuDiState(role);
            //1.给切换的人推送 100
            StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x, y,stage.getLineNo(), GameConstants.IS_CLEAR_BUFF});
            stage.teleportTo(role, x,y);
            //2.瞬移后删除无敌状态
            removeMapAddWuDiState(role);
            //3.给当前AOI区域内的其它人推送702 单位瞬间移动
            StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId, role.getPosition().getX(),role.getPosition().getY()});
            ChuanQiLog.info("跨服serverId={}云宫之巅复活", ChuanQiConfigUtil.getServerId());
	    }
	}
	
	public void shenqiAttack(String stageId,Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(role.getStateManager().isDead()){
			role.getBusinessData().setShenqiTargetId(0L);
			return;
		}
		Long guid = role.getBusinessData().getShenqiTargetId();
		IFighter target = null;
		if(guid != null && guid != 0){
			target = stage.getElement(guid, ElementType.FIGHTER);
			if(target != null){
				boolean isHatred = role.getHatredManager().checkTargetHatred(target);
				if(!isHatred){
					IHatred hatred= role.getHatredManager().getHatredest();
					if(hatred!=null){
						target = stage.getElement(hatred.getId(), hatred.getElementType());
					}
				}
			}else{
				IHatred hatred= role.getHatredManager().getHatredest();
				if(hatred != null){
					target = stage.getElement(hatred.getId(), hatred.getElementType());
				}
			}
		}else{
			IHatred hatred= role.getHatredManager().getHatredest();
			if(hatred != null){
				target = stage.getElement(hatred.getId(), hatred.getElementType());
			}
		}
		if(target == null){
			role.cancelShenqiAttackSchedule();
			return;
		}
		if(target.getStateManager().isDead()){
			role.getHatredManager().clearHatred(target.getId());
			role.checkShenqiAttackSchedule();
			return;
		}
		ISkill skill = SkillManager.getManager().getSkill(role.getBusinessData().getShenqiSkillId2());
		if(skill == null || skill.isCding(role.getPublicCdManager())){//先判断神器绝招是否可使用
			skill = SkillManager.getManager().getSkill(role.getBusinessData().getShenqiSkillId());
		}
		Object[] result = new Object[]{stageId,target.getId(),role.getId(),ElementType.ROLE,skill.getCategory(),target.getElementType()};
		StageMsgSender.send2StageInner(role.getId(), stageId, InnerCmdType.INNER_SKILL_SINGLE_FIRE,result);
	}
	
	/**
	 * 神魔战场复活复活
	 * @param stageId
	 * @param roleId
	 */
	public void shenmoRevive(String stageId, Long roleId) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.SHENMO_FUBEN.equals(iStage.getStageType())){
			return;
		}
		
		ShenMoStage stage = (ShenMoStage)iStage;
		
		IRole attacker = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(attacker == null){
			return;
		}
		
		int[] birthXy = stage.getRevivePoint(roleId);
		if(birthXy == null){
			return;
		}
		
		//取消强制复活定时
		attacker.cancelBackFuhuoSchedule();
		
		//复活前加一个无敌状态
		changeMapAddWuDiState(attacker);
		
		if(attacker.getStateManager().remove(StateType.DEAD)){
			
			attacker.getFightAttribute().resetHp();
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			attacker.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
			
			int x = birthXy[0];
			int y = birthXy[1];
			
			//通知移动到指定地图及坐标
			stage.teleportTo(attacker, x,y);
			//复活后取消无敌状态
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,attacker.getPosition().getX(),attacker.getPosition().getY()});
		}else{
			//复活失败，取消复活前的无敌
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
		}
	}
	
	/**
	 * 跨服boss复活
	 * @param stageId
	 * @param roleId
	 */
	public void kuafuBossRevive(String stageId, Long roleId) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.KUAFUBOSS.equals(iStage.getStageType())){
			return;
		}
		
		KuafuBossStage stage = (KuafuBossStage)iStage;
		
		IRole attacker = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(attacker == null){
			return;
		}
		
		int[] birthXy = stage.getRevivePoint();
		if(birthXy == null){
			return;
		}
		
		//取消强制复活定时
		attacker.cancelBackFuhuoSchedule();
		
		//复活前加一个无敌状态
		changeMapAddWuDiState(attacker);
		
		if(attacker.getStateManager().remove(StateType.DEAD)){
			
			attacker.getFightAttribute().resetHp();
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			attacker.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
			
			int x = birthXy[0];
			int y = birthXy[1];
			
			//通知移动到指定地图及坐标
			stage.teleportTo(attacker, x,y);
			//复活后取消无敌状态
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,attacker.getPosition().getX(),attacker.getPosition().getY()});
		}else{
			//复活失败，取消复活前的无敌
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
		}
	}
	/**
	 * 跨服群仙宴复活
	 * @param stageId
	 * @param roleId
	 */
	public void kuafuQunXianYanRevive(String stageId, Long roleId) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.KUAFUQUNXIANYAN.equals(iStage.getStageType())){
			return;
		}
		
		KuafuQunXianYanStage stage = (KuafuQunXianYanStage)iStage;
		
		IRole attacker = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(attacker == null){
			return;
		}
		DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(stage.getMapId());
		List<int[]> birthPointList = dituConfig.getBirthRandomPoints();
		if(birthPointList == null || birthPointList.size() <= 0){
			ChuanQiLog.error("qunxianyan dituConfig is null");
			return;
		}
		int[] birthXy = birthPointList.get(0);
		if(birthXy == null){
			return;
		}
		
		//取消强制复活定时
		attacker.cancelBackFuhuoSchedule();
		
		//复活前加一个无敌状态
		changeMapAddWuDiState(attacker);
		
		if(attacker.getStateManager().remove(StateType.DEAD)){
			
			attacker.getFightAttribute().resetHp();
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			attacker.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
			
			int x = birthXy[0];
			int y = birthXy[1];
			
			//通知移动到指定地图及坐标
			stage.teleportTo(attacker, x,y);
			//复活后取消无敌状态
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,attacker.getPosition().getX(),attacker.getPosition().getY()});
		}else{
			//复活失败，取消复活前的无敌
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
		}
	}
	/**
	 * 跨服大乱斗复活
	 * @param stageId
	 * @param roleId
	 */
	public void kuafuDaluandouRevive(String stageId, Long roleId) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.KUAFUDALUANDOU.equals(iStage.getStageType())){
			return;
		}
		
		KuafuLuanDouStage stage = (KuafuLuanDouStage)iStage;
		
		IRole attacker = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(attacker == null){
			return;
		}
		
		int[] birthXy = stage.getRevivePoint();
		if(birthXy == null){
			return;
		}
		
		//取消强制复活定时
		attacker.cancelBackFuhuoSchedule();
		
		//复活前加一个无敌状态
		changeMapAddWuDiState(attacker);
		
		if(attacker.getStateManager().remove(StateType.DEAD)){
			
			attacker.getFightAttribute().resetHp();
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			attacker.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
			
			int x = birthXy[0];
			int y = birthXy[1];
			
			//通知移动到指定地图及坐标
			stage.teleportTo(attacker, x,y);
			//复活后取消无敌状态
			//attacker.getStateManager().remove(StateType.NO_ATTACKED);
			//复活成功 5秒后取消无敌
			stage.schedulerQuXiaoFuHou(roleId);
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,attacker.getPosition().getX(),attacker.getPosition().getY()});
		}else{
			//复活失败，取消复活前的无敌
			attacker.getStateManager().remove(StateType.NO_ATTACKED);
		}
	}
	
	/**
	 * 装备复活
	 * @param stageId
	 * @param roleId
	 */
	public void yuPeiRevive(String stageId, Long roleId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		
		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.ZHUANBEI_FUHOU, roleId);
	}
	
	public void kuafuLuanDouQuXiaoWuDi(String stageId,Long userRoleId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.KUAFUDALUANDOU.equals(iStage.getStageType())){
			return;
		}
		
		KuafuLuanDouStage stage = (KuafuLuanDouStage)iStage;
		
		IRole attacker = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(attacker == null){
			return;
		}
		attacker.getStateManager().remove(StateType.NO_ATTACKED);
	}
}
