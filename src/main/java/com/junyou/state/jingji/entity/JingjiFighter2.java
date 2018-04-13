package com.junyou.state.jingji.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.attribute.role.RoleFightAttribute;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.AbsFighter;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.model.core.state.StateManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.utils.lottery.Lottery;

public class JingjiFighter2 extends AbsFighter implements IJingjiFighter{
	public JingjiFighter2(Long userRoleId, String name,int configId) {
		super(userRoleId, name);
		this.configId = configId;
	}
	private JingjiAttribute2 jingjiattribute;
	private RoleFightAttribute roleFightAttribute;
	private JingjiCdManager jingjiCdManager;
	private List<ISkill> canUseSkill = new ArrayList<>();
	private JingjiFight jingjiFight;
	private StateManager stateManager = new StateManager();
	private int configId;
	private int zuoqi;
	private int chibang;
	private int level;
	
	public void setJingjiattribute(JingjiAttribute2 jingjiattribute){
		this.jingjiattribute = jingjiattribute;
		roleFightAttribute = new RoleFightAttribute(this);
		calAttribute();
	}
	
	public void setJingjiFight(JingjiFight jingjiFight){
		this.jingjiFight = jingjiFight;
		jingjiCdManager = new JingjiCdManager(jingjiFight);
		getFightAttribute().resetHp();
	}
	/**
	 * 获取攻击技能
	 * @return
	 */
	public ISkill getSkill(){
		ISkill skill = null;
		for (ISkill iSkill : getSkills()) {
			if(!iSkill.isCding(jingjiCdManager)){
				canUseSkill.add(iSkill);
			}
		}
		if(canUseSkill.size() > 0){
			int index = Lottery.roll(canUseSkill.size());
			skill = canUseSkill.get(index);
			canUseSkill.clear();
			skill.toCd(this);
		}
		return skill;
	}
	/**
	 * 获取最近CD
	 * @return
	 */
	public int getMinCd(){
		int minCd = 1000;
		for (ISkill iSkill : getSkills()) {
			if(iSkill.isCding(jingjiCdManager)){
				int cd = iSkill.getDynamicCd(jingjiCdManager);
				if(cd < minCd){
					minCd = cd;
				}
			}
		}
		return minCd;
	}
	
	/**
	 * 重新计算战斗属性
	 */
	public void calAttribute(){
		//设置属性
		for (Entry<Integer, Map<String, Long>> entry : jingjiattribute.getAttribute().entrySet()) {
			BaseAttributeType type = BaseAttributeType.getBaseAttributeTypeByVal(entry.getKey());
			getFightAttribute().initBaseAttribute(type,entry.getValue());
		}
		if(jingjiattribute.getBuff() > 100){
			Map<String,Long> buff = new HashMap<>();
			buff.put(EffectType.x42.name(), jingjiattribute.getBuff() * 100l);
			getFightAttribute().initBaseAttribute(BaseAttributeType.BUFF, buff);
		}
		
		getFightAttribute().refresh();
		
		//设置技能
		for (String skillId : jingjiattribute.getSkills()) {
			ISkill skill = SkillManager.getManager().getSkill(skillId);
			if(skill != null){
				this.addSkill(skill);
			}else{
				ChuanQiLog.error("技能不存在，技能id:" + skillId);
			}
		}
	}
	
	public long getZpuls(){
		return getFightAttribute().getZhanLi();
	}
	
	public long getCurHp(){
		return getFightAttribute().getCurHp();
	}
	
	@Override
	public Integer getCamp() {
		return null;
	}
	@Override
	public IFightStatistic getFightStatistic() {
		return null;
	}
	@Override
	public IFightAttribute getFightAttribute() {
		return roleFightAttribute;
	}
	@Override
	public IBuffManager getBuffManager() {
		return null;
	}
	@Override
	public void deadHandle(IHarm harm) {
		jingjiFight.setEnd();
	}
	@Override
	public IHatredManager getHatredManager() {
		return null;
	}
	@Override
	public PublicCdManager getPublicCdManager() {
		return jingjiCdManager;
	}
	@Override
	public Object getMoveData() {
		return null;
	}
	@Override
	public IStateManager getStateManager() {
		return stateManager;
	}
	@Override
	public Object getStageData() {
		return null;
	}
	@Override
	public PathNodeSize getPathNodeSize() {
		return null;
	}
	@Override
	public ElementType getElementType() {
		return null;
	}
	@Override
	public short getEnterCommand() {
		return 0;
	}
	@Override
	public PointTakeupType getTakeupType() {
		return null;
	}
	@Override
	public Object getMsgData() {
		return null;
	}
	@Override
	public void leaveStageHandle(IStage stage) {
	}
	@Override
	public void enterStageHandle(IStage stage) {
	}

	@Override
	public IRole getOwner() {
		return null;
	}

	@Override
	public int getConfigId() {
		return configId;
	}

	public int getZuoqi() {
		return zuoqi;
	}

	public void setZuoqi(int zuoqi) {
		this.zuoqi = zuoqi;
	}

	public int getChibang() {
		return chibang;
	}

	public void setChibang(int chibang) {
		this.chibang = chibang;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	
}
