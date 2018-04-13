package com.junyou.gameconfig.dazuo.configure.export;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 打坐经验表 
 *
 * @author wind
 * @date 2015-03-20 11:21:39
 */
public class DaZuoConfig extends AbsVersion {

	private Integer level;

	private Integer sxexp;

	private Integer skill1exp;

	private Integer sxzhenqi;

	private Integer zhenqi;


	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSxexp() {
		return sxexp;
	}

	public void setSxexp(Integer sxexp) {
		this.sxexp = sxexp;
	}
	public Integer getSkill1exp() {
		return skill1exp;
	}

	public void setSkill1exp(Integer skill1exp) {
		this.skill1exp = skill1exp;
	}
	public Integer getSxzhenqi() {
		return sxzhenqi;
	}

	public void setSxzhenqi(Integer sxzhenqi) {
		this.sxzhenqi = sxzhenqi;
	}
	public Integer getZhenqi() {
		return zhenqi;
	}

	public void setZhenqi(Integer zhenqi) {
		this.zhenqi = zhenqi;
	}   
}
