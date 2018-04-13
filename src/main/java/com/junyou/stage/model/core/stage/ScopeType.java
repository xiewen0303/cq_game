package com.junyou.stage.model.core.stage;

import com.junyou.stage.utils.OffUtils;

/**
 * 格子或者是像素，一个格子是60*60
 */
public enum ScopeType {
	GRID,PIXEL;
	
	public static int grid2Pixel(int gridCount){
		return gridCount * OffUtils.GAIL_PX;
	}
}
