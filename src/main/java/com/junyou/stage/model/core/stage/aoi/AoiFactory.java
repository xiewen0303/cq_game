package com.junyou.stage.model.core.stage.aoi;


public class AoiFactory {
	
	private IAoiListenerFactory aoiListenerFactory;
	
	public AoiFactory(IAoiListenerFactory aoiListenerFactory){
		this.aoiListenerFactory = aoiListenerFactory;
	}
	
	public AOI create(AoiPoint aoiPoint, AOIManager aoiManager){
		AOI aoi = new AOI(aoiPoint, aoiManager);
		aoi.setListener(aoiListenerFactory.create(aoi));
		
		return aoi;
	}
}
