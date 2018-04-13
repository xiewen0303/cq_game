package com.junyou.stage.model.core.stage.aoi;

import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;

public abstract class AbsAoiSpecialMsgListener implements IAoiListener {

	@SuppressWarnings("unused")
	private AOI aoi;
	
	public AbsAoiSpecialMsgListener(AOI aoi){
		this.aoi = aoi;
	}
	
	@Override
	public void elementEnter(IStageElement element, int direction) {
		
	}
	
	/**
	 * 获取周围元素进入信息
	 */
	protected void notifySurroundElementsEnterMsg(IStageElement element,int direction){
		
	}

	/**
	 * 获取周围元素离开信息
	 */
	protected void notifySurroundElementsLeaveMsg(IStageElement element,int direction){
		
	}
	
	
	@Override
	public void elementLeave(IStageElement element, int direction) {
		
	}

	public Object[] getElementIds(ElementType elementType, int direction){
		return null;
	}
	
	public Object[] getElementIdsAndLog(ElementType elementType, int direction,String selfId){
		return null;
	}

	/**
	 * 获取指定元素进入视野的消息指令
	 * @param elementType 指定类型
	 */
	protected abstract String getEnterCommand(ElementType elementType);
	
	/**
	 * 获取指定元素离开视野的消息指令
	 * @param elementType 指令类型
	 */
	protected abstract String getLeaveCommand(ElementType elementType);

	/**
	 * 通知角色周边的元素信息
	 * @param roleId
	 * @param command
	 * @param data
	 */
	protected abstract void notifyMsg(String userRoleId, String command, Object data);
	/**
	 * 通知角色周边的元素信息
	 * @param roleId
	 * @param command
	 * @param data
	 */
	protected abstract void notifyMsg(Object[] userRoleIds, String command, Object data);
}
