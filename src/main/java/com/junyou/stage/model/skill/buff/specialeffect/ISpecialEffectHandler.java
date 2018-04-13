package com.junyou.stage.model.skill.buff.specialeffect;

import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.skill.buff.Buff;

/**
 * @description buff特殊效果处理器
 * @author ShiJie Chi
 * @date 2012-3-6 下午3:03:57 
 */
public interface ISpecialEffectHandler {
	
	public void triggerHandle(IBuff buff);

	public void overHandle(Buff buff);

}
