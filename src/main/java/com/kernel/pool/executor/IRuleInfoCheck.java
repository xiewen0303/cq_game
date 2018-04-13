package com.kernel.pool.executor;

/**
 * @description 用来判定某一id的{@link RouteInfo}是否可以被清除
 * @author hehj
 * @date 2012-3-29 上午10:40:33 
 */
public interface IRuleInfoCheck {

	public Boolean valid(Object ruleinfo);
	
}
