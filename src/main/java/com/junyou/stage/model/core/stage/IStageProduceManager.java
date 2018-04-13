package com.junyou.stage.model.core.stage;

import java.util.Collection;

public interface IStageProduceManager {

	/**
	 * 获取元素的生产组集合
	 * @param elementType
	 * @return
	 */
	public Collection<IElementProduceTeam> getElementProduceTeams(ElementType elementType);
	
	/**
	 * 获取生产组
	 * @param elementType
	 * @param teamId
	 * @return
	 */
	public IElementProduceTeam getElementProduceTeam(ElementType elementType, String teamId);
	
	/**
	 * 增加生产组
	 * @param element
	 */
	public void addElementProduceTeam(IElementProduceTeam elementTeam);
	
	
	/**
	 * 去除生产组
	 * @param elementType
	 * @param teamId
	 */
	public void removeElementProduceTeam(ElementType elementType,String teamId);
	
	/**
	 * 生产所有可生产元素
	 */
	public void produceAll();
}
