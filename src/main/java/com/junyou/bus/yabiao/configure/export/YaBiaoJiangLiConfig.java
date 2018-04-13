package com.junyou.bus.yabiao.configure.export;

/**
 * @description 押镖奖励配置 
 *
 * @author LiNing
 * @date 2015-03-13 17:05:04
 */
public class YaBiaoJiangLiConfig {

	private Integer id;
	private Integer maxlevel;
	private Integer jiangexp;
	private Integer minlevel;
	private Integer jiangmoney;
	private Integer jiangzhenqi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMaxlevel() {
		return maxlevel;
	}

	public void setMaxlevel(Integer maxlevel) {
		this.maxlevel = maxlevel;
	}
	public Integer getJiangexp() {
		return jiangexp;
	}

	public void setJiangexp(Integer jiangexp) {
		this.jiangexp = jiangexp;
	}
	public Integer getMinlevel() {
		return minlevel;
	}

	public void setMinlevel(Integer minlevel) {
		this.minlevel = minlevel;
	}
	public YaBiaoJiangLiConfig copy(){
		return null;
	}

	public Integer getJiangmoney() {
		return jiangmoney;
	}

	public void setJiangmoney(Integer jiangmoney) {
		this.jiangmoney = jiangmoney;
	}

	public Integer getJiangzhenqi() {
		return jiangzhenqi;
	}

	public void setJiangzhenqi(Integer jiangzhenqi) {
		this.jiangzhenqi = jiangzhenqi;
	}
}
