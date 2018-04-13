/**
 * 
 */
package com.junyou.stage.model.stage.aoi;

import com.junyou.cmd.ClientCmdType;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.aoi.AOI;
import com.junyou.stage.model.core.stage.aoi.AbsAoiMsgListener;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;

/**
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-26 下午4:37:39
 */
public class AoiMsgListener extends AbsAoiMsgListener {

	public AoiMsgListener(AOI aoi){
		super(aoi);
	}
	
	protected short getEnterCommand(ElementType elementType) {
		if( ElementType.isRole(elementType) ){
			return ClientCmdType.AOI_ROLES_ENTER;
		}
		if( ElementType.isMonster(elementType) ){
			return ClientCmdType.AOI_MONSTERS_ENTER;
		}
		if( ElementType.isGoods(elementType) ){
			return ClientCmdType.AOI_GOODS_ENTER;
		}
		if( ElementType.isBiaoChe(elementType) ){
			return ClientCmdType.AOI_BIAO_CHE;
		}
		if( ElementType.isPet(elementType) ){
			return ClientCmdType.AOI_PET;
		}
		if( ElementType.isCollects(elementType) ){
			return ClientCmdType.AOI_COLLECT;
		}
		return 0;
	}

	@Override
	protected void notifyMsg(Long userRoleId, Short command, Object data) {
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2One(userRoleId, command, data);
		}else{
			StageMsgSender.send2One(userRoleId, command, data);
		}
	}

	@Override
	protected void notifyMsg(Object[] userRoleIds, Short command, Object data) {
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2Many(userRoleIds, command, data);
		}else{
			StageMsgSender.send2Many(userRoleIds, command, data);
		}
	}
}