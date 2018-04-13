package com.junyou.stage.model.element.goods;

import com.junyou.cmd.ClientCmdType;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;

/**
 * 
 *@Description 跨服云宫之巅活动旗子
 *@Author Yang Gao
 *@Since 2016-10-8
 *@Version 1.1.0
 */
public class KuafuYunGongQizi extends Collect {


	public KuafuYunGongQizi(Long id, String teamId, ZiYuanConfig ziYuanConfig) {
		super(id, teamId, ziYuanConfig);
	}

	public void scheduleDisappearHandle() {
		super.scheduleDisappearHandle();
	}

	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_TERRITORY_FLAG;
	}


}
