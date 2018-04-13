package com.junyou.stage.model.fight.zhanli;

import java.util.Map;

import com.junyou.gameconfig.constants.AttributeType;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.ZhuanDouLiCanShuConfig;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.IFighter;

/**
 * 战力计算公式
 *@author  DaoZheng Yuan
 *@created 2012-10-29下午5:17:13
 */
public class ZhanLiCalculateHelper {

	public static Integer calculateZhuanLi(IFighter fighter) {
		
		ZhuanDouLiCanShuConfig zhanLiConfig = StageConfigureHelper.getZhuanDouLiCanShuConfigExportService().load();
		Map<String, Float> map = zhanLiConfig.getAttrXs();

		IFightAttribute fightAttribute = fighter.getFightAttribute();
		
		//总值
		Float reuslt = 0f;
		
		//增加maxHP上限数值（战斗属性）	maxhp
		Float maxHpValue = map.get(AttributeType.MaxHpIndex) * fightAttribute.getMaxHp();
		reuslt += maxHpValue;
		
		/*
		//最大物理攻击
		Float maxWuGong = map.get(EffectType.X12) * fightAttribute.getMaxWuGong();
		reuslt += maxWuGong;
		
		//最小物理攻击
		Float minWuGong = map.get(EffectType.X11) * fightAttribute.getMinWuGong();
		reuslt += minWuGong;
		
		//最大魔法攻击
		Float maxMoGong = map.get(EffectType.X14) * fightAttribute.getMaxMoGong();
		reuslt += maxMoGong;
		
		//最小魔法攻击
		Float minMoGong = map.get(EffectType.X13) * fightAttribute.getMinMoGong();
		reuslt += minMoGong;
		
		//最大道术攻击
		Float maxDaoGong = map.get(EffectType.X16) * fightAttribute.getMaxDaoGong();
		reuslt += maxDaoGong;
		
		//最小道术攻击
		Float minDaoGong = map.get(EffectType.X15) * fightAttribute.getMinDaoGong();
		reuslt += minDaoGong;
		
		//最大物理防御
		Float maxWuFang = map.get(EffectType.X22) * fightAttribute.getMaxWuFang();
		reuslt += maxWuFang;
		
		//最小物理防御
		Float minWuFang = map.get(EffectType.X21) * fightAttribute.getMinWuFang();
		reuslt += minWuFang;
		
		//最大魔法防御
		Float maxMoFang = map.get(EffectType.X24) * fightAttribute.getMaxMoFang();
		reuslt += maxMoFang;
		
		//最小魔法防御
		Float minMoFang = map.get(EffectType.X23) * fightAttribute.getMinMoFang();
		reuslt += minMoFang;

		//闪避几率
		Float shanBiValue = map.get(EffectType.X32) * fightAttribute.getShanBi();
		reuslt += shanBiValue;
		
		//命中几率
		Float mingZhongValue = map.get(EffectType.X31) * fightAttribute.getMingZhong();
		reuslt += mingZhongValue;
		
		//暴击几率
		Float baoJi = map.get(EffectType.X51) * fightAttribute.getBaoJi();
		reuslt += baoJi;
		
		//暴击伤害
		Float baoJi1 = map.get(EffectType.X53) * fightAttribute.getBaoJi1();
		reuslt += baoJi1;
		
		//幸运值
		Float xingYun = map.get(EffectType.X41) * fightAttribute.getXingYun();
		reuslt += xingYun;
		
		//移动
		Float speed = map.get(EffectType.X201) * fightAttribute.getSpeed();
		reuslt += speed;
		
		//暴击倍率
		Float baoji2 = getAttributeVal(map, EffectType.X52) * fightAttribute.getBaoJi2();
		reuslt += baoji2;
		
		//战力(不直接做面板显示，用于战力系数的计算)
		Float zhanLi = map.get(EffectType.ZhanLi) * fightAttribute.getZhanLi();
		reuslt += zhanLi;
		*/
		//处理
		Integer finalValue = (int) Math.ceil(reuslt*1d);
		
		return finalValue;
	}
}
