package com.junyou.http.msg;

/**
 * 测试  停机次数
 * @author DaoZheng Yuan
 * 2014年11月25日 下午5:14:54
 */
public class StopTimes_Msg extends HttpCallBackMsg {

	private int stopTimes;

	public int getStopTimes() {
		return stopTimes;
	}

	public void setStopTimes(int stopTimes) {
		this.stopTimes = stopTimes;
	}
	
}
