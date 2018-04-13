package com.junyou.http.msg;

/**
 * http停机服务返回消息
 * @author DaoZheng Yuan
 * 2014年12月16日 下午4:40:14
 */
public class HttpStopServer_Msg extends HttpCallBackMsg{

	//停机状态
	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
