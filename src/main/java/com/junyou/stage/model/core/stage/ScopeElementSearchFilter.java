package com.junyou.stage.model.core.stage;

public class ScopeElementSearchFilter implements IElementSearchFilter {

	private IStageElement from;
	
	private int scope;
	
	private ScopeType scopeType;
	
	public ScopeElementSearchFilter(IStageElement from, int scope, ScopeType scopeType) {
		this.from = from;
		this.scope = scope;
		this.scopeType = scopeType;
	}

	@Override
	public boolean check(IStageElement target) {
		return from.getStage().inScope(from.getPosition(), target.getPosition(), scope, scopeType);
	}

	@Override
	public boolean isEnough() {
		return false;
	}

}
