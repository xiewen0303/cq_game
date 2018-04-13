package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * 副本相关
 * @author LiuYu
 * @date 2015-3-13 下午7:54:50
 */
public class FubenPublicConfig extends AdapterPublicConfig{
	/**一键扫荡所需VIP*/
	private int needVip;
	/**瞬秒所需VIP*/
	private int miaoVip;
	public int getNeedVip() {
		return needVip;
	}
	public void setNeedVip(int needVip) {
		this.needVip = needVip;
	}
	public int getMiaoVip() {
		return miaoVip;
	}
	public void setMiaoVip(int miaoVip) {
		this.miaoVip = miaoVip;
	}
	
	
}
