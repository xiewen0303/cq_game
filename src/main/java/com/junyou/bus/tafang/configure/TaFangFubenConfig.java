package com.junyou.bus.tafang.configure;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.stagecontroll.MapType;

/**
 * @author LiuYu
 * 2015-10-13 上午9:50:34
 */
public class TaFangFubenConfig extends AbsFubenConfig{

	@Override
	public int getMapType() {
		return MapType.TAFANG_FUBEN_MAP;
	}

	@Override
	public int getFubenType() {
		return 0;
	}

	@Override
	public short getExitCmd() {
		return 0;
	}

	@Override
	public boolean isAutoProduct() {
		return false;
	}

}
