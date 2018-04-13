package com.junyou.stage.model.stage.quartz;

import java.util.TimerTask;

/**
 * 刷怪任务
 *@author  DaoZheng Yuan
 *@created 2013-7-1下午3:09:54
 */
public class TxRefreshMonsterTimerTask extends TimerTask{

	private Object[] data;
	
	private String stageId;
	
	
	
	public TxRefreshMonsterTimerTask(Object[] data, String stageId) {
		super();
		this.data = data;
		this.stageId = stageId;
	}



	@Override
	public void run() {
//		StageMsgSender.send2StageInner(null, stageId, StageCommands.INNER_REFRESH_MONSTER, data);
	}

	
	
}
