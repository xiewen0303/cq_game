package com.junyou.stage.model.stage.quartz;

import java.util.Timer;
import java.util.TimerTask;

public class TxSimpleScheduler {

	private Timer timer;
	
	private int seconds;
	
	public TxSimpleScheduler(int seconds) {
		timer = new Timer();
		this.seconds = seconds;
	}
	
	public TxSimpleScheduler() {
		timer = new Timer();
	}

	/**
	 * 启动1
	 */
	public void start(TimerTask timerTask){
		timer.schedule(timerTask, seconds * 1000);
	}
	
	/**
	 * 启动2
	 */
	public void start(TimerTask timerTask,int runSeconds){
		timer.schedule(timerTask, runSeconds * 1000);
	}
}
