package com.kernel.data.dao;

import com.junyou.utils.datetime.GameSystemTime;

public abstract class AbsVersion implements IVersion {

	private long version = GameSystemTime.getSystemMillTime();
	
	@Override
	public String getVersion() {
		return String.valueOf(version);
	}

	@Override
	public void updateVersion() {
		this.version = GameSystemTime.getSystemMillTime();
	}

}
