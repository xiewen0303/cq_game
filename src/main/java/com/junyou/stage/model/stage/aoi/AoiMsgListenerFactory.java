package com.junyou.stage.model.stage.aoi;

import com.junyou.stage.model.core.stage.aoi.AOI;
import com.junyou.stage.model.core.stage.aoi.IAoiListener;
import com.junyou.stage.model.core.stage.aoi.IAoiListenerFactory;

public class AoiMsgListenerFactory implements IAoiListenerFactory{

	@Override
	public IAoiListener create(AOI aoi) {
		return new AoiMsgListener(aoi);
	}
}
