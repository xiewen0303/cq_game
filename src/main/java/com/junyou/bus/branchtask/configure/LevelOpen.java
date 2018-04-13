package com.junyou.bus.branchtask.configure;

public class LevelOpen implements IOpenCondition {
	private int needLevel;
	
	
	public LevelOpen(int needLevel) {
		super();
		this.needLevel = needLevel;
	}


	@Override
	public boolean isOpen(Object params) {
		int level = (int)params;
		return level >= needLevel;
	}


	@Override
	public boolean isType(int conditionType) {
		return conditionType == ConditionType.HERO_LEVEL;
	}
}
