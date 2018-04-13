package com.junyou.bus.share.lockKey;

import com.junyou.gs.share.thread.RoleidThreadShare;
import com.kernel.sync.aop.ILockKey;

public class BusLockKey implements ILockKey {

	@Override
	public Long getKey() {
		return RoleidThreadShare.getRoleId();
	}

}
