/**
 * 
 */
package com.junyou.stage.model.core.attributeformula;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.attributeformula.AllFormula;
import com.junyou.stage.model.attributeformula.AttackFormula;
import com.junyou.stage.model.attributeformula.BaoJi2Formula;
import com.junyou.stage.model.attributeformula.BaoJiFormula;
import com.junyou.stage.model.attributeformula.ChengFaFormula;
import com.junyou.stage.model.attributeformula.DefenseFormula;
import com.junyou.stage.model.attributeformula.DiDangFormula;
import com.junyou.stage.model.attributeformula.FanshangFormula;
import com.junyou.stage.model.attributeformula.JianShangFormula;
import com.junyou.stage.model.attributeformula.KangBaoFormula;
import com.junyou.stage.model.attributeformula.MaxHpFormula;
import com.junyou.stage.model.attributeformula.MingZhongFormula;
import com.junyou.stage.model.attributeformula.ShanBiFormula;
import com.junyou.stage.model.attributeformula.ShangAddFormula;
import com.junyou.stage.model.attributeformula.ShangDefFormula;
import com.junyou.stage.model.attributeformula.SpeedFormula;
import com.junyou.stage.model.attributeformula.WuXingFormula;
import com.junyou.stage.model.attributeformula.ZengShangFormula;
import com.junyou.stage.model.attributeformula.ZhanLiFormula;

/**
 * @description 属性公式搜索
 * @author ShiJie Chi
 * @created 2011-12-2下午4:17:40
 */
public class AttributeFormulaSearch {
	
	private static Map<String,IAttributeFormula> formulas = new HashMap<String, IAttributeFormula>();
	
	static{
		
		//外部注册为佳
		//TODO
		MaxHpFormula maxHpFormula = new MaxHpFormula();
		AttackFormula attackFormula = new AttackFormula();
		DefenseFormula defenseFormula = new DefenseFormula();
		ShanBiFormula shanBiFormula = new ShanBiFormula();
		MingZhongFormula mingZhongFormula = new MingZhongFormula();
		BaoJiFormula baoJiFormula = new BaoJiFormula();
		BaoJi2Formula baoJi2Formula = new BaoJi2Formula();
		KangBaoFormula kangBaoFormula = new KangBaoFormula();
		SpeedFormula speedFormula = new SpeedFormula();
		AllFormula allFormula = new AllFormula();
		JianShangFormula jianShangFormula = new JianShangFormula();
		ShangAddFormula shangAddFormula = new ShangAddFormula();
		ShangDefFormula shangDefFormula = new ShangDefFormula();
		ZengShangFormula zengShangFormula = new ZengShangFormula();
		ZhanLiFormula zhanLiFormula = new ZhanLiFormula();
		ChengFaFormula chengFaFormula = new ChengFaFormula();
		//		------------------------------神武装备----------------------------//
		DiDangFormula diDangFormula = new DiDangFormula();
		FanshangFormula fanshangFormula = new FanshangFormula();
		
		//		------------------------------神武装备----------------------------//
		WuXingFormula wuXingFormula = new WuXingFormula();
		
		
		formulas.put(EffectType.x1.name(), maxHpFormula);
		formulas.put(EffectType.x2.name(), maxHpFormula);
		formulas.put(EffectType.x3.name(), maxHpFormula);
		
		formulas.put(EffectType.x4.name(), attackFormula);
		formulas.put(EffectType.x5.name(), attackFormula);
		formulas.put(EffectType.x6.name(), attackFormula);
		formulas.put(EffectType.x49.name(), attackFormula);
		
		formulas.put(EffectType.x7.name(), defenseFormula);
		formulas.put(EffectType.x8.name(), defenseFormula);
		formulas.put(EffectType.x43.name(), defenseFormula);
		formulas.put(EffectType.x50.name(), defenseFormula);
		
		formulas.put(EffectType.x10.name(), shanBiFormula);
		formulas.put(EffectType.x11.name(), shanBiFormula);
		formulas.put(EffectType.x45.name(), shanBiFormula);
		
		formulas.put(EffectType.x13.name(), mingZhongFormula);
		formulas.put(EffectType.x14.name(), mingZhongFormula);
		formulas.put(EffectType.x46.name(), mingZhongFormula);
		
		formulas.put(EffectType.x15.name(), baoJiFormula);
		formulas.put(EffectType.x16.name(), baoJiFormula);
		formulas.put(EffectType.x44.name(), baoJiFormula);
		
		formulas.put(EffectType.x28.name(), baoJi2Formula);
		
		formulas.put(EffectType.x17.name(), kangBaoFormula);
		formulas.put(EffectType.x18.name(), kangBaoFormula);
		formulas.put(EffectType.x47.name(), kangBaoFormula);
		
		formulas.put(EffectType.x19.name(), speedFormula);
		formulas.put(EffectType.x20.name(), speedFormula);
		formulas.put(EffectType.x51.name(), speedFormula);
		formulas.put(EffectType.x80.name(), speedFormula);
		
		formulas.put(EffectType.x24.name(), shangAddFormula);
		formulas.put(EffectType.x25.name(), shangDefFormula);
		formulas.put(EffectType.x26.name(), zengShangFormula);
		formulas.put(EffectType.x27.name(), jianShangFormula);
		
		formulas.put(EffectType.zplus.name(), zhanLiFormula);
		
		formulas.put(EffectType.x42.name(), allFormula);
		formulas.put(EffectType.x48.name(), chengFaFormula);
		
		formulas.put(EffectType.x58.name(), diDangFormula);
		formulas.put(EffectType.x59.name(), diDangFormula);
		formulas.put(EffectType.x60.name(), diDangFormula);
		
		formulas.put(EffectType.x61.name(), fanshangFormula);
		formulas.put(EffectType.x62.name(), fanshangFormula);
		formulas.put(EffectType.x63.name(), fanshangFormula);
		formulas.put(EffectType.x74.name(), fanshangFormula);
		
		formulas.put(EffectType.x64.name(), wuXingFormula);
		formulas.put(EffectType.x65.name(), wuXingFormula);
		formulas.put(EffectType.x66.name(), wuXingFormula);
		formulas.put(EffectType.x67.name(), wuXingFormula);
		formulas.put(EffectType.x68.name(), wuXingFormula);
		formulas.put(EffectType.x69.name(), wuXingFormula);
		formulas.put(EffectType.x70.name(), wuXingFormula);
		formulas.put(EffectType.x71.name(), wuXingFormula);
		formulas.put(EffectType.x72.name(), wuXingFormula);
		formulas.put(EffectType.x73.name(), wuXingFormula);
		
	}
	
	/**
	 * 寻找属性关联的公式
	 * @param attributeType {@link EffectType}
	 */
	public static IAttributeFormula findReference(String attributeType){
		return formulas.get(attributeType);
	}

}
