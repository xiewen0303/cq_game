package com.junyou.stage.model.element.monster.ai;

import com.junyou.cmd.InnerCmdType;
import com.junyou.stage.model.core.element.IFighter;

/**
 * 仙宫巡逻
 * @author LiuYu
 * @date 2015-6-19 下午2:23:39
 */
public class XianGongXunLuoAi extends DefaultAi {
	
	protected IFighter fighter;
	
	public XianGongXunLuoAi(IFighter fighter) {
		super(fighter);
	}

	/**
	 * 获取ai处理指令
	 */
	protected Short getAiHandleCommand() {
		return InnerCmdType.AI_XIANGONG_XUNLUO;
	}
}
