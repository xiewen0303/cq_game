package com.junyou.stage.model.core.stage;

public interface IElementSearchFilter {
	/**
	 * 根据自定义验证规则，验证目标是否合法
	 * @param target
	 * @return
	 */
	public boolean check(IStageElement target);
	
	/**
	 * 搜寻人数是否已足够
	 * @return
	 */
	public boolean isEnough();
}
