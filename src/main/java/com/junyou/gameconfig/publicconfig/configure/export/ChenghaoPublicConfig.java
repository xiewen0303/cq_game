package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * 称号公共配置表
 * 
 * @author LiuYu
 * @date 2015-4-21 下午2:20:59
 */
public class ChenghaoPublicConfig extends AdapterPublicConfig {

	private int touxianvip;// 【称号】联系客服按钮开启vip等级
	private int toudingnum;// 人物头顶称号数量

	public int getTouxianvip() {
		return touxianvip;
	}

	public void setTouxianvip(int touxianvip) {
		this.touxianvip = touxianvip;
	}

	public int getToudingnum() {
		return toudingnum;
	}

	public void setToudingnum(int toudingnum) {
		this.toudingnum = toudingnum;
	}

}
