package com.junyou.stage.model.fight.calculator;

/**
 * 
 * @description 输出的类型
 *
 * @author LiuJuan
 * @created 2011-12-14 下午04:17:31
 */
public enum FightResultCalculatorType {
	
	SHANGHAI(0),ZHILIAO(1),WUSHANGHAI(2);
	
	private FightResultCalculatorType(int val) {
		this.val = val;
	}
	
	private int val;
	
	public int getVal() {
		return val;
	}

	public static FightResultCalculatorType convert(Integer damageType) {
		if(damageType.equals(0)){
			//伤害公式
			return SHANGHAI;
		}else if(damageType.equals(1)){
			//治疗公式
			return ZHILIAO;
		}else if(damageType.equals(2)){
			//无伤害公式
			return WUSHANGHAI;
		}
		
		return null;
	}
	
	
}
