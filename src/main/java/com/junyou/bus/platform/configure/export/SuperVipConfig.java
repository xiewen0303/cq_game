package com.junyou.bus.platform.configure.export;


/**
 * lxn
 * 超级会员面板常亮信息
 * @description  
 */
public class SuperVipConfig  {

	private int allRecharge; //累计充值
	private int onceRecharge; //一次性充值
	private String pic; //图片地址
	private Long qq; //qq号码
	
	public Long getQq() {
		return qq;
	}
	public void setQq(Long qq) {
		this.qq = qq;
	}
	public int getAllRecharge() {
		return allRecharge;
	}

	public void setAllRecharge(int allRecharge) {
		this.allRecharge = allRecharge;
	}

	public int getOnceRecharge() {
		return onceRecharge;
	}

	public void setOnceRecharge(int onceRecharge) {
		this.onceRecharge = onceRecharge;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
}
