package com.junyou.stage.ruleinfo;

import com.junyou.stage.model.stage.StageManager;
import com.kernel.pool.executor.IRuleInfoCheck;

public class StageRuleInfoCheck implements IRuleInfoCheck {

	@Override
	public Boolean valid(Object ruleinfo) {
		return !StageManager.exist((String) ruleinfo);
	}

}
