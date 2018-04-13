/**
 * 
 */
package com.junyou.stage.model.element.role;

import java.util.List;
import java.util.Set;

import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.wuqi.manage.WuQi;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.stage.entity.TeamMember;
import com.junyou.stage.model.attribute.role.IRoleFightAttribute;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.prop.PropModel;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.SkillFactory;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-16下午1:43:11
 */
public interface IRole extends IFighter {
	
	public void setFightStatistic(IFightStatistic fightStatistic);
	
	public void setRoleFightAttribute(IRoleFightAttribute roleFightAttribute);
	
	public SkillFactory getSkillFactory();
	
	public void setSkillFactory(SkillFactory skillFactory);
	
	public void setBuffManager(IBuffManager buffManager);
	
	public void setCamp(String camp);

	public BusinessData getBusinessData();
	
	public void setBusinessData(BusinessData data);

	public TeamMember getTeamMember();
	
	public void setTeamMember(TeamMember member);
	
	public Pet getPet();
	
	public boolean isRedRole();
	
	public void setPet(Pet pet);
	
	public void setZuoQi(ZuoQi zuoQi);
	
	public ZuoQi getZuoQi();
	
	public int getZuoQiLevel();
	
	public PropModel getPropModel();

	public void setPropModel(PropModel propModel);
	
	public void setBiaoche(Biaoche biaoche);

	public boolean isYinshen();	
	
	public void setYinshen(int yinshen);
	
	public boolean isGm();
	
	public void setIsGm(int gm);
	
	public Integer getZyCamp();
	
	/**
	 * 开启回血定时
	 * @param delay
	 */
	public void startHpSchedule(int delay);
	
	/**
	 * 开启领地战加经验定时器
	 * @param delay
	 */
	public void startTerritoryAddExpSchedule(int delay);
	/**
	 * 取消领地战加经验定时器
	 */
	public void cancelTerritoryAddExpSchedule();
	
	/**
	 * 取消皇城争霸赛加经验定时器
	 */
	public void cancelHcZhengBaSaiAddExpSchedule();
	
	/**
	 *开启领地战加经验定时器
	 */
	public void startHcZhengBaSaiAddExpSchedule(int delay);
	
    /**
     * 取消跨服云宫之巅加经验加真气的定时器
     */
    public void cancelKfYunGongAddExpZqSchedule();

    /**
     * 开启跨服云宫之巅加经验加真气定时器
     */
    public void startKfYunGongAddExpZqSchedule(int delay);
//	
//	/**
//	 * 开启土城加高经验
//	 * @param tuChenExpPublicConfig
//	 * @param serverOpenDay
//	 */
//	public void startTuChenSchedule(TuChenExpPublicConfig tuChenExpPublicConfig);
//	
//	
//	/**
//	 * 死亡土城高经验处理
//	 * @param tuChenExpPublicConfig
//	 * @param map
//	 */
//	public void deadTuChenExp(TuChenExpPublicConfig tuChenExpPublicConfig,Map<Long,String> map);
//	
//	/**
//	 * 复活土城高经验处理
//	 * @param tuChenExpPublicConfig
//	 * @param map
//	 */
//	public void resetTuChenExp(TuChenExpPublicConfig tuChenExpPublicConfig,Map<Long,String> map);
	
	/**
	 * 是否在切换地图
	 * @return
	 */
	public boolean isChanging();
	
	/**
	 * 切换地图状态变更
	 */
	public void setChanging(boolean change);
	
	public Biaoche getBiaoche();
	
	/**
	 * 获取其他人查看时的数据
	 * @return
	 */
	public Object[] getOtherData(Object[] equips);
	
	public void startCollect(Long collectId,long finishTime);
	
	/**
	 * 获取当前采集id（如果未完成采集则返回null)
	 * @return
	 */
	public Long getCollectId();
	
	/**
	 * 清除采集状态
	 */
	public void clearCollect();
	
	/**
	 * 获取当前怒气值
	 * @return
	 */
	public int getNuqi();
	/**
	 * 设置怒气（带通知）
	 * @param nuqi
	 */
	public void setNuqiNotice(int nuqi);
	/**
	 * 设置怒气（无通知）
	 * @param nuqi
	 */
	public void setNuqi(int nuqi);
	
	/**
	 * 在线时间增加怒气
	 * @param first	是否是开启的那一次执行
	 */
	public void startNuqiSchedule(boolean first);
	
	/**
	 * 设置cdManager
	 * @param cdManager
	 */
	public void setCdManager(PublicCdManager cdManager);
	
	/**
	 * 怒气技能
	 * @param skillId
	 */
	public void nuqiSkill(String skillId);
	
	/**
	 * 取消怒气技能
	 * @param skillId
	 */
	public void stopNuqiSkill(String skillId);
	
	/**
	 * 是否是恶人
	 * @return
	 */
	public boolean isWicked();
	
	/**
	 * 开启死亡强制复活定时
	 */
	public void startBackFuHuoSchedule();
	
	/**
	 * 取消死亡强制复活定时
	 */
	public void cancelBackFuhuoSchedule();
	/**
	 * 获取队伍id[在阵营战中，返回值为阵营id]
	 * @return
	 */
	public Integer getTeamId();
	
	/**
	 * 获取糖宝仙剑id
	 * @return
	 */
	public Integer getXianJianShowId();
	/**
	 * 获取糖宝战甲id
	 * @return
	 */
	public Integer getZhanJiaShowId();
	/**
	 * 获取糖宝翅膀id
	 * @return
	 */
	public Integer getTianYuShowId();
	/**
	 * 获取同模数据
	 * @return
	 */
	public Object getSameMsgData(Short cmd);
	
	/**
	 * 开启阵营战加经验定时
	 */
	public void startCampAddExpSchedule(long delay);
	
	/**
	 * 取消阵营战加经验定时
	 */
	public void cancelCampAddExpSchedule();
	/**
	 * 校验并启动神器攻击定时器
	 */
	public void checkShenqiAttackSchedule();
	/**
	 * 取消神器攻击定时
	 */
	public void cancelShenqiAttackSchedule();
	
	
	/**
	 * 开启探宝经验定时
	 */
	public void tanBaoExpSchedule(int delay);
	
	/**
	 * 开启温泉加经验定时器
	 * @param delay
	 */
	public void startWenquanAddExpZhenqiSchedule();
	
	/**
	 * 开启魔宫猎焰加经验真气定时器
	 * @param delay
	 */
	public void startMglyAddExpZhenqiSchedule(int delay);
	
	/**
     * 取消魔宫猎焰加经验真气定时器
     * @param delay
     */
    public void cancelMglyAddExpZhenqiSchedule();
    
    /**
     * 开启魔宫猎焰减少御魔值定时器
     * @param delay
     */
    public void startMglyCutYumoValSchedule(int delay);
    
    /**
     * 取消魔宫猎焰减少御魔值定时器
     * @param delay
     */
    public void cancelMglyCutYumoValSchedule();
    
    /**
     * 开启魔宫猎焰延迟退出场景定时器
     * @param delay
     */
    public void startMglyDelayExitStageSchedule(int delay);
    
    /**
     * 取消魔宫猎焰延迟退出场景定时器
     * @param delay
     */
    public void cancelMglyDelayExitStageSchedule();
    
	/**
	 * 开启称号过期定时器
	 * @param chenghaoId
	 * @param delay
	 */
	public void startChenghaoExpireSchedule(Integer chenghaoId,Long delay);
	/**
	 * 获取是否妖神状态
	 * @return
	 */
	public boolean isYaoshen(); 
	/**
	 * 切换妖神
	 * @param isYaoshen
	 */
	public void setYaoshen(boolean isYaoshen) ;
	/**
	 * 当前妖神魔纹阶数
	 * @return
	 */
	public int yaoshenMowenJie(); 
	    
	/**
	 * 获取当前时装
	 * @return
	 */
	public Integer getShiZhuang();
	/**
	 * 设置当前时装
	 * @return
	 */
	public void setShiZhuang(Integer shizhuangId);
	/**
	 * 设置当前配偶
	 * @return
	 */
	public void setPeiou(String peiou);
	public String getPeiou();
	/**
	 * 设置当前信物
	 * @return
	 */
	public void setXinwu(Integer xinwu);
	public Integer getXinwu();
	
	public Set<String> getNoticeSkills();
	public void setNoticeSkills(Set<String> noticeSkills);
	
	public void addSkillShulian(RoleSkill roleSkill);
	public void addShulian(String skillId);
	public List<RoleSkill> getRoleSkills();
	
	
	public void setWuQi(WuQi zuoQi);
	
	public WuQi getWuQi();
	public int getWuQiLevel();
	
//	/**
//	 * 从副本出来，重置之前的战斗模式
//	 */
//	public void resetBattleMode();
	
//	public void setOldBattleMode(BattleMode oldBattleMode);
//	
//	/**
//	 * 从副本出来，重置之前的战斗模式
//	 */
//	public BattleMode getOldBattleMode();
}
