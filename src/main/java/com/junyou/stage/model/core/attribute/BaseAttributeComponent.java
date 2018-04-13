package com.junyou.stage.model.core.attribute;

import java.util.Map;


public class BaseAttributeComponent implements IBaseAttribute {
	
	private IBaseAttribute attribute;
	private IBaseAttribute attribute2;
	
	public BaseAttributeComponent(IBaseAttribute attribute,IBaseAttribute attribute2) {
		this.attribute = attribute;
		this.attribute2 = attribute2;
	}

	@Override
	public long get(String attributeType) {
		return attribute.get(attributeType) + attribute2.get(attributeType);
	}

	@Override
	public void set(String attributeType, long val) {
		throw new UnsupportedOperationException("BaseAttributeComponent can't support set method!!");
	}

	@Override
	public Map<String, Long> toMap() {
		throw new UnsupportedOperationException("BaseAttributeComponent can't support set method!!");
	}
	

}
