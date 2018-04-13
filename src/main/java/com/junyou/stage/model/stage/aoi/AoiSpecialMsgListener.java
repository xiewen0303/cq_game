/**
 * 
 */
package com.junyou.stage.model.stage.aoi;

import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.aoi.AOI;
import com.junyou.stage.model.core.stage.aoi.AbsAoiSpecialMsgListener;

/**
 * 特殊的AOI监听 ,不希望看到推送
 * @author DaoZheng Yuan
 * 2014-8-5 下午2:37:50
 */
public class AoiSpecialMsgListener extends AbsAoiSpecialMsgListener {

	public AoiSpecialMsgListener(AOI aoi){
		super(aoi);
	}
	
	protected String getEnterCommand(ElementType elementType) {
		return null;
	}

	@Override
	protected String getLeaveCommand(ElementType elementType) {
		return null;
	}

	@Override
	protected void notifyMsg(String userRoleId, String command, Object data) {
		
	}

	@Override
	protected void notifyMsg(Object[] userRoleIds, String command, Object data) {
		
	}
	
	
}
