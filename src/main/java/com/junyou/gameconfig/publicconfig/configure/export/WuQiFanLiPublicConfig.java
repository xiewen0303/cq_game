package com.junyou.gameconfig.publicconfig.configure.export;



/**
 * 御剑返利配置
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-13 下午5:51:55
 */
public class WuQiFanLiPublicConfig extends AdapterPublicConfig{
	
	private int kfTime;//开服几天内可以领取
	
	private int flid;//御剑ID（ID达到可以领取）
	
	private String flitem;//道具

	public int getKfTime() {
		return kfTime;
	}

	public void setKfTime(int kfTime) {
		this.kfTime = kfTime;
	}

	public int getFlid() {
		return flid;
	}

	public void setFlid(int flid) {
		this.flid = flid;
	}

	public String getFlitem() {
		return flitem;
	}

	public void setFlitem(String flitem) {
		this.flitem = flitem;
	}
	
	
	
	
}
