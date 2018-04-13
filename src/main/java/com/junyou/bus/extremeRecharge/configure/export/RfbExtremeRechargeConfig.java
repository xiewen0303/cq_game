package com.junyou.bus.extremeRecharge.configure.export;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 
 * @description:至尊充值配置 
 *
 *	@author ChuBin
 *
 * @date 2016-11-24
 */
public class RfbExtremeRechargeConfig {

	// 所需元宝
	private Integer yb;

	// 模型
	private String ani;

	// 奖励道具
	private String item;
	
	private Map<String, GoodsConfigureVo> jiangItem ;

	// 配置文件加密信息
	private String md5Version;

	// 活动底图
	private String res;
	
	//战斗力
	private Long zlpus;
	
	public Map<String, GoodsConfigureVo> getJiangItem() {
		return jiangItem;
	}

	public void setJiangItem(Map<String, GoodsConfigureVo> jiangItem) {
		this.jiangItem = jiangItem;
	}

	public Long getZlpus() {
		return zlpus;
	}

	public void setZlpus(Long zlpus) {
		this.zlpus = zlpus;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Integer getYb() {
		return yb;
	}

	public void setYb(Integer yb) {
		this.yb = yb;
	}

	public String getAni() {
		return ani;
	}

	public void setAni(String ani) {
		this.ani = ani;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
}
