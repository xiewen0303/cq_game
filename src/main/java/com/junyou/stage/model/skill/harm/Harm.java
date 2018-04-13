/**
 * 
 */
package com.junyou.stage.model.skill.harm;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;

/**
 * @description 伤害
 * @author ShiJie Chi
 * @created 2011-12-9上午11:00:53
 */
public class Harm implements IHarm {

	private HarmType harmType;
	private long val;
	
	private IFighter attacker;
	private IFighter target;
	private ISkill skill;
	
	private Integer harmTypeValue;
	
	//扣除的魔法值
	private Integer kouMpValue;
	//扣除的真元值
	private Integer kouZyValue;
	//扣除的血量值
	private Integer kouHpValue;
	//吸血值
	private Integer xiHpValue;
	//抵挡值
    private long ddVal;
    //反伤值
    private long fsVal;
	
	
	public Harm(ISkill skill,IFighter attacker,IFighter target,HarmType harmType,long val) {
		this.skill = skill;
		this.harmType = harmType;
		this.val = val;
		this.attacker = attacker;
		this.target = target;
	}
	
    public Harm(ISkill skill, IFighter attacker, IFighter target, HarmType harmType, long val, long ddVal, long fsVal) {
        this.skill = skill;
        this.harmType = harmType;
        this.val = val;
        this.attacker = attacker;
        this.target = target;
        this.ddVal = ddVal;
        this.fsVal = fsVal;
    }
	
	public Integer getKouHpValue() {
		return kouHpValue == null ? 0 : kouHpValue;
	}

	public void setKouHpValue(Integer kouHpValue) {
		this.kouHpValue = kouHpValue;
	}

	public Integer getXiHpValue() {
		return xiHpValue;
	}

	public void setXiHpValue(Integer xiHpValue) {
		this.xiHpValue = xiHpValue;
	}

	public Integer getKouMpValue() {
		return kouMpValue;
	}

	public void setKouMpValue(Integer kouMpValue) {
		this.kouMpValue = kouMpValue;
	}

	public Integer getKouZyValue() {
		return kouZyValue;
	}

	public void setKouZyValue(Integer kouZyValue) {
		this.kouZyValue = kouZyValue;
	}

	public Integer getHarmTypeValue() {
		return harmTypeValue;
	}

	public void setHarmTypeValue(Integer harmTypeValue) {
		this.harmTypeValue = harmTypeValue;
	}

	/**
	 * @param
	 */
	public HarmType getType(){
		return harmType;
	}

	/**
	 * @param
	 */
	public long getVal(){
		return val;
	}

	@Override
	public IFighter getAttacker() {
		return attacker;
	}

	@Override
	public IFighter getTarget() {
		return target;
	}

	@Override
	public ISkill getSkill() {
		return skill;
	}

    public long getDdVal() {
        return ddVal;
    }

    public long getFsVal() {
        return fsVal;
    }
	
}