package com.junyou.stage.model.stage.quartz;

import java.util.TimerTask;

/**
 * 清理怪任务
 *@author  DaoZheng Yuan
 *@created 2013-7-1下午3:09:54
 */
public class TxClearMonsterTimerTask extends TimerTask{

	private Object[] data;
	
	private String stageId;
	
	
	
	public TxClearMonsterTimerTask(Object[] data, String stageId) {
		super();
		this.data = data;
		this.stageId = stageId;
	}



	@Override
	public void run() {
//		StageMsgSender.send2StageInner(null, stageId, StageCommands.INNER_CLEAR_REFRESH_MONSTER, data);
	}
	
}
