package com.junyou.stage.model.skill.buff;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.BuffConstant;
import com.junyou.stage.JunQinStateConstants;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.BuffConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;

public class BuffFactory {

	public static IBuff create(IFighter target, IFighter attacker,
			ISkill skill, String buffId) {
		
		IBuff buff = create(target, attacker, buffId);
		
		return buff;
	}
	
	public static IBuff create(IFighter target, IFighter attacker,String buffId,Integer duration){
		BuffConfig buffConfig = StageConfigureHelper.getBuffConfigExportService().loadById(buffId);
		if(buffConfig == null){
			ChuanQiLog.error("buff is null buffId:"+buffId);
			return null;
		}
		
		Long id = IdFactory.getInstance().generateNonPersistentId();
		
		Buff buff = new Buff(id,buffConfig.getEffectId(),buffConfig.getEffectLevel(),buffConfig.getEffectId(),buffConfig.getBaseHit());
		
		buff.setOwner(target);
		buff.setDuration(duration);
		buff.setAttacker(attacker);
		buff.setDeadClear(buffConfig.isDeadClear());
		buff.setOfflineClear(buffConfig.isOfflineClear());
		buff.setChangeClear(buffConfig.isChangeClear());
		buff.setMoveClear(buffConfig.isMoveClear());
		if(null != buffConfig.getEffectPro()){
			buff.setAttributes(buffConfig.getEffectPro());
		}
		buff.setSpecialEffect(buffConfig.getSpecialEffect());
		buff.setSpecialPeriod(buffConfig.getInterval());
		buff.setSpecialEffectValue(buffConfig.getSkill());
		
		buff.setBeiAttackAble(buffConfig.isBeiAttackAble());
		buff.setAttackAble(buffConfig.isAttackAble());
		buff.setMoveAble(buffConfig.isMoveAble());
		buff.setBeiAttackAble1(buffConfig.isBeiAttackAble1());
		buff.setConsumAble(buffConfig.isConsumAble());
		buff.setTeleportAble(buffConfig.isTeleportAble());
		buff.setRideAble(buffConfig.isRideAble());
		
		return buff;
	}
	
	public static IBuff create(IFighter target, IFighter attacker,String buffId) {
		
		BuffConfig buffConfig = StageConfigureHelper.getBuffConfigExportService().loadById(buffId);
		if(buffConfig == null){
			ChuanQiLog.error("buff is null buffId:"+buffId);
			return null;
		}
		if(buffConfig.isMonsterNomal()){
			if(ElementType.isMonster(target.getElementType())){
				Monster monster = (Monster)target;
				if(monster.getRank() > 0){
					return null;
				}
			}
		}
		
		return create(target, attacker, buffId,buffConfig.getLastTime());
	}
	
	/**
	 * 神武被动技能buff
	 * @author Yang Gao
	 * @param target 被攻击者,buff的拥有者
	 * @param attacker 攻击者,buff的触发者
	 * @param buffId 技能编号
	 * @param buffVal 技能效果值
	 * @param duration 技能持续时间
	 * @return  
	 */
	public static IBuff create(IFighter target, IFighter attacker,String buffId, Long buffVal,Integer duration){
     BuffConfig buffConfig = StageConfigureHelper.getBuffConfigExportService().loadById(buffId);
     if (buffConfig == null) {
         ChuanQiLog.error("buff is null buffId:" + buffId);
         return null;
     }
     Long id = IdFactory.getInstance().generateNonPersistentId();
     Buff buff = new Buff(id, buffConfig.getEffectId(), buffConfig.getEffectLevel(), buffConfig.getEffectId(), buffConfig.getBaseHit());

     // 新构造的属性效果集合
     Map<String, Long> effectMap = buffConfig.getEffectPro();
     if(!ObjectUtil.isEmpty(effectMap)){
         Map<String, Long> newEffectMap = new HashMap<String, Long>();
         for(String key : effectMap.keySet()){
             newEffectMap.put(key, buffVal);
         }
         buff.setAttributes(newEffectMap);
     }
     
     buff.setOwner(target);
     buff.setDuration(duration);
     buff.setAttacker(attacker);
     buff.setDeadClear(buffConfig.isDeadClear());
     buff.setOfflineClear(buffConfig.isOfflineClear());
     buff.setChangeClear(buffConfig.isChangeClear());
     buff.setMoveClear(buffConfig.isMoveClear());
     buff.setSpecialEffect(buffConfig.getSpecialEffect());
     buff.setSpecialPeriod(buffConfig.getInterval());
     buff.setSpecialEffectValue(buffConfig.getSkill());

     buff.setBeiAttackAble(buffConfig.isBeiAttackAble());
     buff.setAttackAble(buffConfig.isAttackAble());
     buff.setMoveAble(buffConfig.isMoveAble());
     buff.setBeiAttackAble1(buffConfig.isBeiAttackAble1());
     buff.setConsumAble(buffConfig.isConsumAble());
     buff.setTeleportAble(buffConfig.isTeleportAble());
     buff.setRideAble(buffConfig.isRideAble());

     return buff;
     
	}
	
	/**
     * 心魔斗场临时buff
     * @author Yang Gao
     * @param attacker 攻击者,buff的拥有者
     * @param buffId 技能编号
     * @param buffVal 技能效果属性值
     * @return  
     */
    public static IBuff create(IFighter attacker, String buffId, Map<String,Long> buffValMap){
     if(ObjectUtil.isEmpty(buffValMap)) return null;
     BuffConfig buffConfig = StageConfigureHelper.getBuffConfigExportService().loadById(buffId);
     if (buffConfig == null) {
         ChuanQiLog.error("buff is null buffId:" + buffId);
         return null;
     }
     Long id = IdFactory.getInstance().generateNonPersistentId();
     Buff buff = new Buff(id, buffConfig.getEffectId(), buffConfig.getEffectLevel(), buffConfig.getEffectId(), buffConfig.getBaseHit());

     // 新构造的属性效果集合
     Map<String, Long> effectMap = buffConfig.getEffectPro();
     if(!ObjectUtil.isEmpty(effectMap)){
         Map<String, Long> newEffectMap = new HashMap<String, Long>();
         for(String key : effectMap.keySet()){
             if(buffValMap.containsKey(key)){
                 newEffectMap.put(key, buffValMap.get(key));
             }
         }
         buff.setAttributes(newEffectMap);
//         ChuanQiLog.info("心魔斗场副本临时buff的属性值:{}", JSON.toJSONString(newEffectMap));
     }
     
     buff.setOwner(attacker);
     buff.setDuration(buffConfig.getLastTime());
     buff.setDeadClear(buffConfig.isDeadClear());
     buff.setOfflineClear(buffConfig.isOfflineClear());
     buff.setChangeClear(buffConfig.isChangeClear());
     buff.setMoveClear(buffConfig.isMoveClear());
     buff.setSpecialEffect(buffConfig.getSpecialEffect());
     buff.setSpecialPeriod(buffConfig.getInterval());
     buff.setSpecialEffectValue(buffConfig.getSkill());

     buff.setBeiAttackAble(buffConfig.isBeiAttackAble());
     buff.setAttackAble(buffConfig.isAttackAble());
     buff.setMoveAble(buffConfig.isMoveAble());
     buff.setBeiAttackAble1(buffConfig.isBeiAttackAble1());
     buff.setConsumAble(buffConfig.isConsumAble());
     buff.setTeleportAble(buffConfig.isTeleportAble());
     buff.setRideAble(buffConfig.isRideAble());

     return buff;
     
    }
	
	
	public static String getJunQinBuffId(IFighter target){
		return JunQinStateConstants.JUNQIN_BUFF_ID+"_"+target.getId();
	}

	public static IBuff createMpBuff(IFighter target){
		String mpBuffId = BuffConstant.RECOVER_MP_BUFF_ID;
		
		return create(target, target, mpBuffId);
	}
	
	/**
	 * 恢复buff
	 * 1、过期不予以恢复
	 * @param data [category,level,starttime,attackerId,additionalData]
	 * @param role
	 */
	public static IBuff recover(Object[] data, IFighter role) {
		
		String category = (String)data[0];
		long startTime = Long.valueOf((String)data[1]);
		Long attackerId = Long.valueOf((String)data[2]);
		Object additionalData = (String)data[3];
		
		long duration = GameSystemTime.getSystemMillTime() - startTime;
		
		BuffConfig buffConfig = StageConfigureHelper.getBuffConfigExportService().loadById(category);
		if(null == buffConfig){
			return null;
		}
		
		if(buffConfig.getLastTime() <= duration){
			return null;
		}
		
		Long id = IdFactory.getInstance().generateNonPersistentId();
		Buff buff = new Buff(id,buffConfig.getEffectId(),buffConfig.getEffectLevel(),buffConfig.getEffectId(),1f);
		
		buff.setOwner(role);
		buff.setDuration(buffConfig.getLastTime());
		buff.recoverStartTime(startTime);
		buff.setAttackerId(attackerId);
		buff.setAdditionalData(additionalData);
		buff.setDeadClear(buffConfig.isDeadClear());
		buff.setOfflineClear(buffConfig.isOfflineClear());
		buff.setChangeClear(buffConfig.isChangeClear());
		buff.setMoveClear(buffConfig.isMoveClear());
		if(null != buffConfig.getEffectPro()){
			buff.setAttributes(buffConfig.getEffectPro());
		}
		buff.setSpecialEffect(buffConfig.getSpecialEffect());
		buff.setSpecialPeriod(buffConfig.getInterval());
		buff.setSpecialEffectValue(buffConfig.getSkill());
		
		buff.setBeiAttackAble(buffConfig.isBeiAttackAble());
		buff.setAttackAble(buffConfig.isAttackAble());
		buff.setMoveAble(buffConfig.isMoveAble());
		buff.setBeiAttackAble1(buffConfig.isBeiAttackAble1());
		buff.setConsumAble(buffConfig.isConsumAble());
		buff.setTeleportAble(buffConfig.isTeleportAble());
		buff.setRideAble(buffConfig.isRideAble());
		
		return buff;
	}

}
