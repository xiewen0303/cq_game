package com.junyou.stage.model.skill.buff.specialeffect;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 
 * @author ShiJie Chi
 * @date 2012-3-6 下午3:06:26 
 */
public class SpecialEffectHandlerFactory {
	
	private static Map<String,ISpecialEffectHandler> handlers = new HashMap<String, ISpecialEffectHandler>();
	
	
	public void setHandlers(Map<String, ISpecialEffectHandler> handlers) {
		SpecialEffectHandlerFactory.handlers = handlers;
	}

	/**
	 * 
	 */
	public static ISpecialEffectHandler getHandler(String effectType) {
		
		ISpecialEffectHandler handler = null;
		if(null != handlers){
			handler = handlers.get(effectType);
		}
		
		if(null == handler){
			throw new NullPointerException("no relate special effect handler :" + effectType);
		}
		
		return handler;
	}
	
}
