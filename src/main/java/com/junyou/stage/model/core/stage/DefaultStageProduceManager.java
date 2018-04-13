package com.junyou.stage.model.core.stage;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.junyou.log.ChuanQiLog;

public class DefaultStageProduceManager implements IStageProduceManager {

	private IStage stage;
	
	public DefaultStageProduceManager(IStage stage){
		this.stage = stage;
	}
	
	private Map<ElementType, Map<String, IElementProduceTeam>> elementProduceTeamMaps = new HashMap<ElementType, Map<String,IElementProduceTeam>>();
	
	@Override
	public Collection<IElementProduceTeam> getElementProduceTeams(
			ElementType elementType) {
		return elementProduceTeamMaps.get(elementType).values();
	}

	@Override
	public IElementProduceTeam getElementProduceTeam(ElementType elementType,
			String teamId) {
		Map<String, IElementProduceTeam> elementProduceTeamMap = elementProduceTeamMaps.get(elementType);
		if( elementProduceTeamMap == null ) return null;
		return elementProduceTeamMap.get(teamId);
	}
	
	@Override
	public void addElementProduceTeam(IElementProduceTeam elementProduceTeam) {
		Map<String, IElementProduceTeam> produceTeams = elementProduceTeamMaps.get(elementProduceTeam.getElementType());
		if( produceTeams == null ){
			produceTeams = new HashMap<String, IElementProduceTeam>();
			elementProduceTeamMaps.put(elementProduceTeam.getElementType(), produceTeams);
		}
		
		produceTeams.put(elementProduceTeam.getId(), elementProduceTeam);
		elementProduceTeam.setStage(stage);
	}
	
	public void removeElementProduceTeam(ElementType elementType,String teamId){
		Map<String, IElementProduceTeam> elementProduceTeamMap = elementProduceTeamMaps.get(elementType);
		if( elementProduceTeamMap != null ){
			
			synchronized (this) {
				elementProduceTeamMap.remove(teamId);
			}
		}
	}

	@Override
	public void produceAll() {
		for( Map<String, IElementProduceTeam> produceTeamMap : elementProduceTeamMaps.values() ){
			for( IElementProduceTeam produceTeam : produceTeamMap.values() ){
				try{
					if(produceTeam.isDelayProduct()){
						produceTeam.schedule();
					}else{
						produceTeam.produceAll();
					}
				}catch (Exception e) {
					ChuanQiLog.error("", e);
				}
			}
		}
	}

}
