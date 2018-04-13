package com.junyou.stage.model.fight.statistic;

import java.util.Map;

import com.junyou.stage.model.core.fight.IFightStatistic;

/**
 * @description 怪物统计
 * @author ShiJie Chi
 * @date 2012-3-30 上午10:07:16 
 */
public interface IMonsterFightStatistic extends IFightStatistic {

	/**
	 * 获取伤害统计
	 * @return (key:roleId,val:harm)
	 */
	public Map<Long,Integer> pullHarmStatistic();
	
}
