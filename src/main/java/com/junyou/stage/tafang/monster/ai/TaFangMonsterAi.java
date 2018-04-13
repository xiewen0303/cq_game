package com.junyou.stage.tafang.monster.ai;

import com.junyou.cmd.InnerCmdType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.element.monster.ai.DefaultAi;

/**
 * 塔防怪物
 * @author LiuYu
 * @date 2015-6-19 下午2:23:39
 */
public class TaFangMonsterAi extends DefaultAi {
	
	protected IFighter fighter;
	
	public TaFangMonsterAi(IFighter fighter) {
		super(fighter);
	}

	/**
	 * 获取ai处理指令
	 */
	protected Short getAiHandleCommand() {
		return InnerCmdType.AI_TAFANG_MONSTER;
	}
}
