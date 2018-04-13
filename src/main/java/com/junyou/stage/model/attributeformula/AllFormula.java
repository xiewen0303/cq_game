package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 全属性增幅
 * @author LiuYu
 * @date 2015-3-19 上午10:49:23
 */
public class AllFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//攻击
		long x4 = destination.getEffectAttribute(EffectType.x4.name());
		long x5 = destination.getEffectAttribute(EffectType.x5.name());
		long x6 = destination.getEffectAttribute(EffectType.x6.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		long att = x4*(EffectType.getAttBase()+x6)/EffectType.getAttBase()+x5;
		att = att*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setAttack(att < 0 ? 0 : att);
		
		//防御
		long x7 = destination.getEffectAttribute(EffectType.x7.name());
		long x8 = destination.getEffectAttribute(EffectType.x8.name());
		long def = x7 + x8;
		def = def*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setDefense(def < 0 ? 0 : def);
		
		//血
		long x1 = destination.getEffectAttribute(EffectType.x1.name());
		long x2 = destination.getEffectAttribute(EffectType.x2.name());
		long x3 = destination.getEffectAttribute(EffectType.x3.name());
		
		long maxHp = x1 * (EffectType.getAttBase() + x3)/EffectType.getAttBase() + x2;
		if (maxHp < 1) {
			maxHp = 1l;
		}
		maxHp = maxHp*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		
		destination.setMaxHp(maxHp);
		
		long curHp = destination.getCurHp();
		if(curHp > maxHp){
			curHp = maxHp;
		}
		
		destination.setCurHp(curHp);	
		
		//命中
		long x13 = destination.getEffectAttribute(EffectType.x13.name());
		long x14 = destination.getEffectAttribute(EffectType.x14.name());
		long mingzhong = x13 + x14;
		mingzhong = mingzhong*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setMingZhong(mingzhong < 0 ? 0 : mingzhong);
		
		//闪避
		long x10 = destination.getEffectAttribute(EffectType.x10.name());
		long x11 = destination.getEffectAttribute(EffectType.x11.name());
		long shanbi = x10+x11;
		shanbi = shanbi*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setShanBi(shanbi < 0 ? 0 : shanbi);
		
		//暴击
		long x15 = destination.getEffectAttribute(EffectType.x15.name());
		long x16 = destination.getEffectAttribute(EffectType.x16.name());
		long baoji = x15 + x16;
		baoji = baoji*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setBaoJi(baoji < 0 ? 0 : baoji);
		
		//韧性
		long x17 = destination.getEffectAttribute(EffectType.x17.name());
		long x18 = destination.getEffectAttribute(EffectType.x18.name());
		long renxing = x17 + x18;
		renxing = renxing*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setKangBao(renxing < 0 ? 0 : renxing);
		
		//战力
		long zhanLi = destination.getEffectAttribute(EffectType.zplus.name());
		zhanLi = zhanLi*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setZhanLi(zhanLi < 0 ? 0 : zhanLi);
	}
}
