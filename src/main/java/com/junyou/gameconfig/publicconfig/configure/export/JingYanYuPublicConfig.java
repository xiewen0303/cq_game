package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * 装备鉴定配置
 *@author  LiNing
 *@created 2013-10-24上午6:22:26
 */
public class JingYanYuPublicConfig extends AdapterPublicConfig{
	
	private float beishu;//经验倍数
	
	private Integer needlevel;//开启等级

	public float getBeishu() {
		return beishu;
	}

	public void setBeishu(float beishu) {
		this.beishu = beishu;
	}

	public Integer getNeedlevel() {
		return needlevel;
	}

	public void setNeedlevel(Integer needlevel) {
		this.needlevel = needlevel;
	}

	
}
