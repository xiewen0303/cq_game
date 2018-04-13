package com.junyou.stage.model.core.attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.stage.model.core.element.IFighter;

public class BaseFightAttribute extends AbsFightAttribute {
	
	private Map<BaseAttributeType,IBaseAttribute> baseAttributeMap = new HashMap<BaseAttributeType, IBaseAttribute>();
	
	private IBaseAttribute effectAttribute;
	
	private IFighter fighter;
	
	public BaseFightAttribute(IFighter fighter) {
		this.fighter = fighter;
	}
	
	private IBaseAttribute getAttribute(BaseAttributeType type,boolean init){
		
		IBaseAttribute baseAttribute = baseAttributeMap.get(type);
		if(init && null == baseAttribute){
			
			baseAttribute = new EffectBaseAttribute();
			
			if(null == effectAttribute){
				effectAttribute = baseAttribute;
			}else{
				effectAttribute = new BaseAttributeComponent(effectAttribute, baseAttribute);
			}
			
			baseAttributeMap.put(type, baseAttribute);
		}
		
		return baseAttribute;
	}
	
	public void setBaseAttribute(BaseAttributeType type,Map<String,Long> valMap){
		
		clearBaseAttribute(type,false);
	
		IBaseAttribute baseAttribute = getAttribute(type,true);
		
		for(Entry<String,Long> entry : valMap.entrySet()){
			AttributeCalculateHelper.setAttribute(entry.getKey(), entry.getValue(), baseAttribute);
			//改变属性
			addChangedBaseAttribute(entry.getKey());
		}
		refresh();
		
	}
	
	public void initBaseAttribute(BaseAttributeType type,Map<String,Long> valMap){
		
		clearBaseAttribute(type,false);
		
		IBaseAttribute baseAttribute = getAttribute(type,true);
		
		for(Entry<String,Long> entry : valMap.entrySet()){
			AttributeCalculateHelper.setAttribute(entry.getKey(), entry.getValue(), baseAttribute);
			//改变属性
			addChangedBaseAttribute(entry.getKey());
		}
		
	}
	
	public void incrBaseAttribute(BaseAttributeType type,Map<String,Long> valMap){
		
		IBaseAttribute baseAttribute = getAttribute(type,true);
		
		for(Entry<String,Long> entry : valMap.entrySet()){
			AttributeCalculateHelper.increaseAttribute(entry.getKey(), entry.getValue(), baseAttribute);
			//改变属性
			addChangedBaseAttribute(entry.getKey());
		}
		refresh();
		
	}
	
	public void descBaseAttribute(BaseAttributeType type,Map<String,Long> valMap){
		
		IBaseAttribute baseAttribute = getAttribute(type,false);
		
		if(baseAttribute != null){
			
			for(Entry<String,Long> entry : valMap.entrySet()){
				AttributeCalculateHelper.decreaseAttribute(entry.getKey(), entry.getValue(), baseAttribute);
				//改变属性
				addChangedBaseAttribute(entry.getKey());
			}
			refresh();
		}
		
	}
	
	public void clearBaseAttribute(BaseAttributeType type,boolean refreshOrNot){
		
		IBaseAttribute baseAttribute = getAttribute(type,false);
		
		if(null != baseAttribute){
			Map<String, Long> valMap =  baseAttribute.toMap();
			if(null != valMap && valMap.size() > 0){
				
				for(Entry<String,Long> entry : valMap.entrySet()){
					
					if(entry.getKey() != null){
						AttributeCalculateHelper.setAttribute(entry.getKey(), 0, baseAttribute);
						//改变属性
						addChangedBaseAttribute(entry.getKey());
					}
					
				}
				
				if(refreshOrNot){
					refresh();
				}
			}
		}
	}

	@Override
	public long getEffectAttribute(String effectType) {
		return (null != effectAttribute) ? effectAttribute.get(effectType) : 0;
	}

	@Override
	protected IFighter getFighter() {
		return fighter;
	}

	@Override
	public IBaseAttribute getBaseAttributeMap(BaseAttributeType type) {
		return baseAttributeMap.get(type);
	}

}
