package com.junyou.stage.model.element.monster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.shenmo.configure.ShenMoScoreConfig;
import com.junyou.bus.tanbao.entity.XianGongXunLuoMonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.attribute.monster.MonsterFightAttribute;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.state.StateManager;
import com.junyou.stage.model.element.componentlistener.FightListener;
import com.junyou.stage.model.element.componentlistener.FubenMonsterStateListener;
import com.junyou.stage.model.element.componentlistener.KuafuBossFightListener;
import com.junyou.stage.model.element.componentlistener.QiShaBossFightListener;
import com.junyou.stage.model.element.componentlistener.TutengMonsterStateListener;
import com.junyou.stage.model.element.monster.ai.DefaultAi;
import com.junyou.stage.model.element.monster.ai.XianGongXunLuoAi;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.fight.statistic.MonsterFightStatistic;
import com.junyou.stage.model.hatred.MonsterHatredManager2;
import com.junyou.stage.model.hatred.MonsterHatredRule;
import com.junyou.stage.model.hatred.TaFangMonsterHatredManager;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.skill.buff.BuffManager;
import com.junyou.stage.shenmo.entity.ShenMoShuiJing;
import com.junyou.stage.tafang.monster.ai.TaFangMonsterAi;
import com.junyou.stage.tafang.monster.ai.TaFangNPCAi;
import com.junyou.stage.tafang.stage.TaFangMonster;
import com.junyou.stage.tafang.stage.TaFangNpc;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @description 怪物工厂
 *
 * @author LiuJuan
 * @created 2011-12-15 上午10:49:17
 */
public class MonsterFactory {
	
	
	/**
	 * 创建普通场景怪物
	 */
	public static IMonster create(String teamId,Long id, MonsterConfig monsterConfig){
		
		Monster monster = new Monster(id,teamId,monsterConfig);
		return createHandle(monster, monsterConfig);
	
	}
	
	/**
	 * 创建七杀场景怪物
	 */
	public static Monster createQiSha(String teamId,Long id, MonsterConfig monsterConfig){
		
		QiShaMonster monster = new QiShaMonster(id, teamId, monsterConfig);
		createHandle(monster, monsterConfig);
		
		QiShaBossFightListener listener = new QiShaBossFightListener(monster);
		monster.getFightAttribute().addListener(listener);
		
		return monster;
		
	}
	
	/**
	 * 创建跨服boss
	 */
	public static KuafuBossMonster createKuafuBoss(String teamId,Long id, MonsterConfig monsterConfig){
		
		KuafuBossMonster monster = new KuafuBossMonster(id, teamId, monsterConfig);
		createHandle(monster, monsterConfig);
		
		KuafuBossFightListener listener = new KuafuBossFightListener(monster);
		monster.getFightAttribute().addListener(listener);
		
		return monster;
		
	}
	
	/**
	 * 创建普通定时怪物
	 */
	public static IMonster createDingShiMonster(Long id, MonsterConfig monsterConfig){
		
		FubenMonster fubenMonster = new FubenMonster(id,monsterConfig);
		IMonster monster =  createHandle(fubenMonster, monsterConfig);
		return monster;
	}	
	
	/**
	 * 创建副本怪物
	 */
	public static IMonster createFubenMonster(Long id, MonsterConfig monsterConfig){
		
		FubenMonster fubenMonster = new FubenMonster(id,monsterConfig);
		IMonster monster =  createHandle(fubenMonster, monsterConfig);
		monster.getStateManager().addListener(new FubenMonsterStateListener(monster));
		return monster;
	}	
	
	/**
	 * 创建仙宫怪物
	 */
	public static IMonster createXianGongMonster(Long id, XianGongXunLuoMonsterConfig config,MonsterConfig monsterConfig){
		
		XianGongXunLuoMonster fubenMonster = new XianGongXunLuoMonster(id,monsterConfig,config.getLujing());
		IMonster monster =  createHandleNoAi(fubenMonster, monsterConfig);
		monster.setAi(new XianGongXunLuoAi(monster));
		return monster;
	}	
	
	/**
	 * 创建塔防怪物
	 */
	public static TaFangMonster createTaFangMonster(Long id,MonsterConfig monsterConfig){
		
		TaFangMonster fubenMonster = new TaFangMonster(id, monsterConfig);
		IMonster monster =  createHandleNoAi(fubenMonster, monsterConfig);
		monster.setHatredManger(new TaFangMonsterHatredManager(monster,new MonsterHatredRule(monster)));
		monster.setAi(new TaFangMonsterAi(monster));
		return (TaFangMonster)monster;
	}	
	
	/**
	 * 创建塔防NPC
	 */
	public static TaFangNpc createTaFangNpc(Long id,MonsterConfig monsterConfig){
		
		TaFangNpc npc = new TaFangNpc(id, monsterConfig);
		createHandleNoAi(npc, monsterConfig);
		npc.setAi(new TaFangNPCAi(npc));
		return npc;
	}	
	
	/**
	 * 创建图腾怪物
	 * @param id	怪物id
	 * @param monsterConfig	怪物配置
	 * @param guildId	怪物公会id
	 * @param cmd	死亡触发指令
	 * @return
	 */
	public static IMonster createTuTengMonster(Long id, MonsterConfig monsterConfig,long guildId,short cmd){
		
		IMonster monster =  createHandle(new TutengMonster(id,monsterConfig,guildId), monsterConfig);
		monster.getStateManager().addListener(new TutengMonsterStateListener(monster,cmd));
		return monster;
	}
	
	/**
	 * 创建神魔水晶
	 * @param id
	 * @param monsterConfig
	 * @param config
	 * @return
	 */
	public static ShenMoShuiJing createShenMoShuiJing(Long id, MonsterConfig monsterConfig,ShenMoScoreConfig config){
		ShenMoShuiJing shuijing = new ShenMoShuiJing(id, monsterConfig, config);
		createHandleNoAi(shuijing, monsterConfig);
		return shuijing;
	}
	
		
	/**
	 * 跨服混战场怪物
	 * @param id
	 * @param teamId
	 * @param monsterConfig
	 * @return
	 */
	public static IMonster createKfHunDunMonseter(String id,String teamId,MonsterConfig monsterConfig,int jfValue){
//		MonsterHd monsterHd = new MonsterHd(id,teamId,monsterConfig);
//		monsterHd.setJfValue(jfValue);
//		
//		IMonster monster =  createHandle(monsterHd, monsterConfig);
//		monster.getStateManager().addListener(new KfHunDunMonsterStateListener(monster));
//		return monster;
		
		return null;
	}
	
	public static IMonster createHandle(Monster monster, MonsterConfig monsterConfig) {
		createHandleNoAi(monster, monsterConfig);
		
		if(!monsterConfig.isNoAttrack() || !monsterConfig.isNoMove()){//如果既不能攻击也不能移动，则不设置AI
			//ai
			monster.setAi(new DefaultAi(monster));
		}
		
		return monster;
	}
	
	public static IMonster createMultiAttrsHandle(Monster monster, MonsterConfig monsterConfig, List<String> attrTypes, Float multiplier) {
	    createHandleNoAi(monster, monsterConfig, attrTypes, multiplier);
        
        if(!monsterConfig.isNoAttrack() || !monsterConfig.isNoMove()){//如果既不能攻击也不能移动，则不设置AI
            //ai
            monster.setAi(new DefaultAi(monster));
        }
        
        return monster;
	}
	
	public static IMonster createHandleNoAi(Monster monster, MonsterConfig monsterConfig) {
		monster.setLevel(monsterConfig.getLevel());
		monster.setMonsterId(monsterConfig.getId());
		monster.setCamp(monsterConfig.getCamp());
		
		/**
		 * 装配
		 * */
		
		//状态相关
		monster.setStateManager(new StateManager());
		//战斗变化统计
		MonsterFightStatistic monsterFightStatistic = new MonsterFightStatistic(monster);
		monster.setFightStatistic(monsterFightStatistic);
		
		//属性管理器
		MonsterFightAttribute monsterFightAttribute = new MonsterFightAttribute(monster);
		monster.setMonsterFightAttribute(monsterFightAttribute);
		
		//buff管理器
		monster.setBuffManager(new BuffManager());
		//仇恨管理器
		monster.setHatredManger(new MonsterHatredManager2(monster,new MonsterHatredRule(monster)));
		
		/**
		 * 填充
		 * */
		//填充属性
		monsterFightAttribute.setBaseAttribute(BaseAttributeType.LEVEL,monsterConfig.getAttribute());
		
		//战斗模式填充
		monster.setBattleMode(BattleMode.MONSTER_NORMAL);
		
		//填充血量
		monster.getFightAttribute().resetHp();
		
		
		//组建绑定监听器
		FightListener fightListener = new FightListener(monster.getFightStatistic());
		monster.getFightAttribute().addListener(fightListener);
		monster.getBuffManager().addListener(fightListener);
		monster.getStateManager().addListener(fightListener);
		
		//技能
		List<String> skills = monsterConfig.getSkillList();
		if(null != skills){
			for(String skillId : skills){
				ISkill skill = SkillManager.getManager().getSkill(skillId);
				monster.addSkill(skill);
			}
		}
		
		return monster;
	}
	
	public static IMonster createHandleNoAi(Monster monster, MonsterConfig monsterConfig,List<String> attrTypes, Float multiplier) {
        monster.setLevel(monsterConfig.getLevel());
        monster.setMonsterId(monsterConfig.getId());
        monster.setCamp(monsterConfig.getCamp());
        
        /**
         * 装配
         * */
        
        //状态相关
        monster.setStateManager(new StateManager());
        //战斗变化统计
        MonsterFightStatistic monsterFightStatistic = new MonsterFightStatistic(monster);
        monster.setFightStatistic(monsterFightStatistic);
        
        //属性管理器
        MonsterFightAttribute monsterFightAttribute = new MonsterFightAttribute(monster);
        monster.setMonsterFightAttribute(monsterFightAttribute);
        
        //buff管理器
        monster.setBuffManager(new BuffManager());
        //仇恨管理器
        monster.setHatredManger(new MonsterHatredManager2(monster,new MonsterHatredRule(monster)));
        
        /**
         * 填充
         * */
        Map<String, Long> baseAttributeMap = new HashMap<String, Long>(monsterConfig.getAttribute());
        if (null != multiplier && !ObjectUtil.isEmpty(attrTypes)) {
            for (Entry<String, Long> entry : monsterConfig.getAttribute().entrySet()) {
                if (attrTypes.contains(entry.getKey())) {
                    baseAttributeMap.put(entry.getKey(), (long) (entry.getValue() * multiplier));
                }
            }
        }
        //填充属性
        monsterFightAttribute.setBaseAttribute(BaseAttributeType.LEVEL, baseAttributeMap);
        
        //战斗模式填充
        monster.setBattleMode(BattleMode.MONSTER_NORMAL);
        
        //填充血量
        monster.getFightAttribute().resetHp();
        
        
        //组建绑定监听器
        FightListener fightListener = new FightListener(monster.getFightStatistic());
        monster.getFightAttribute().addListener(fightListener);
        monster.getBuffManager().addListener(fightListener);
        monster.getStateManager().addListener(fightListener);
        
        //技能
        List<String> skills = monsterConfig.getSkillList();
        if(null != skills){
            for(String skillId : skills){
                ISkill skill = SkillManager.getManager().getSkill(skillId);
                monster.addSkill(skill);
            }
        }
        
        return monster;
    }
	
    /**
     * 创建五行副本怪物
     */
    public static IMonster createWuxingFubenMonster(String teamId,long id, MonsterConfig monsterConfig, ReadOnlyMap<String, Long> wxAttsMap, boolean isRetrieve) {
        WuxingFubenMonster wxFubenMonster = new WuxingFubenMonster(id, teamId, monsterConfig, isRetrieve);
        wxFubenMonster.setLevel(monsterConfig.getLevel());
        wxFubenMonster.setMonsterId(monsterConfig.getId());
        wxFubenMonster.setCamp(monsterConfig.getCamp());

        // 状态相关
        wxFubenMonster.setStateManager(new StateManager());
        // 战斗变化统计
        MonsterFightStatistic monsterFightStatistic = new MonsterFightStatistic(wxFubenMonster);
        wxFubenMonster.setFightStatistic(monsterFightStatistic);

        // 属性管理器
        MonsterFightAttribute monsterFightAttribute = new MonsterFightAttribute(wxFubenMonster);
        wxFubenMonster.setMonsterFightAttribute(monsterFightAttribute);

        // buff管理器
        wxFubenMonster.setBuffManager(new BuffManager());
        // 仇恨管理器
        wxFubenMonster.setHatredManger(new MonsterHatredManager2(wxFubenMonster, new MonsterHatredRule(wxFubenMonster)));

        // 填充等级属性
        wxFubenMonster.getFightAttribute().initBaseAttribute(BaseAttributeType.LEVEL, monsterConfig.getAttribute());

        // 填充五行属性
        if(null != wxAttsMap){
            wxFubenMonster.getFightAttribute().initBaseAttribute(BaseAttributeType.WUXING, wxAttsMap);
        }

        // 战斗模式填充
        wxFubenMonster.setBattleMode(BattleMode.MONSTER_NORMAL);

        // 计算怪物最终属性
        wxFubenMonster.getFightAttribute().refresh();

        // 填充血量
        wxFubenMonster.getFightAttribute().resetHp();

        // 组建绑定监听器
        FightListener fightListener = new FightListener(wxFubenMonster.getFightStatistic());
        wxFubenMonster.getFightAttribute().addListener(fightListener);
        wxFubenMonster.getBuffManager().addListener(fightListener);
        wxFubenMonster.getStateManager().addListener(fightListener);

        // 技能
        List<String> skills = monsterConfig.getSkillList();
        if (null != skills) {
            for (String skillId : skills) {
                ISkill skill = SkillManager.getManager().getSkill(skillId);
                wxFubenMonster.addSkill(skill);
            }
        }

        if (!monsterConfig.isNoAttrack() || !monsterConfig.isNoMove()) {// 如果既不能攻击也不能移动，则不设置AI
            wxFubenMonster.setAi(new DefaultAi(wxFubenMonster));
        }

        wxFubenMonster.getStateManager().addListener(new FubenMonsterStateListener(wxFubenMonster));

        return wxFubenMonster;
    }
	
    /**
     * 创建扩大基础属性倍数的副本怪物 
     * @param id
     * @param monsterConfig
     * @param multi 扩大倍数
     * @return
     */
    public static IMonster createMultiFubenMonster(Long id, MonsterConfig monsterConfig, List<String> attrTypes, Float multi){
        FubenMonster fubenMonster = new FubenMonster(id,monsterConfig);
        IMonster monster =  createMultiAttrsHandle(fubenMonster, monsterConfig, attrTypes, multi);
        monster.getStateManager().addListener(new FubenMonsterStateListener(monster));
        return monster;
    }

}
