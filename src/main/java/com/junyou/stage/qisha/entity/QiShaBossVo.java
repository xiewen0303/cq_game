package com.junyou.stage.qisha.entity;

import com.junyou.constants.GameConstants;

/**
 * @author LiuYu
 * 2015-8-20 下午3:29:25
 */
public class QiShaBossVo {
	private String monsterId;
	private int state;
	private Object[] data;
	public QiShaBossVo(String monsterId){
		this.monsterId = monsterId;
		state = GameConstants.BOSS_STATE_NOREFRESH;
		data = new Object[]{monsterId,state};
	}
	public String getMonsterId() {
		return monsterId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
		data[1] = state;
	}
	public Object[] getBossData(){
		return data;
	}
	
}
