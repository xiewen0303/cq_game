package com.junyou.http.msg;


/**
 * 请求服务器数据回调信息[一般性返回就用这个消息回调对象]
 * @author DaoZheng Yuan
 * 2014年12月22日 上午11:05:31
 */
public class ServerData_Msg extends HttpCallBackMsg {
	
	private int type;
	
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
