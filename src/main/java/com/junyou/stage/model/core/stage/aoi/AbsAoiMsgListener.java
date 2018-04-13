package com.junyou.stage.model.core.stage.aoi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.cmd.ClientCmdType;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.element.monster.AbsHurtRankMonster;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.StageMsgSender;

public abstract class AbsAoiMsgListener implements IAoiListener {

	private AOI aoi;
	
	public AbsAoiMsgListener(AOI aoi){
		this.aoi = aoi;
	}
	
	@Override
	public void elementEnter(IStageElement element, int direction) {
		if(!element.isBeiKuan()){
			return;
		}
		
		int oppositeDirection = aoi.getOppositeDirection(direction);
		boolean isRole = false;
		//每次role位置发生变化，立马通知相关联aoi
		if( ElementType.isRole(element.getElementType()) ){
			
			aoi.notifyRoleChange(oppositeDirection);
			isRole = true;
		}
		
		Object[] userRoleIds = null;
		if(isRole){
			IRole role = (IRole)element;
			
			if(!role.isYinshen()){//非隐身状态才进入其他角色AOI
				userRoleIds = getElementIdsAndLog(ElementType.ROLE, oppositeDirection, element.getId());
			}
		}else{
			userRoleIds = getElementIds(ElementType.ROLE, oppositeDirection);
		}
		
		if( userRoleIds != null ){
			Short command = element.getStage().getSameMoCmd();
			if(command != null){
				Object data = element.getSameMsgData(command);
				if(data == null){
					command = null;
				}else{
					notifyMsg(userRoleIds, command, new Object[]{data});
				}
			}
			//通知周边角色，有新元素进入
			if(command == null){
//				command = getEnterCommand(element.getElementType());
				command = element.getEnterCommand();
				
				if( ElementType.isMonster(element.getElementType()) ){
					Monster monster = (Monster) element;
					//只向客户端推送活着的怪物
					if(monster.getStateManager().getClientState()){
						notifyMsg(userRoleIds, command, new Object[]{element.getMsgData()});
					}
				}else{
					notifyMsg(userRoleIds, command, new Object[]{element.getMsgData()});
				}
			}
		}
		
		//如果新元素为角色，则推送给他周边元素(NPC、角色、怪物)进入视野信息
		if( ElementType.isRole(element.getElementType()) ){
			notifySurroundElementsEnterMsg((IRole)element, oppositeDirection);
		}
	}

	/**
	 * 获取周围元素进入信息
	 */
	protected void notifySurroundElementsEnterMsg(IRole role,int direction){
		
		Short sameCmd = role.getStage().getSameMoCmd();
		//周边角色信息
		Object roleInfos = getElementRoleInfos(direction,sameCmd);
		if(null != roleInfos){
			if(sameCmd == null){
//				sameCmd = getEnterCommand(ElementType.ROLE);
				sameCmd = role.getEnterCommand();
			}
			notifyMsg(role.getId(),sameCmd ,roleInfos);
		}
		
		//周围元素信息
		for (ElementType type : ElementType.elementClient) {
			Object[] infos = getElementInfos(type, direction,sameCmd);
			if(null != infos){
//				notifyMsg(element.getId(), getEnterCommand(type), infos);
				notifyMsg(role.getId(), (short)infos[0], infos[1]);
			}
		}
		
		//如果有野外boss进入视野，需要刷出伤害排行榜数据给客服端 
		Collection<IStageElement> elements = aoi.getElements(ElementType.MONSTER , direction);
		if(elements != null && elements.size() > 0){
			for (IStageElement iStageElement : elements) {
				if(iStageElement.isShowHurtRank()){
					AbsHurtRankMonster monster = (AbsHurtRankMonster)iStageElement;
					Object[] result = monster.getRankData(monster.getId(role));
					if(result != null){
						StageMsgSender.send2One(role.getId(), monster.getNoticeCmd(), result);
						break;//同一时间只推送第一只BOSS伤害排行
					}
				}
			}
		}
	}

	/**
	 * 获取周围元素离开信息
	 */
	protected void notifySurroundElementsLeaveMsg(IStageElement element,int direction){
		short levelComm = ClientCmdType.AOI_ELEMENT_LEAVE;
		//周边角色信息
		Object roleIds = getElementIds(ElementType.ROLE, direction);
		if(null != roleIds){
			notifyMsg(element.getId(),levelComm, roleIds);
		}
		
		//周围元素信息
		for (ElementType type : ElementType.elementClient) {
			if(ElementType.isMonster(type)){
				Collection<IStageElement> list = aoi.getElements(type, direction);
				if(list != null){
					for (IStageElement imonster : new ArrayList<>(list)) {
						if(imonster.isShowHurtRank()){
							AbsHurtRankMonster monster = (AbsHurtRankMonster)imonster;
							StageMsgSender.send2One(element.getId(), monster.getCloseRankCmd(), null);
							break;
						}
					}
				}
			}
			Object infos = getElementIds(type, direction);
			if(null != infos){
				notifyMsg(element.getId(), getLeaveCommand(type), infos);
			}
		}
		
	}
	
	private Object[] getElementInfos(ElementType elementType, int direction,Short sameCmd){
		
		Collection<IStageElement> elements = aoi.getElements(elementType, direction);
		
		if( elements != null && elements.size() > 0 ){
			Object[] elementIds = new Object[elements.size()];
			int i = 0;
			short cmd = 0;
			for( IStageElement tmp : elements ){
				Object data = tmp.getSameMsgData(sameCmd);
				if(data == null){
					elementIds[i] = tmp.getMsgData();
					cmd = tmp.getEnterCommand();
				}else{
					cmd = sameCmd;
					elementIds[i] = data;
				}
				i++;
			}
			return new Object[]{cmd,elementIds};
		}
		return null;
	}
	
	private Object[] getElementRoleInfos(int direction,Short sameCmd){
		Collection<IStageElement> elements = aoi.getElements(ElementType.ROLE, direction);
		if( elements != null && elements.size() > 0 ){
			List<Object> elementIds = new ArrayList<Object>();
			
			for( IStageElement tmp : elements ){
				IRole role = (IRole) tmp;
				
				if(!role.isYinshen()){//隐身或摆摊状态不进入其他玩家AOI
					if(sameCmd == null){
						elementIds.add(tmp.getMsgData());
					}else{
						elementIds.add(role.getSameMsgData(sameCmd));
					}
				}
			}
			
			if(elementIds.size() > 0){
				return elementIds.toArray();
			}
		}
		
		return null;
	}
	
	
	@Override
	public void elementLeave(IStageElement element, int direction) {
		if(!element.isBeiKuan()){
			return;
		}
		
		int oppositeDirection = aoi.getOppositeDirection(direction);
		
		//每次role位置发生变化，立马通知相关联aoi
		if( ElementType.isRole(element.getElementType()) ){
			aoi.notifyRoleChange(oppositeDirection);
		}
		
		//通知周边角色，有新元素离开
		Object[] userRoleIds = getElementIds(ElementType.ROLE, oppositeDirection);
		if( userRoleIds != null ){ 
			//通知周边角色，有新元素离开
			Short command = getLeaveCommand(element.getElementType()); 
			
			notifyMsg(userRoleIds,command, new Object[]{element.getId()});
		}
		
		//如果元素为角色，则推送给他周边元素(NPC、角色、怪物)离开视野信息
		if( ElementType.isRole(element.getElementType()) ){
			
			notifySurroundElementsLeaveMsg(element, oppositeDirection);
			
		}else if(ElementType.isMonster(element.getElementType()) && element.isShowHurtRank()){
			AbsHurtRankMonster monster = (AbsHurtRankMonster)element;
			StageMsgSender.send2Many(userRoleIds, monster.getCloseRankCmd(), null);
		}
	}
	
	private Short getLeaveCommand(ElementType elementType) {
		if( ElementType.isRole(elementType) || ElementType.isMonster(elementType) || ElementType.isPet(elementType)|| ElementType.isBiaoChe(elementType) || ElementType.isCollects(elementType)|| ElementType.isChongwu(elementType)){
			return ClientCmdType.AOI_ELEMENT_LEAVE;
		}
		if( ElementType.isGoods(elementType) ){
			return ClientCmdType.AOI_GOODS_LEAVE;
		}
		return null;
	}

	public Object[] getElementIds(ElementType type, int direction){
		
		Collection<IStageElement> elements = aoi.getElements(type, direction);
		
		if( elements != null && elements.size() > 0 ){
			List<Long> elementIds = new ArrayList<Long>();
			
			for( IStageElement tmp : elements ){
				if(tmp.isBeiKuan()){
					elementIds.add(tmp.getId());
				}
			}
			
			if(elementIds.size() > 0){
				return elementIds.toArray();
			}
		}
		
		return null;
	}
	
	public Object[] getElementIdsAndLog(ElementType elementType, int direction,Long selfId){
		
		Collection<IStageElement> elements = aoi.getElements(elementType, direction);
		
		if( elements != null && elements.size() > 0 ){
			List<Long> elementIds = new ArrayList<Long>();
			
			for( IStageElement tmp : elements ){
				
				if(tmp.getId().equals(selfId)){
					ChuanQiLog.error("debug role enter notify error selfId:"+selfId);
				}
				
				if(tmp.isBeiKuan()){
					elementIds.add(tmp.getId());
				}
			}
			
			if(elementIds.size() > 0){
				return elementIds.toArray();
			}
		}
		return null;
	}

	/**
	 * 获取指定元素进入视野的消息指令
	 * @param elementType 指定类型
	 */
	protected abstract short getEnterCommand(ElementType elementType);

	/**
	 * 通知角色周边的元素信息
	 * @param roleId
	 * @param command
	 * @param data
	 */
	protected abstract void notifyMsg(Long userRoleId, Short command, Object data);
	/**
	 * 通知角色周边的元素信息
	 * @param roleId
	 * @param command
	 * @param data
	 */
	protected abstract void notifyMsg(Object[] userRoleIds, Short command, Object data);
}
