package com.junyou.stage.model.stage.quartz;

import java.util.TimerTask;

/**
 * 跨服任务定时器
 * @author DaoZheng Yuan
 * 2014-11-3 下午4:06:23
 */
public class KuafuTimerTask extends TimerTask{

	private String command;
	
	public KuafuTimerTask(String cmd) {
		super();
		this.command = cmd;
	}



	@Override
	public void run() {
//		StageMsgSender.send2StageInner(null, null, command, null);
	}

}
