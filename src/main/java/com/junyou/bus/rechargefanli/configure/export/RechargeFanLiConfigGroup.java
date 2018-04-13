package com.junyou.bus.rechargefanli.configure.export;


/**
 * 
 * @description 七日开服活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class RechargeFanLiConfigGroup {
	
	private String des;//活动描述
	private String pic;//背景图
	private Integer minGold;//最少充值多少元宝可领取
	private Float goldRatio;//比率
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Integer getMinGold() {
		return minGold;
	}
	public void setMinGold(Integer minGold) {
		this.minGold = minGold;
	}
	public Float getGoldRatio() {
		return goldRatio;
	}
	public void setGoldRatio(Float goldRatio) {
		this.goldRatio = goldRatio;
	}

	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	
}
