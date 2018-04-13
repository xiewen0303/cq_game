package com.junyou.bus.kuafuarena1v1.configure.export;

import com.kernel.data.dao.AbsVersion;

public class KuaFuJingJiZongBiaoConfig extends AbsVersion {
	private Integer id;
	private Integer limitTimes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(Integer limitTimes) {
		this.limitTimes = limitTimes;
	}

}
