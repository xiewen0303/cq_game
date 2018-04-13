package com.junyou.bus.role.utils;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;

/**
 * @author LiuYu
 * 2015-6-2 上午11:05:16
 */
public class TangbaoUtil {
	public static Map<Integer,Long> tangbaoAttributeOutput(IFightAttribute tangbao,Map<String,Long> eat){
		Map<Integer,Long> map = new HashMap<>();
		/**
		 * 	0	最大生命值
			1	攻击
			2	防御
			3	闪避等级
			4	命中等级
			5	暴击等级
			6	韧性等级
			7	移动速度
			-1	为人物增加的生命值
			-2	为人物增加的攻击
			-3	为人物增加的防御
		 */
		map.put(0, tangbao.getMaxHp());
		map.put(1, tangbao.getAttack());
		map.put(2, tangbao.getDefense());
		map.put(3, tangbao.getShanBi());
		map.put(4, tangbao.getMingZhong());
		map.put(5, tangbao.getBaoJi());
		map.put(6, tangbao.getKangBao());
		map.put(7, tangbao.getSpeed());
		map.put(-1, eat.get(EffectType.x2.name()));
		map.put(-2, eat.get(EffectType.x5.name()));
		map.put(-3, eat.get(EffectType.x8.name()));
		return map;
	}
	
	public static Map<Integer,Long> changeAttributeOutput(Map<String,Long> eat){
		Map<Integer,Long> map = new HashMap<>();
		map.put(-1, eat.get(EffectType.x2.name()));
		map.put(-2, eat.get(EffectType.x5.name()));
		map.put(-3, eat.get(EffectType.x8.name()));
		return map;
	}
}
