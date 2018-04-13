/**
 * 
 */
package com.junyou.stage.model.fight.calculator;

import java.util.HashMap;
import java.util.Map;

import com.junyou.stage.model.core.fight.IFightResultCalculator;

/**
 * @description 战斗结果计算
 * @author ShiJie Chi
 * @created 2011-12-14下午1:55:10
 */
public class FightResultCalculatorFactory {

	private static Map<FightResultCalculatorType, IFightResultCalculator> fightResultCalculators = new HashMap<FightResultCalculatorType, IFightResultCalculator>();
	
	static {
		fightResultCalculators.put(FightResultCalculatorType.SHANGHAI, new ShanghaiResultCalculator());
		fightResultCalculators.put(FightResultCalculatorType.WUSHANGHAI, new WuShanghaiResultCalculator());
		fightResultCalculators.put(FightResultCalculatorType.ZHILIAO, new ZhiLiaoResultCalculator());
	}
	
	/**
	 * 获取战斗结果计算规则
	 * @param fightResultType
	 * @return IFightResultCalculator
	 */
	public static IFightResultCalculator getFightResultCalculator(FightResultCalculatorType fightResultType) {
		return fightResultCalculators.get(fightResultType);
	}
	
}
