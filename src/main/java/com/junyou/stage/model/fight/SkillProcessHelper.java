/**
 * 
 */
package com.junyou.stage.model.fight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.activityboss.export.ActivityBossExportService;
import com.junyou.bus.activityboss.manage.ActivityBossManage;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1KuafuService;
import com.junyou.bus.shenmo.configure.ShenMoScoreConfig;
import com.junyou.bus.shenmo.service.KuafuArena4v4KuafuService;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathNodeCopy;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.gameconfig.publicconfig.configure.export.BaguaPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.CampPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.HcZBSPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuYunGongPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.ShenMoPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.TerritoryPublicConfig;
import com.junyou.stage.configure.SkillTargetChooseType;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.RoleBehaviourExportService;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightResult;
import com.junyou.stage.model.core.fight.IFightResultCalculator;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementSearchFilter;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateEventType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.fight.calculator.FightResultCalculatorType;
import com.junyou.stage.model.skill.harm.Harm;
import com.junyou.stage.model.skill.harm.HarmPrintUtils;
import com.junyou.stage.model.skill.harm.HarmType;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.model.stage.kuafudianfeng.KuafuDianFengStage;
import com.junyou.stage.model.stage.kuafuyungong.KuafuYunGongStage;
import com.junyou.stage.model.stage.territory.TerritoryStage;
import com.junyou.stage.model.stage.zhengbasai.HcZhengBaSaiStage;
import com.junyou.stage.tafang.stage.TaFangMonster;
import com.junyou.stage.tafang.stage.TaFangNpc;
import com.junyou.stage.tafang.stage.TaFangStage;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.lottery.MathUtils;

/**
 * @description 技能流程帮助类
 * @author ShiJie Chi
 * @created 2011-12-5下午3:37:38
 */
@Component
public class SkillProcessHelper {

	private static RoleBehaviourExportService roleBehaviourExportService;
	private static ActivityBossExportService activityBossExportService;
	private static GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	private static KuafuArena1v1KuafuService kuafuArena1v1KuafuService;
	private static KuafuArena4v4KuafuService kuafuArena4v4KuafuService;
	@Autowired
	public void setKuafuArena4v4KuafuService(KuafuArena4v4KuafuService kuafuArena4v4KuafuService) {
		SkillProcessHelper.kuafuArena4v4KuafuService = kuafuArena4v4KuafuService;
	}
	@Autowired
	public void setKuafuArena1v1KuafuService(KuafuArena1v1KuafuService kuafuArena1v1KuafuService) {
		SkillProcessHelper.kuafuArena1v1KuafuService = kuafuArena1v1KuafuService;
	}
	@Autowired
	public void setActivityBossExportService(ActivityBossExportService activityBossExportService) {
		SkillProcessHelper.activityBossExportService = activityBossExportService;
	}

	@Autowired
	public void setRoleBehaviourExportService(RoleBehaviourExportService roleBehaviourExportService) {
		SkillProcessHelper.roleBehaviourExportService = roleBehaviourExportService;
	}

	@Autowired
	public void setGongGongShuJuBiaoConfigExportService(GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService) {
		SkillProcessHelper.gongGongShuJuBiaoConfigExportService = gongGongShuJuBiaoConfigExportService;
	}

    /**
	 * 技能释放验证
	 * 
	 * 1、施法场景验证 2、技能消耗/cd验证 3、施法者验证
	 */
	public static boolean skillFireCheck(IStage stage, IFighter fighter, ISkill skill) {

		return (null != skill) && SkillProcessHelper.stageCheck(stage) && SkillProcessHelper.fighterCheck(fighter);
	}

	/**
	 * 技能准备释放验证
	 */
	public static boolean skillReadyFireCheck(IStage stage, IFighter fighter, ISkill skill) {

		return (null != skill) && SkillProcessHelper.stageCheck(stage) && SkillProcessHelper.skillCheck(skill, fighter) && SkillProcessHelper.fighterCheck(fighter);
	}

	/**
	 * 技能准备释放验证
	 */
	public static boolean skillInnerReadyFireCheck(IStage stage, IFighter fighter, ISkill skill) {

		return (null != skill) && SkillProcessHelper.stageCheck(stage);
	}

	/**
	 * 施法场景验证
	 */
	public static boolean stageCheck(IStage stage) {
		return true;
	}

	/**
	 * 施法者验证
	 * 
	 * @param
	 */
	public static boolean fighterCheck(IFighter attacker) {
		// 角色有无敌状态 不可以攻击
		if (attacker.getStateManager().contains(StateType.WUDI_NOMOVE)) {
			return false;
		}

		return !attacker.getStateManager().isDead() && !attacker.getStateManager().isForbidden(StateEventType.ATTACK);
	}

	/**
	 * 技能cd验证
	 */
	public static boolean skillCheck(ISkill skill, IFighter attacker) {
		return !skill.isCding(attacker.getPublicCdManager());
	}

	/**
	 * 进入cd
	 */
	public static void skillReadyFireConsume(IStage stage, IFighter attacker, ISkill skill) {
		// 技能cd
		skill.toCd(attacker);

	}

	/**
	 * 选择目标
	 */
	public static Collection<IFighter> chooseTargets(IStage stage, IFighter attacker, ISkill skill, Object[] helpParams) {
		IElementSearchFilter searchFilter = new SkillTargetFilter(stage, attacker, skill, helpParams);
		return chooseTargets(stage, attacker, skill, helpParams, searchFilter);
	}

	/**
	 * 选择目标
	 */
	public static Collection<IFighter> chooseTargets(IStage stage, IFighter attacker, ISkill skill, Object[] helpParams, IElementSearchFilter searchFilter) {

		Collection<IFighter> result = null;

		SkillConfig skillConfig = skill.getSkillConfig();

		if (SkillTargetChooseType.isSelf(skillConfig.getTargetType())) {
			// self
			result = new ArrayList<IFighter>();
			result.add(attacker);

		} else if (SkillTargetChooseType.isSingleTarget(skillConfig.getTargetType())) {
			// sigle enemy
			IHatred hatred = null;

			hatred = attacker.getOwner().getHatredManager().getLastActiveAttackTarget();

			if (null != hatred) {
				result = new ArrayList<IFighter>();
				IFighter target = stage.getElement(hatred.getId(), hatred.getElementType());

				if (target != null && searchFilter.check(target)) {
					result.add(target);
				}
			}

		} else if (SkillTargetChooseType.isAOE(skillConfig.getTargetType())) {
			Collection<IStageElement> enemies = null;

			enemies = stage.getSurroundElements(attacker.getPosition(), ElementType.FIGHTER, searchFilter);

			if (enemies != null && enemies.size() > 0) {
				result = new ArrayList<IFighter>();

				for (IStageElement stageElement : enemies) {

					result.add((IFighter) stageElement);
				}
			}

//			result = stage.getSurroundElements(attacker.getPosition(), ElementType.FIGHTER, searchFilter);
		}else if (SkillTargetChooseType.isTaFang(skillConfig.getTargetType())){
			//塔防技能
			if(helpParams != null && StageType.TAFANG_FUBEN.equals(stage.getStageType())){
				Monster monster = (Monster) attacker;
				if(monster.isTaFangNpc()){
					TaFangNpc npc = (TaFangNpc)attacker;
					List<Integer> lines = npc.getAttLine();
					if(lines != null && lines.size() > 0){
						result = new ArrayList<>();
						for (Integer line : lines) {
							TaFangStage tfStage = (TaFangStage)stage;
							List<TaFangMonster> list = tfStage.getMonsters(line);
							if(list != null && list.size() > 0){
								int max = Math.min(list.size(), skillConfig.getMaxTarget());
								for (int i = 0; i < max; i++) {
									result.add(list.get(i));
								}
							}
						}
					}
				}
			}
		}

		return result;

	}

	/**
	 * 攻击目标确认
	 * 
	 * @param skill
	 * @param attacker
	 * @param recommendTargetIds
	 * @param stage
	 */
	public static List<IFighter> confirmTargets(ISkill skill, IFighter attacker, Object[] recommendTargetIds, Object[] helpParams, IStage stage) {
		List<IFighter> result = new ArrayList<IFighter>();

		SkillConfig skillConfig = skill.getSkillConfig();
		if (SkillTargetChooseType.isSelf(skillConfig.getTargetType())) {
			result.add(attacker);

		} else {
			if (null == recommendTargetIds) {
				return null;
			}
			SkillTargetFilter targetFileter = new SkillTargetFilter(stage, attacker, skill, helpParams);

			for (Object targetId : recommendTargetIds) {
				IFighter element = (IFighter) stage.getElement(CovertObjectUtil.object2Long(targetId), ElementType.FIGHTER);
				if (null != element) {
					if (!targetFileter.isEnough() && targetFileter.check(element)) {
						// 攻击阵型上目标选择
						List<IFighter> fighters = skill.getTarget().chooseTargets(skill, attacker, element);
						result.addAll(fighters);
					}
				}
			}
		}

		return result;
	}

	/**
	 * 内部指令选择攻击目标
	 * 
	 * @param skill
	 * @param attacker
	 * @param helpParams
	 * @param stage
	 * @return
	 */
	public static Object[] innerConfirmTargets(ISkill skill, IFighter attacker, Object[] helpParams, IStage stage) {
		Object[] result = null;

		IElementSearchFilter searchFilter = new SkillTargetFilter(stage, attacker, skill, helpParams);
		Collection<IFighter> targets = stage.getSurroundElements(attacker.getPosition(), ElementType.FIGHTER, searchFilter);
		if (targets != null && targets.size() > 0) {
			result = new Object[targets.size()];

			int index = 0;
			for (IFighter iFighter : targets) {
				result[index++] = iFighter.getId();
			}
		}

		return result;
	}

	public static List<IFighter> confirmTargets(ISkill skill, IFighter attacker, Collection<IFighter> targets, Object[] helpParams, IStage stage) {

		if (null == targets) {
			return null;
		}

		List<IFighter> result = new ArrayList<IFighter>();

		SkillConfig config = skill.getSkillConfig();
		TargetCounter counter = new TargetCounter(config.getMaxTarget());
		for (IFighter element : targets) {
			if (null != element) {

				ConfirmDraft draft = new ConfirmDraft();
				if (

				!counter.max() && skill.getSkillFirePath().inRange(skill, attacker, element, helpParams, stage, draft) && attackedAblecheck.check(skill, attacker, element, stage)) {

					result.add(element);

					counter.incr();
				}
			}
		}

		return result;

	}

	/**
	 * 攻击计算
	 * 
	 * @param fighter
	 * @param targets
	 * @return [[attackerId,harmType,harmVal]...]
	 */
	public static List<Object[]> fight(IFighter fighter, ISkill skill, Collection<IFighter> targets, IMsgWriter msgWriter) {
		if (null == targets) {
			return null;
		}
		
		List<Object[]> harms = new ArrayList<Object[]>();

		boolean isHarm = false;// 技能是否造成伤害
		boolean isCamp = false;// 是否是阵营战场
		int zyHarm = 0;// 阵营战固定伤害
		int baguaHarm = 0;// 八卦固定伤害
		boolean isBagua = false;
		// 阵营战
		if (fighter.getStage() != null && fighter.getStage().getStageType().equals(StageType.CAMP)) {
			isCamp = true;
			isHarm = StageConfigureHelper.getZhenYinZhanJiNengBiaoConfigExportService().check(skill.getCategory());
			CampPublicConfig config = StageConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_CAMP);
			zyHarm = config == null ? 0 : config.getHarm();
		}
		boolean isKuafuArena = fighter.getStage().getStageType().equals(StageType.KUAFU_ARENA_SINGLE);
		if(isKuafuArena&&!kuafuArena1v1KuafuService.isKuafuArenaStart(fighter.getId())){
			return null;
		}
		boolean isShenmo = fighter.getStage().getStageType().equals(StageType.SHENMO_FUBEN);
		if(isShenmo&&!kuafuArena4v4KuafuService.isKuafuArenaStart(fighter.getId())){
			return null;
		}
		
        if(fighter.getStage().getStageType().equals(StageType.KUAFUDIANFENG)){
            KuafuDianFengStage dfStage = (KuafuDianFengStage) fighter.getStage();
            if(!dfStage.isCanAttack()){
                return null;
            }
        }
        
		// 领地战
		boolean isTerritory = fighter.getStage().getStageType().equals(StageType.TERRITORY_WAR);
		//皇城争霸赛
		boolean isHcZbs = fighter.getStage().getStageType().equals(StageType.HCZBS_WAR);
		//跨服云宫之巅场景
		boolean isKfYunGong = fighter.getStage().getStageType().equals(StageType.KUAFU_YUNGONG);
		
		if(fighter.getStage().getStageType().equals(StageType.BAGUA_FUBEN)){
			isBagua = true;
			isHarm = true;
			BaguaPublicConfig config = StageConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_BAGUA);
			baguaHarm = config == null ? 0 : config.getShanghai();
		}
		
		for (IFighter target : targets) {
			boolean territoryNoHarm = false;// 领地战伤害
			boolean isTerritoryLd = false;// 领地做龙腾
			boolean hcZbsNoHarm = false;// 争霸赛伤害
			boolean isHcZbsLd = false;// 争霸赛龙腾
			boolean hcZbsFlagRoleNoHarm = false;// 持旗人攻击不造成
			if (!fighter.getId().equals(target.getId()) && target.getStateManager().isForbidden(StateEventType.ATTACKED)) {
				continue;
			}
			if (target.getStateManager().isDead()) {
				continue;
			}

			// 过滤镖车被非伤害技能处理
			if (ElementType.isBiaoChe(target.getElementType()) && !skill.getDamageType().equals(FightResultCalculatorType.SHANGHAI)) {
				continue;
			}

			if (ElementType.isRole(target.getElementType())) {

				// 如果在跳跃中,那么就需要检查是否有跳跃值了。可以用1点跳跃值抵消一次攻击
				if (roleBehaviourExportService.jumpMissHandle((Role) target)) {
					continue;
				}

				// 被攻击了取消打坐状态
				roleBehaviourExportService.daZuoCancelHandle((Role) target);
			}
			if (isCamp && ElementType.isRoleOrPet(target.getElementType()) && ElementType.isRoleOrPet(fighter.getElementType())) {
				IRole targetRole = (IRole) target.getOwner();
				IRole fightRole = (IRole) fighter.getOwner();
				if (targetRole.getZyCamp().equals(fightRole.getZyCamp())) {
					continue;
				}
			}
			if (isTerritory && ElementType.isRoleOrPet(target.getElementType()) && ElementType.isRoleOrPet(fighter.getElementType())) {
				IRole fightRole = (IRole) fighter.getOwner();
				IRole targetRole = (IRole) target.getOwner();
				Long fightGuildId = fightRole.getBusinessData().getGuildId();
				Long targetGuildId = targetRole.getBusinessData().getGuildId();
				// 同一个帮派不能攻击
				if (fightGuildId != null && targetGuildId != null && fightGuildId.longValue() == targetGuildId.longValue()) {
					territoryNoHarm = true;
					continue;
				}
				TerritoryStage tStage = (TerritoryStage) fighter.getStage();
				IRole flagOwner = tStage.getFlagOwnerRole();
				if(flagOwner!=null && flagOwner.getId().longValue() == fightRole.getId().longValue()){
					territoryNoHarm = true;
					continue;
				}
			}

			if (isTerritory && ElementType.isRoleOrPet(fighter.getElementType()) && ElementType.isMonster(target.getElementType())) {
				Monster monster = (Monster) target;
				if (monster.isTerritoryMonster()) {
					isTerritoryLd = true;
					// 判断当前攻击玩家是不是图腾守护者
					IRole fightRole = (IRole) fighter.getOwner();
					TerritoryStage tStage = (TerritoryStage) fightRole.getStage();
					if (tStage.getOwnerGuildId() == null) {
						territoryNoHarm = true;
						continue;
					}
					if (fightRole.getBusinessData().getGuildId() != null && tStage.getOwnerGuildId().longValue() == fightRole.getBusinessData().getGuildId().longValue()) {
						territoryNoHarm = true;
						continue;
					}
					if (fightRole.getBusinessData().getGuildId() == null) {
						territoryNoHarm = true;
						continue;
					}
				}
			}
			if (isHcZbs && ElementType.isRoleOrPet(target.getElementType()) && ElementType.isRoleOrPet(fighter.getElementType())) {
				IRole fightRole = (IRole) fighter.getOwner();
				IRole targetRole = (IRole) target.getOwner();
				Long fightGuildId = fightRole.getBusinessData().getGuildId();
				Long targetGuildId = targetRole.getBusinessData().getGuildId();
				// 同一个帮派不能攻击
				if (fightGuildId != null && targetGuildId != null && fightGuildId.longValue() == targetGuildId.longValue()) {
					hcZbsNoHarm  = true;
					continue;
				}
			}
			
			if (isHcZbs && ElementType.isRoleOrPet(target.getElementType()) && ElementType.isRoleOrPet(fighter.getElementType())) {
				IRole fightRole = (IRole) fighter.getOwner();
				//持旗人攻击其他人的伤害为0 ，对其他人不造成伤害
				HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) fightRole.getStage();
				if(tStage.getFlagOwnerRole()!=null && fightRole.getId().longValue()==tStage.getFlagOwnerRole().getId().longValue()){
					hcZbsFlagRoleNoHarm  = true;
					continue;
				}
			}
			
			if (isHcZbs && ElementType.isRoleOrPet(fighter.getElementType()) && ElementType.isMonster(target.getElementType())) {
				Monster monster = (Monster) target;
				if (monster.isHcZBSMonster()) {
					isHcZbsLd  = true;
					// 判断当前攻击玩家是不是图腾守护者
					IRole fightRole = (IRole) fighter.getOwner();
					HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) fightRole.getStage();
					if (tStage.getOwnerGuildId() == null) {
						hcZbsNoHarm  = true;
						continue;
					}
					if (fightRole.getBusinessData().getGuildId() != null && tStage.getOwnerGuildId().longValue() == fightRole.getBusinessData().getGuildId().longValue()) {
						hcZbsNoHarm  = true;
						continue;
					}
					if (fightRole.getBusinessData().getGuildId() == null) {
						hcZbsNoHarm  = true;
						continue;
					}
				}
			}
			
            /**
             * 
             * 跨服云宫之巅场景攻击限制
             *  1.同一公会不可攻击 
             *  2.旗子没有被拔起之前(即没有扛旗者),不同公会之间可以攻击,可以被杀死
             * 3.旗子拔起,产生图腾之后(有扛旗者),扛旗者对任何人和图腾都不能攻击,非拔旗者公会的玩家对图腾产生固定伤害
             * 4.糖宝与玩家具有相同的攻击限制
             */
            boolean isKfYunGongTuteng = false;
            if (isKfYunGong && ElementType.isRoleOrPet(fighter.getElementType())) {// 攻击者是玩家或者糖宝
                IRole atkFighter = (IRole) fighter.getOwner();// 获取释放技能的主人,攻击方
                KuafuYunGongStage stage = (KuafuYunGongStage) fighter.getStage();
                IRole ownerRole = stage.getOwnerRole(); // 场景中的扛旗者
                if (ownerRole != null && ownerRole.getId().equals(atkFighter.getId())) {// 扛旗者对任何人和图腾攻击无效
                    continue;
                }
                if (ElementType.isRoleOrPet(target.getElementType())) {// 被攻击是玩家或者糖宝
                    // 同一公会不可攻击
                    IRole targetFighter = (IRole) target.getOwner();
                    if (atkFighter.getBusinessData().getGuildId().equals(targetFighter.getBusinessData().getGuildId())) {
                        continue;
                    }
                } else if (ElementType.isMonster(target.getElementType())) {// 产生图腾了,意味着场景中出现了扛旗者
                    Monster monster = (Monster) target;
                    if (monster.isKfYunGongMonster()) {
                        isKfYunGongTuteng = true;
                        if (atkFighter.getBusinessData().getGuildId().equals(stage.getOwnerGuildId())) {// 扛旗者所属公会都不可以攻击图腾怪物
                            continue;
                        }
                    }
                }
            }
			
			IHarm harm = null;
			if (isCamp && target.getElementType().equals(ElementType.MONSTER)) {
				if (!isHarm) {
					zyHarm = 0;// 该技能不能造成伤害
				}

				// 阵营战场技能处理
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, zyHarm);

				long hp = MathUtils.max(0, target.getFightAttribute().getCurHp() - zyHarm);
				target.getFightAttribute().setCurHp(hp);
				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			}else if(isBagua && target.getElementType().equals(ElementType.MONSTER)){
				if (!isHarm) {
					baguaHarm = 0;// 该技能不能造成伤害
				}

				// 八卦战场技能处理
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, baguaHarm);

				long hp = MathUtils.max(0, target.getFightAttribute().getCurHp() - baguaHarm);
				target.getFightAttribute().setCurHp(hp);
				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			}else if(isShenmo && target.getElementType().equals(ElementType.MONSTER) && ((Monster) target).isShenmoShuijing()){
				int shenmoShuijingHarm =0;
				ShenMoScoreConfig shenMoScoreConfig = StageConfigureHelper.getShenMoScoreConfigExportService().getConfig(((Monster) target).getMonsterId());
				if(shenMoScoreConfig!=null){
					IRole fightRole = (IRole) fighter.getOwner();
					if(shenMoScoreConfig.getTeamId()!=fightRole.getTeamId().intValue()){
						ShenMoPublicConfig config = StageConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_SHENMO);
						shenmoShuijingHarm = config == null ? 0 : config.getGongji();
					}
				}
				// 神魔战场技能处理
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, shenmoShuijingHarm);

				long hp = MathUtils.max(0, target.getFightAttribute().getCurHp() - shenmoShuijingHarm);
				target.getFightAttribute().setCurHp(hp);
				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			}else if (isTerritory && territoryNoHarm) {
				// 领地站战场技能处理
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, 0);

				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			} else if (isTerritory && isTerritoryLd) {
				TerritoryPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TERRITORY_WAR);
				// 领地站战场技能处理
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, config.getShanghai());

				long hp = MathUtils.max(0, target.getFightAttribute().getCurHp() - config.getShanghai());
				target.getFightAttribute().setCurHp(hp);
				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			}else if (isHcZbs && hcZbsNoHarm) {
				//皇城站战场技能处理
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, 0);

				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			}
			else if (isHcZbs && hcZbsFlagRoleNoHarm) {
				//持旗人攻击不造成伤害
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, 0);

				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			}
			else if (isHcZbs && isHcZbsLd) {
				HcZBSPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_HCZBS_WAR);
				//皇城站战场技能处理
				harm = new Harm(skill, fighter, target, HarmType.PUTONG, config.getShanghai());

				long hp = MathUtils.max(0, target.getFightAttribute().getCurHp() - config.getShanghai());
				target.getFightAttribute().setCurHp(hp);
				target.getFightStatistic().addHarm(harm);

				if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
					target.deadHandle(harm);
				}
			}else if (isKfYunGong && isKfYunGongTuteng) { //跨服云宫之巅对图腾的固定伤害处理
                KuafuYunGongPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_KUAFU_YUNGONG);
                harm = new Harm(skill, fighter, target, HarmType.PUTONG, config.getShanghai());
                long hp = MathUtils.max(0, target.getFightAttribute().getCurHp() - config.getShanghai());
                target.getFightAttribute().setCurHp(hp);
                target.getFightStatistic().addHarm(harm);
                if (!target.getStateManager().isDead() && target.getFightAttribute().getCurHp() == 0) {
                    target.deadHandle(harm);
                }
            } else {
				// 直接伤害
				IFightResultCalculator fightResultCalculator = skill.getFightResultCalculator();
				IFightResult fightResult = fightResultCalculator.calculate(skill, fighter, target);

				fightResult.execute(target, fighter, skill);

				harm = fightResult.getHarm();
				// 增加仇恨排行榜，活动bosss使用
				if (ActivityBossManage.isBossMonster(target.getId())) {
					Long userRoleId = fighter.getId();
					if (!ElementType.isRole(fighter.getElementType())) {
						userRoleId = fighter.getOwner().getId();
					}
					activityBossExportService.modifyHurtValue(userRoleId, target.getId(), fightResult.getHarm().getVal(), target.getStateManager().isDead());
				}
			}

			if (null != harm) {
				harms.add(HarmPrintUtils.harmMsg(harm));

				/**
				 * 战斗结果业务
				 */
				fightResultHandle(fighter, target, harm);
				
			}

			target.getFightStatistic().flushChanges(msgWriter);

		}
		fighter.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());

		return harms;
	}

	
	/**
	 * 是否可以释放关联技能
	 * 
	 * @param role
	 * @param glSkillId
	 * @return true:可以
	 */
	private static boolean isGuanLianSkill(IRole role, String glSkillId) {
		return true;
	}

	/**
	 * 战斗结果业务
	 * 
	 * @param fighter
	 * @param target
	 * @param harm
	 */
	private static void fightResultHandle(IFighter fighter, IFighter target, IHarm harm) {

		// 跨服暂时不参与PK值处理
		if (KuafuConfigPropUtil.isKuafuServer()) {
			KfFightHundunResult.fightResultHandle(fighter, target, harm);
			return;
		}

		// 红名业务
		FightPkValueResult.pkValueResultHandle(fighter, target, harm);

		// 混沌战场业务
		// FightHundunResult.fightResultHandle(fighter, target, harm);

	}

	/**
	 * 目标场景保护(新人、等级差、夜间保护)
	 */
	private static boolean stageProtect(IStage stage, IFighter attacker, IFighter target, ISkill skill) {
		// 安全区可以用治疗技能
		if (skill.getDamageType().equals(FightResultCalculatorType.ZHILIAO)) {
			return false;
		}
		boolean isPetOrRole = ElementType.isRoleOrPet(attacker.getElementType());
		boolean tarIsPetOrRole = ElementType.isRoleOrPet(target.getElementType());

		if (isPetOrRole && tarIsPetOrRole) {
			// 如果都是人，判断场景是否可以PK
			if (!stage.isCanPk()) {
				return true;
			}
		}
		AoiStage aoiStage = null;
		if (stage.getStageType() == StageType.TERRITORY_WAR) {
			TerritoryStage tStage = (TerritoryStage) stage;
			aoiStage = tStage.getStage();
		} else {
			aoiStage = (AoiStage) stage;
		}

		PathNodeCopy pathNode = aoiStage.getPathInfo().getPathNode(target.getPosition().getX(), target.getPosition().getY(), PathNodeSize._1X);
		PathNodeCopy attackerPathNode = aoiStage.getPathInfo().getPathNode(attacker.getPosition().getX(), attacker.getPosition().getY(), PathNodeSize._1X);

		boolean isTargetSafe = null != pathNode && pathNode.isSafe();
		boolean isAttackerSafe = null != attackerPathNode && attackerPathNode.isSafe();

		if (isTargetSafe || isAttackerSafe) {
			if (isPetOrRole && tarIsPetOrRole) {
				return true;
			}
		}
		return false;
	}

	public static final AttackedAbleCheck attackedAblecheck = new AttackedAbleCheck();

	public static AttackedAbleCheck getAttackedAblecheck() {
		return attackedAblecheck;
	}

	/**
	 * @description 目标是否可攻击验证
	 * @author ShiJie Chi
	 * @date 2012-6-8 下午2:56:57
	 */
	public static class AttackedAbleCheck {

		/**
		 * 是否通过
		 * 
		 * @param skill
		 * @param attacker
		 * @param target
		 * @param stage
		 */
		public boolean check(ISkill skill, IFighter attacker, IFighter target, IStage stage) {
			if (null != target && !target.getStateManager().isDead() && !stageProtect(stage, attacker, target, skill) && isCanAttact(skill, target, attacker.getId())
					&& isBeiAttractTarget(target) && skill.getTarget().inRange(skill, attacker, target)) {
				return true;
			}
			return false;
		}

		/**
		 * 是否是可攻击的目标
		 * 
		 * @param target
		 * @param attackerId
		 * @return
		 */
		private boolean isCanAttact(ISkill skill, IStageElement target, Long attackerId) {

			if (skill.getDamageType().equals(FightResultCalculatorType.SHANGHAI)) {
				if (target.getId().equals(attackerId)) {
					return false;
				}
			} else {
				// SkillConfig skillConfig =
				// StageConfigureHelper.getSkillConfigExportService().loadById(skill.getId());
				// return !skillConfig.isIshostile();

				return true;
			}

			return true;
		}

	}

	/**
	 * @description 目标选取计数器
	 * @author ShiJie Chi
	 * @date 2012-7-2 下午3:26:57
	 */
	public static class TargetCounter {

		private int maxTarget;
		private int cur;

		public TargetCounter(int maxTarget) {
			this.maxTarget = maxTarget;
		}

		public void incr() {
			cur++;
		}

		public boolean max() {
			return cur >= maxTarget;
		}

	}

	/**
	 * @description 草稿
	 * @author ChiShiJie
	 * @date 2012-7-16 下午1:45:08
	 */
	public static class ConfirmDraft {

		Object draft = null;

		@SuppressWarnings("unchecked")
		public <T> T getDraft() {
			return (T) draft;
		}

		public void setDraft(Object draft) {
			this.draft = draft;
		}

	}

	/**
	 * 是否是可被攻击的怪物
	 * 
	 * @param target
	 * @return true:可被攻击
	 */
	private static boolean isBeiAttractTarget(IStageElement target) {

		if (ElementType.isMonster(target.getElementType())) {
			IMonster monster = (IMonster) target;

			if (monster.isNoBeiAttrack()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @description
	 * @author ChiShiJie
	 * @date 2012-7-16 下午2:20:11
	 */
	public static class SkillTargetFilter implements IElementSearchFilter {

		protected IStage stage;
		protected IFighter attacker;
		protected TargetCounter counter;
		protected ISkill skill;
		protected Object[] helpParams;
		protected ConfirmDraft draft;
		protected int scope;

		public SkillTargetFilter(IStage stage, IFighter attacker, ISkill skill, Object[] helpParams) {

			SkillConfig skilConfig = StageConfigureHelper.getSkillConfigExportService().loadById(skill.getCategory());
			this.counter = new TargetCounter(skilConfig.getMaxTarget());

			this.scope = skilConfig.getRange();

			this.draft = new ConfirmDraft();

			this.stage = stage;
			this.attacker = attacker;
			this.skill = skill;
			this.helpParams = helpParams;

		}

		@Override
		public boolean check(IStageElement target) {

			// 验证规则:
			// 1、战斗元素
			// 2、技能最大人数
			// 3、其他可攻击验证
			// 4、技能施法路径
			// 5、施法距离
			if (ElementType.isFighter(target.getElementType()) && !counter.max() && SkillProcessHelper.getAttackedAblecheck().check(skill, attacker, (IFighter) target, stage)
					&& skill.getSkillFirePath().inRange(skill, attacker, target, helpParams, stage, draft) && isBeiAttractTarget(target) && stage.inSight(attacker, target)
			// && stage.inScope(attacker.getPosition(), target.getPosition(),
			// scope, ScopeType.PIXEL)
			) {

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

}
