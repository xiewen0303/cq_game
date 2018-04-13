package com.junyou.gameconfig.publicconfig.configure.export;


public class HpMpPublicConfig extends AdapterPublicConfig{

	
	//回复的血量
	private int hpValue;
	//回复血时间间隔(毫秒)
	private int hpTimeValue;
	
	//回复的蓝量
	private int mpValue;
	//回复蓝时间间隔(毫秒)
	private int mpTimeValue;
	
	//回复真元时间间隔(毫秒)
	private int zyTimeValue;
	
	public int getZyTimeValue() {
		return zyTimeValue;
	}

	public void setZyTimeValue(int zyTimeValue) {
		this.zyTimeValue = zyTimeValue;
	}

	public int getHpValue() {
		return hpValue;
	}

	public void setHpValue(int hpValue) {
		this.hpValue = hpValue;
	}

	public int getHpTimeValue() {
		return hpTimeValue;
	}

	public void setHpTimeValue(int hpTimeValue) {
		this.hpTimeValue = hpTimeValue;
	}

	public int getMpValue() {
		return mpValue;
	}

	public void setMpValue(int mpValue) {
		this.mpValue = mpValue;
	}

	public int getMpTimeValue() {
		return mpTimeValue;
	}

	public void setMpTimeValue(int mpTimeValue) {
		this.mpTimeValue = mpTimeValue;
	}


}
