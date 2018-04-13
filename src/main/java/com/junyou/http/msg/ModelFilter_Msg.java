package com.junyou.http.msg;


/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午12:12:57
 *@Description: 对模块操作的数据返回
 */
public class ModelFilter_Msg extends HttpCallBackMsg {
	private boolean flag = true;
	private String reslults;
	
	public String getReslults() {
		return reslults;
	}

	public void setReslults(String reslults) {
		this.reslults = reslults;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
